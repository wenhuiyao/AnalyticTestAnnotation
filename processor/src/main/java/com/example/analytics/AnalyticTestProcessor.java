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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static javax.lang.model.util.ElementFilter.fieldsIn;

/**
 * Auto generate test code for Analytic tests.
 * <p/>
 * Created by wyao on 3/8/16.
 */
@AutoService(Processor.class)
public class AnalyticTestProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
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
                debug(typeElement.getQualifiedName().toString());

                AnalyticMapField analyticMapField = extractAnalyticMap(typeElement);

                final AnalyticTestClass analyticTestClass = new AnalyticTestClass(typeElement);
                final List<AnalyticVarField> analyticVarFields = extractOmnitureFieldVar(analyticTestClass);

                AnalyticTestCodeGenerator codeGenerator = new AnalyticTestCodeGenerator(analyticTestClass, analyticMapField);
                codeGenerator.generateCode(analyticVarFields, elementUtils, filer);
            }

        } catch (ProcessingException e) {
            error(e.getElement(), e.getMessage());
        } catch (IOException e) {
            error(null, e.getMessage());
        }

        return true;
    }

    private AnalyticMapField extractAnalyticMap(TypeElement typeElement) throws ProcessingException {
        final List<VariableElement> variableElements = fieldsIn(typeElement.getEnclosedElements());
        for (VariableElement variableElement : variableElements) {
            final AnalyticMap annotation = variableElement.getAnnotation(AnalyticMap.class);
            if (annotation != null) {
                checkAnalyticMapVariable(variableElement);
                return new AnalyticMapField(variableElement);
            }
        }

        throw new ProcessingException(typeElement, "There is must be one @%s in %s", AnalyticMap.class
                .getCanonicalName(), typeElement.getQualifiedName().toString());
    }


    private List<AnalyticVarField> extractOmnitureFieldVar(AnalyticTestClass analyticTestClass)
            throws ProcessingException {
        ArrayList<AnalyticVarField> result = new ArrayList<>(20);

        if (analyticTestClass.getVarClass() != null) {
            final Class varClass = analyticTestClass.getVarClass();

            Field[] fields = varClass.getDeclaredFields();
            for (Field field : fields) {
                AnalyticVarField f = newAnalyticField(new AnalyticFieldAdapter(field));
                if (f != null) {
                    result.add(f);
                    debug(f.getFieldName());
                }
            }

        } else {
            final List<VariableElement> variableElements = fieldsIn(analyticTestClass.getVarTypeElement().getEnclosedElements());

            for (VariableElement variableElement : variableElements) {
                AnalyticVarField f = newAnalyticField(new AnalyticFieldAdapter(variableElement));
                if (f != null) {
                    result.add(f);
                    debug(f.getFieldName());
                }
            }
        }

        return result;
    }

    /**
     * @return A {@link AnalyticVarField} object if the field is annotated {@link AnalyticVar}, otherwise return null.
     */
    private AnalyticVarField newAnalyticField(AnalyticFieldAdapter analyticFieldAdapter) throws ProcessingException {
        final AnalyticVar annotation = analyticFieldAdapter.getAnnotation(AnalyticVar.class);
        if (annotation != null) {
            // Make sure it is what we expected, static final field
            checkOmnitureVariable(analyticFieldAdapter);

            return new AnalyticVarField(analyticFieldAdapter);
        }

        return null;
    }


    private void checkAnalyticMapVariable(VariableElement variableElement) throws ProcessingException {

        final Set<Modifier> modifiers = variableElement.getModifiers();

        if (!modifiers.contains(Modifier.PUBLIC)) {
            throw new ProcessingException(variableElement, "The field @% must be static field", variableElement
                    .getSimpleName());
        }
    }

    /**
     * Make sure the field is a static final constant field field
     */
    private void checkOmnitureVariable(AnalyticFieldAdapter fieldAdapter) throws ProcessingException {
        // Make sure it is and static
        final VariableElement variableElement = fieldAdapter.getVariableElement();

        final String simpleName = fieldAdapter.getSimpleName();
        if (!(java.lang.reflect.Modifier.isStatic(fieldAdapter.getModifiers()))) {
            throw new ProcessingException(variableElement, "The field @%s must be static field", simpleName);
        }

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

    private void debug(Object message) {
        System.out.println("Debug: " + message);
    }
}
