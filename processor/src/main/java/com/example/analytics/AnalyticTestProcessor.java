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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.lang.model.util.ElementFilter.fieldsIn;
import static javax.lang.model.util.ElementFilter.methodsIn;

/**
 * Auto generate test code for Analytic tests.
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
                debug(typeElement.getQualifiedName().toString());

                AnalyticMapMethod analyticMapMethod = extractAnalyticMap(typeElement);

                final AnalyticTestClass analyticTestClass = new AnalyticTestClass(typeElement);
                final List<AnalyticVarField> analyticVarFields = extractOmnitureFieldVar(analyticTestClass);

                AnalyticTestCodeGenerator codeGenerator = new AnalyticTestCodeGenerator(analyticTestClass, analyticMapMethod);
                codeGenerator.generateCode(analyticVarFields, elementUtils, filer);
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
                checkAnalyticMapMethod(executableElement);
                return new AnalyticMapMethod(executableElement);
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
            checkAnalyticVariable(analyticFieldAdapter);

            return new AnalyticVarField(analyticFieldAdapter);
        }

        return null;
    }


    /**
     * The method to get map must be public static, and it must take no parameter, and return {@link java.util.Map}
     */
    private void checkAnalyticMapMethod(ExecutableElement element) throws ProcessingException {

        final Set<Modifier> modifiers = element.getModifiers();

        if (!modifiers.contains(Modifier.PUBLIC)) {
            throw new ProcessingException(element, "The @%s method must be public field", AnalyticMap.class.getSimpleName());
        }

        if (!modifiers.contains(Modifier.STATIC)) {
            throw new ProcessingException(element, "The @%s method must be static field", AnalyticMap.class.getSimpleName());
        }

        final List<? extends VariableElement> parameters = element.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            throw new ProcessingException(element, "The @%s method must have no parameter", AnalyticMap.class
                    .getSimpleName());
        }

        final TypeMirror returnType = element.getReturnType();
        String returnClass = ((TypeElement) typeUtils.asElement(returnType)).getQualifiedName().toString();
        String mapClass = Map.class.getCanonicalName();
        if (!mapClass.equals(returnClass)) {
            throw new ProcessingException(element, "The @%1s method's return type must be %2s, but get %3s",
                    AnalyticMap.class.getSimpleName(), mapClass, returnClass);
        }
    }

    /**
     * Make sure the field is a static final constant field field
     */
    private void checkAnalyticVariable(AnalyticFieldAdapter fieldAdapter) throws ProcessingException {
        // Make sure it is and static
        final VariableElement variableElement = fieldAdapter.getVariableElement();

        if (!(java.lang.reflect.Modifier.isStatic(fieldAdapter.getModifiers()))) {
            throw new ProcessingException(variableElement, "The field @%s must be static field", AnalyticVar.class
                    .getSimpleName());
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
