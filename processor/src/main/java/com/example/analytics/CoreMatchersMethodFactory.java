package com.example.analytics;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.ParameterSpec;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.reflect.Type;
import java.util.HashMap;

import static example.android.wenhui.annotation.AnalyticMatchers.*;
/**
 * A factory to assist generate method code for hamcrest core matcher methods
 * Created by wyao on 3/8/16.
 */
public class CoreMatchersMethodFactory {

    private static final HashMap<String, CoreMatchersMethod> sMethodMap = new HashMap<>(6);

    static{
        sMethodMap.put(EQUAL_TO, new EqualTo());
        sMethodMap.put(NOT_NULL_VALUE, new NotNullValue());
        sMethodMap.put(NULL_VALUE, new NullValue());
        sMethodMap.put(CONTAINS_STRING, new ContainsString());
        sMethodMap.put(ANY_OF, new AnyOf());
        sMethodMap.put(IS, new Is());
    }

    public static CoreMatchersMethod newMethod(String methodName){
        return sMethodMap.get(methodName);
    }

    public static class EqualTo implements CoreMatchersMethod {

        private final String parameter = "obj";

        @Override
        public TypeMirror expectedObjectType(Elements elementUtils, Types typeUtils, TypeMirror mapValueType) {
            return mapValueType;
        }

        @Override
        public boolean isParameterVarargs() {
            return false;
        }

        @Override
        public ParameterSpec[] parameters() {
            return singleParameterSpec(Object.class, parameter);
        }

        @Override
        public CodeBlock methodBlock() {
            return singleParameterMethodBlock(EQUAL_TO, parameter);
        }

    }

    public static class ContainsString implements CoreMatchersMethod {

        private final String parameter = "str";

        @Override
        public TypeMirror expectedObjectType(Elements elementUtils, Types typeUtils, TypeMirror mapValueType) throws
                ProcessingException {
            TypeMirror stringType = fromClassToTypeMirror(elementUtils, String.class);
            if( !typeUtils.isAssignable(stringType, mapValueType) ){
                throw new ProcessingException(null, "The value type %1s in map @%2s isn't super type of String",
                        mapValueType.toString(), AnalyticMapMethod.class.getSimpleName());
            }
            return stringType;
        }

        @Override
        public boolean isParameterVarargs() {
            return false;
        }

        @Override
        public ParameterSpec[] parameters() {
            return singleParameterSpec(String.class, parameter);
        }

        @Override
        public CodeBlock methodBlock() {
            return singleParameterMethodBlock(CONTAINS_STRING, parameter);
        }
    }

    /**
     * anyOf(org.hamcrest.Matcher<? super T>... matchers)
     */
    public static class AnyOf implements CoreMatchersMethod {

        private String parameter = "matchers";

        @Override
        public TypeMirror expectedObjectType(Elements elementUtils, Types typeUtils, TypeMirror mapValueType) {
            return mapValueType;
        }

        @Override
        public boolean isParameterVarargs() {
            return true;
        }

        @Override
        public ParameterSpec[] parameters() {
            return singleParameterSpec(Matcher[].class, parameter);
        }

        @Override
        public CodeBlock methodBlock() {
            return singleParameterMethodBlock(ANY_OF, parameter);
        }
    }

    public static class Is implements CoreMatchersMethod {

        private String parameter = "object";

        @Override
        public TypeMirror expectedObjectType(Elements elementUtils, Types typeUtils, TypeMirror mapValueType) {
            return mapValueType;
        }

        @Override
        public boolean isParameterVarargs() {
            return false;
        }

        @Override
        public ParameterSpec[] parameters() {
            return singleParameterSpec(Object.class, parameter);
        }

        @Override
        public CodeBlock methodBlock() {
            return singleParameterMethodBlock(IS, parameter);
        }
    }

    public static class NotNullValue implements CoreMatchersMethod {

        @Override
        public TypeMirror expectedObjectType(Elements elementUtils, Types typeUtils, TypeMirror mapValueType) {
            return mapValueType;
        }

        @Override
        public boolean isParameterVarargs() {
            return false;
        }

        @Override
        public ParameterSpec[] parameters() {
            return null;
        }

        @Override
        public CodeBlock methodBlock() {
            return noParameterMethodBlock(NOT_NULL_VALUE);
        }
    }

    public static class NullValue implements CoreMatchersMethod {

        @Override
        public TypeMirror expectedObjectType(Elements elementUtils, Types typeUtils, TypeMirror mapValueType) {
            return mapValueType;
        }

        @Override
        public boolean isParameterVarargs() {
            return false;
        }

        @Override
        public ParameterSpec[] parameters() {
            return null;
        }

        @Override
        public CodeBlock methodBlock() {
            return noParameterMethodBlock(NULL_VALUE);
        }
    }


    private static ParameterSpec[] singleParameterSpec(Type type, String parameterName ){
        ParameterSpec.Builder builder = ParameterSpec.builder(type, parameterName);
        return new ParameterSpec[]{builder.build()};
    }

    private static CodeBlock noParameterMethodBlock(String methodName){
        return CodeBlock.builder().add("$T.$L()", CoreMatchers.class, methodName).build();
    }

    private static CodeBlock singleParameterMethodBlock(String methodName, String parameter){
        return CodeBlock.builder().add("$T.$L($L)", CoreMatchers.class, methodName, parameter).build();
    }

    static TypeMirror fromClassToTypeMirror(Elements elementUtils, Class<?> clazz){
        return elementUtils.getTypeElement(clazz.getCanonicalName()).asType();
    }

}
