package com.example.analytics;

import com.squareup.javapoet.ParameterSpec;
import org.hamcrest.Matcher;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * A factory to assist generate method code for hamcrest core matcher methods
 * Created by wyao on 3/8/16.
 */
public class CoreMatchersMethodFactory {

    private static final String EQUAL_TO = "equalTo";
    private static final String CONTAINS_STRING = "containsString";
    private static final String ANY_OF = "anyOf";
    private static final String IS = "is";

    private static final HashMap<String, CoreMatchersMethod> sMethodMap = new HashMap<>(5);

    static{
        sMethodMap.put(EQUAL_TO, new EqualTo());
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
        public Class objectType() {
            return Object.class;
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
        public String methodBlock() {
            return singleMethodBlock(EQUAL_TO, parameter);
        }

    }

    public static class ContainsString implements CoreMatchersMethod {

        private final String parameter = "str";

        @Override
        public Class objectType() {
            return String.class;
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
        public String methodBlock() {
            return singleMethodBlock(CONTAINS_STRING, parameter);
        }
    }

    /**
     * anyOf(org.hamcrest.Matcher<? super T>... matchers)
     */
    public static class AnyOf implements CoreMatchersMethod {

        private String parameter = "matchers";

        @Override
        public Class objectType() {
            return Object.class;
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
        public String methodBlock() {
            return singleMethodBlock(ANY_OF, parameter);
        }
    }

    public static class Is implements CoreMatchersMethod {

        private String parameter = "object";

        @Override
        public Class objectType() {
            return Object.class;
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
        public String methodBlock() {
            return singleMethodBlock(IS, parameter);
        }
    }

    private static ParameterSpec[] singleParameterSpec(Type type, String parameterName ){
        ParameterSpec.Builder builder = ParameterSpec.builder(type, parameterName);
        return new ParameterSpec[]{builder.build()};
    }

    private static String singleMethodBlock(String methodName, String parameter){
        return methodName + String.format("(%s)", parameter);
    }

}
