package com.example.analytics;

import com.example.analytics.mock.MockElements;
import com.example.analytics.mock.MockTypeMirror;
import com.example.analytics.mock.MockTypes;
import com.squareup.javapoet.ParameterSpec;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.example.analytics.CoreMatchersMethodFactory.newMethod;
import static example.android.wenhui.annotation.AnalyticMatchers.ANY_OF;
import static example.android.wenhui.annotation.AnalyticMatchers.CONTAINS_STRING;
import static example.android.wenhui.annotation.AnalyticMatchers.EQUAL_TO;
import static example.android.wenhui.annotation.AnalyticMatchers.IS;
import static example.android.wenhui.annotation.AnalyticMatchers.NOT_NULL_VALUE;
import static example.android.wenhui.annotation.AnalyticMatchers.NULL_VALUE;
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

    private Elements elementUtils;
    private Types typeUtils;

    private TypeMirror mapValueType;

    @Before
    public void setup() throws Exception {
        elementUtils = new MockElements();
        typeUtils = new MockTypes();
        mapValueType = new MockTypeMirror(Object.class);
    }

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
    public void testEqualToMethod() throws ProcessingException {

        final CoreMatchersMethod coreMatchersMethod = newMethod(EQUAL_TO);

        assertThat(coreMatchersMethod.expectedObjectType(elementUtils, typeUtils, mapValueType), isA(Object.class));
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
    public void testContainsStringMethod() throws ProcessingException {
        final CoreMatchersMethod coreMatchersMethod = newMethod(CONTAINS_STRING);

        assertThat(coreMatchersMethod.expectedObjectType(elementUtils, typeUtils, mapValueType).toString(), equalTo(String
                .class.getCanonicalName()));
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
    public void testAnyOfMethod() throws ProcessingException {
        final CoreMatchersMethod coreMatchersMethod = newMethod(ANY_OF);

        assertThat(coreMatchersMethod.expectedObjectType(elementUtils, typeUtils, mapValueType).toString(), equalTo(Object
                .class.getCanonicalName
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
    public void testIsMethod() throws ProcessingException {
        final CoreMatchersMethod coreMatchersMethod = newMethod(IS);

        assertThat(coreMatchersMethod.expectedObjectType(elementUtils, typeUtils, mapValueType).toString(), equalTo(Object
                .class.getCanonicalName
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
    public void testNotNullValueMethod() throws ProcessingException {
        final CoreMatchersMethod coreMatchersMethod = newMethod(NOT_NULL_VALUE);

        assertThat(coreMatchersMethod.expectedObjectType(elementUtils, typeUtils, mapValueType).toString(), equalTo(Object
                .class.getCanonicalName
                ()));
        assertThat(coreMatchersMethod.isParameterVarargs(), is(false));

        assertThat(coreMatchersMethod.parameters(), nullValue());

        String methodBlack = coreMatchersMethod.methodBlock().toString();
        String expected = CoreMatchers.class.getCanonicalName() + ".notNullValue()";
        assertThat(methodBlack, equalTo(expected));
    }

    @Test
    public void testNullValueMethod() throws ProcessingException {
        final CoreMatchersMethod coreMatchersMethod = newMethod(NULL_VALUE);

        assertThat(coreMatchersMethod.expectedObjectType(elementUtils, typeUtils, mapValueType).toString(), equalTo
                (Object.class.getCanonicalName()));
        assertThat(coreMatchersMethod.isParameterVarargs(), is(false));

        assertThat(coreMatchersMethod.parameters(), nullValue());

        String methodBlack = coreMatchersMethod.methodBlock().toString();
        String expected = CoreMatchers.class.getCanonicalName() + ".nullValue()";
        assertThat(methodBlack, equalTo(expected));
    }

}
