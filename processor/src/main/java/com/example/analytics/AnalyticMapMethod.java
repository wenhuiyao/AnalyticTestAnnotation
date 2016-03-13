package com.example.analytics;

import example.android.wenhui.annotation.AnalyticMap;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wyao on 3/8/16.
 */
public class AnalyticMapMethod {

    private String simpleName;

    private TypeMirror valueType;

    public AnalyticMapMethod(ExecutableElement element, Types typeUtils, Elements elementUtils) throws
            ProcessingException {
        validateMethod(element);

        valueType = getReturnMapValueType(element, typeUtils, elementUtils);

        simpleName = element.getSimpleName().toString();

    }

    public String getSimpleName() {
        return simpleName;
    }

    public TypeMirror getValueType() {
        return valueType;
    }

    /**
     * Make sure the @{@linkpain AnalyticMapMethod} is public static, and takes no argument.
     *
     * @param element
     * @throws ProcessingException
     */
    private void validateMethod(ExecutableElement element) throws
            ProcessingException {
        final Set<Modifier> modifiers = element.getModifiers();

        if (!modifiers.contains(Modifier.PUBLIC)) {
            throw new ProcessingException(element, "The @%s method must be public", AnalyticMap.class.getSimpleName());
        }

        if (!modifiers.contains(Modifier.STATIC)) {
            throw new ProcessingException(element, "The @%s method must be static", AnalyticMap.class.getSimpleName());
        }

        final List<? extends VariableElement> parameters = element.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            throw new ProcessingException(element, "The @%s method must have no parameter", AnalyticMap.class
                    .getSimpleName());
        }


    }

    /**
     * The method must returns {@linkplain java.util.Map}. The returned map object must take the {@linkplain String} as
     * key.
     * @param element
     * @param typeUtils
     * @param elementUtils
     * @throws ProcessingException
     */
    private TypeMirror getReturnMapValueType(ExecutableElement element, Types typeUtils, Elements elementUtils) throws
            ProcessingException {
        final TypeMirror returnType = element.getReturnType();
        if( returnType.getKind() != TypeKind.DECLARED ){
            throw new ProcessingException(element, "The @%1s method's return type must be %2s, but get %3s",
                    AnalyticMap.class.getSimpleName(), DeclaredType.class.getCanonicalName(), returnType);
        }

        String returnClass = ((TypeElement) typeUtils.asElement(returnType)).getQualifiedName().toString();
        if( returnType.getKind() != TypeKind.DECLARED || !Map.class.getCanonicalName().equals(returnClass)){
            throw new ProcessingException(element, "The @%1s method's return type must be %2s, but get %3s",
                    AnalyticMap.class.getSimpleName(), Map.class.getCanonicalName(), returnType);
        }

        final List<? extends TypeMirror> typeArguments = ((DeclaredType) returnType).getTypeArguments();
        if( typeArguments == null || typeArguments.size() != 2 ){
            throw new ProcessingException(element, "Hmmm, the type parameter of the return type isn't 2");
        }

        TypeMirror keyType = typeArguments.get(0);
        // Check if the key is String type, we currently only support String as key
        final TypeMirror stringType = elementUtils.getTypeElement(String.class.getCanonicalName()).asType();
        if( !typeUtils.isSameType(keyType, stringType) ){
            throw new ProcessingException(element, "@%1s only supports %2s", AnalyticMapMethod.class
                    .getSimpleName(), String.class.getCanonicalName());
        }

        return typeArguments.get(1);
    }
}
