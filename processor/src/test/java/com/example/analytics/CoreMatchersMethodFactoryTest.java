package com.example.analytics;

import com.squareup.javapoet.ParameterSpec;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static example.android.wenhui.annotation.AnalyticMatchers.*;

import static com.example.analytics.CoreMatchersMethodFactory.newMethod;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by wyao on 3/10/16.
 */
public class CoreMatchersMethodFactoryTest {

    @Test
    public void testSupportedMethods() {
        assertThat(newMethod(EQUAL_TO), notNullValue());
        assertThat(newMethod(CONTAINS_STRING), notNullValue());
        assertThat(newMethod(ANY_OF), notNullValue());
        assertThat(newMethod(IS), notNullValue());
        assertThat(newMethod(NOT_NULL_VALUE), notNullValue());
        assertThat(newMethod(NULL_VALUE), notNullValue());
    }


    @Test
    public void testEqualToMethod() {

        final CoreMatchersMethod coreMatchersMethod = newMethod(EQUAL_TO);

        assertThat(coreMatchersMethod.expectedObjectType(), isA(Object.class));
        assertThat(coreMatchersMethod.isParameterVarargs(), is(false));

        final ParameterSpec[] parameters = coreMatchersMethod.parameters();
        assertThat(parameters.length, equalTo(1));

        String parameterString = parameters[0].toString();
        String expected = "java.lang.Object obj";
        assertThat(parameterString, equalTo(expected));

        String methodBlack = coreMatchersMethod.methodBlock().toString();
        expected = CoreMatchers.class.getCanonicalName() + ".equalTo(obj)";
        assertThat(methodBlack, equalTo(expected));
    }

    @Test
    public void testContainsStringMethod(){
        final CoreMatchersMethod coreMatchersMethod = newMethod(CONTAINS_STRING);

        assertThat(coreMatchersMethod.expectedObjectType().getCanonicalName(), equalTo(String.class.getCanonicalName()));
        assertThat(coreMatchersMethod.isParameterVarargs(), is(false));

        final ParameterSpec[] parameters = coreMatchersMethod.parameters();
        assertThat(parameters.length, equalTo(1));

        String parameterString = parameters[0].toString();
        String expected = "java.lang.String str";
        assertThat(parameterString, equalTo(expected));

        String methodBlack = coreMatchersMethod.methodBlock().toString();
        expected = CoreMatchers.class.getCanonicalName() + ".containsString(str)";
        assertThat(methodBlack, equalTo(expected));
    }

    @Test
    public void testAnyOfMethod(){
        final CoreMatchersMethod coreMatchersMethod = newMethod(ANY_OF);

        assertThat(coreMatchersMethod.expectedObjectType().getCanonicalName(), equalTo(Object.class.getCanonicalName
                ()));
        assertThat(coreMatchersMethod.isParameterVarargs(), is(true));

        final ParameterSpec[] parameters = coreMatchersMethod.parameters();
        assertThat(parameters.length, equalTo(1));

        String parameterString = parameters[0].toString();
        String expected = "org.hamcrest.Matcher[] matchers";
        assertThat(parameterString, equalTo(expected));

        String methodBlack = coreMatchersMethod.methodBlock().toString();
        expected = CoreMatchers.class.getCanonicalName() +".anyOf(matchers)";
        assertThat(methodBlack, equalTo(expected));
    }

    @Test
    public void testIsMethod(){
        final CoreMatchersMethod coreMatchersMethod = newMethod(IS);

        assertThat(coreMatchersMethod.expectedObjectType().getCanonicalName(), equalTo(Object.class.getCanonicalName
                ()));
        assertThat(coreMatchersMethod.isParameterVarargs(), is(false));

        final ParameterSpec[] parameters = coreMatchersMethod.parameters();
        assertThat(parameters.length, equalTo(1));

        String parameterString = parameters[0].toString();
        String expected = "java.lang.Object object";
        assertThat(parameterString, equalTo(expected));

        String methodBlack = coreMatchersMethod.methodBlock().toString();
        expected = CoreMatchers.class.getCanonicalName() + ".is(object)";
        assertThat(methodBlack, equalTo(expected));
    }

    @Test
    public void testNotNullValueMethod(){
        final CoreMatchersMethod coreMatchersMethod = newMethod(NOT_NULL_VALUE);

        assertThat(coreMatchersMethod.expectedObjectType().getCanonicalName(), equalTo(Object.class.getCanonicalName
                ()));
        assertThat(coreMatchersMethod.isParameterVarargs(), is(false));

        assertThat(coreMatchersMethod.parameters(), nullValue());

        String methodBlack = coreMatchersMethod.methodBlock().toString();
        String expected = CoreMatchers.class.getCanonicalName() + ".notNullValue()";
        assertThat(methodBlack, equalTo(expected));
    }

    @Test
    public void testNullValueMethod(){
        final CoreMatchersMethod coreMatchersMethod = newMethod(NULL_VALUE);

        assertThat(coreMatchersMethod.expectedObjectType().getCanonicalName(), equalTo(Object.class.getCanonicalName
                ()));
        assertThat(coreMatchersMethod.isParameterVarargs(), is(false));

        assertThat(coreMatchersMethod.parameters(), nullValue());

        String methodBlack = coreMatchersMethod.methodBlock().toString();
        String expected = CoreMatchers.class.getCanonicalName() + ".nullValue()";
        assertThat(methodBlack, equalTo(expected));
    }

}
