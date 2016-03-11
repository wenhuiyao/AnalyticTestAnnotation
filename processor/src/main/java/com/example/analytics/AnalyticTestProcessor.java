package com.example.analytics;

import com.google.auto.service.AutoService;
import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static javax.lang.model.util.ElementFilter.fieldsIn;
import static javax.lang.model.util.ElementFilter.methodsIn;

/**
 * Auto generate test helper method for Analytic constant variables.
 * <p/>
 * Created by wyao on 3/8/16.
 */
@AutoService(Processor.class)
public class AnalyticTestProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(AnalyticTest.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        try {
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(AnalyticTest.class)) {

                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(annotatedElement, "only class can be annotated with @%s",
                            AnalyticTest.class.getName());
                }

                // We know it is a class type
                TypeElement typeElement = (TypeElement) annotatedElement;
                debug("Found %1s annotated @%2s", typeElement.getQualifiedName(), AnalyticTest.class.getSimpleName());

                AnalyticMapMethod analyticMapMethod = extractAnalyticMap(typeElement);

                final AnalyticTestClass analyticTestClass = new AnalyticTestClass(typeElement, elementUtils);
                final List<AnalyticVarField> analyticVarFields = extractOmnitureFieldVar(analyticTestClass);

                AnalyticTestCodeGenerator codeGenerator = new AnalyticTestCodeGenerator(analyticTestClass, analyticMapMethod);
                codeGenerator.generateCode(analyticVarFields, elementUtils, typeUtils, filer);
            }

        } catch (ProcessingException e) {
            error(e.getElement(), e.getMessage());
        } catch (IOException e) {
            error(null, e.getMessage());
        }

        return true;
    }

    private AnalyticMapMethod extractAnalyticMap(TypeElement typeElement) throws ProcessingException {
        final List<ExecutableElement> executableElements = methodsIn(typeElement.getEnclosedElements());
        for (ExecutableElement executableElement : executableElements) {
            final AnalyticMap annotation = executableElement.getAnnotation(AnalyticMap.class);
            if (annotation != null) {
                return new AnalyticMapMethod(executableElement, typeUtils, elementUtils);
            }
        }
        throw new ProcessingException(typeElement, "There is must be one @%1s in %2s", AnalyticMap.class
                .getCanonicalName(), typeElement.getQualifiedName().toString());
    }


    private List<AnalyticVarField> extractOmnitureFieldVar(AnalyticTestClass analyticTestClass)
            throws ProcessingException {
        ArrayList<AnalyticVarField> result = new ArrayList<>(20);

        final List<VariableElement> variableElements = fieldsIn(analyticTestClass.getVarTypeElement().getEnclosedElements());

        for (VariableElement variableElement : variableElements) {
            final AnalyticVar annotation = variableElement.getAnnotation(AnalyticVar.class);
            if (annotation != null) {
                debug("Auto generate test helper method for " + variableElement.getSimpleName());
                result.add(new AnalyticVarField(variableElement));
            }
        }
        return result;
    }


    /**
     * Prints an error message
     *
     * @param e The element which has caused the error. Can be null
     * @param msg The error message
     */
    private void error(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR, AnalyticTestProcessor.class.getCanonicalName() + ": " + msg, e);
    }

    private void debug(String message, Object... args) {
        System.out.println("Debug: " + String.format(message, args));
    }
}
