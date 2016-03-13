package com.example.analytics;

import com.example.analytics.mock.MockElements;
import com.example.analytics.mock.MockExecutableElement;
import com.example.analytics.mock.MockName;
import com.example.analytics.mock.MockTypeElement;
import com.example.analytics.mock.MockTypeMirror;
import com.example.analytics.mock.MockTypes;
import com.example.analytics.mock.MockVariableElement;
import com.squareup.javapoet.MethodSpec;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static example.android.wenhui.annotation.AnalyticMatchers.ANY_OF;
import static example.android.wenhui.annotation.AnalyticMatchers.CONTAINS_STRING;
import static example.android.wenhui.annotation.AnalyticMatchers.EQUAL_TO;
import static example.android.wenhui.annotation.AnalyticMatchers.IS;
import static example.android.wenhui.annotation.AnalyticMatchers.NOT_NULL_VALUE;
import static example.android.wenhui.annotation.AnalyticMatchers.NULL_VALUE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Created by wyao on 3/10/16.
 */
public class AnalyticTestCodeGeneratorTest {

    private static final String SIMPLE_METHOD_NAME = "getMap";

    private static final String VARIABLE = "VAR_ONE";
    private static final String VARIABLE_UPPER_LOWER = "VarOne";
    private static final String VARIABLE_VALUE = "var_one";

    private Elements elementUtils;
    private Types typeUtils;
    private Class analyticTestClass;

    @Before
    public void setup() throws Exception {
        elementUtils = new MockElements();
        typeUtils = new MockTypes();
        analyticTestClass = AnalyticTestCodeGeneratorTest.class;
    }

    @Test
    public void testGenerateMethod_equalTo() throws Exception {
        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockVariableElement(new String[] {EQUAL_TO}));
        final MethodSpec method = generator.createMethod(elementUtils, typeUtils, EQUAL_TO, field);

        String got = method.toString();
        String expected = getStandardMethodString("EqualTo", Object.class, "obj", Object.class, EQUAL_TO);
        assertThat(got, equalTo(expected));
    }

    @Test
    public void testGenerateMethod_containsString() throws Exception {

        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockVariableElement(new String[] {CONTAINS_STRING}));
        final MethodSpec method = generator.createMethod(elementUtils, typeUtils, CONTAINS_STRING, field);

        String got = method.toString();
        String expected = getStandardMethodString("ContainsString", String.class, "str", String.class, CONTAINS_STRING);
        assertThat(got, equalTo(expected));
    }

    @Test
    public void testGenerateMethod_anyOf() throws Exception {
        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockVariableElement(new String[] {ANY_OF}));
        final MethodSpec method = generator.createMethod(elementUtils, typeUtils, ANY_OF, field);

        String got = method.toString();
        String expected = getStandardMethodString("AnyOf", Matcher[].class, "matchers", Object.class, ANY_OF);
        assertThat(got, equalTo(expected));
    }


    @Test
    public void testGenerateMethod_is() throws Exception {

        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockVariableElement(new String[] {IS}));
        final MethodSpec method = generator.createMethod(elementUtils, typeUtils, IS, field);

        String got = method.toString();
        String expected = getStandardMethodString("Is", Object.class, "object", Object.class, IS);
        assertThat(got, equalTo(expected));
    }

    @Test
    public void testGenerateMethod_notNullValue() throws Exception {

        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockVariableElement(new String[] {NOT_NULL_VALUE}));
        final MethodSpec method = generator.createMethod(elementUtils, typeUtils, NOT_NULL_VALUE, field);

        String got = method.toString();
        String expected = getStandardMethodString("NotNullValue", null, "", Object.class, NOT_NULL_VALUE);
        assertThat(got, equalTo(expected));
    }


    @Test
    public void testGenerateMethod_nullValue() throws Exception {

        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockVariableElement(new String[] {NULL_VALUE}));
        final MethodSpec method = generator.createMethod(elementUtils, typeUtils, NULL_VALUE, field);

        String got = method.toString();
        String expected = getStandardMethodString("NullValue", null, "", Object.class, NULL_VALUE);
        assertThat(got, equalTo(expected));
    }


    /**
     *
     * @param methodNameUpLow  The method name started with upper case
     * @param objClass  The parameter type class
     * @param objName   The parameter name
     * @param covertTypeClass The type the value of the map will be converted to
     * @param methodName The method name
     * @return
     */
    private String getStandardMethodString( String methodNameUpLow, Class objClass, String objName, Class
            covertTypeClass, String methodName){
        String EXPECTED_METHOD_FORMAT = "public static void assert%s%s(%s%s) {\n"
                        + "  java.util.Map map = %s.%s();\n"
                        + "  org.hamcrest.MatcherAssert.assertThat(%smap.get(\"%s\"), org.hamcrest.CoreMatchers.%s(%s));\n"
                        + "}\n";

        String parameterType ;

        if( objClass != null ) {
            parameterType = objClass.isArray() ? objClass.getCanonicalName().replace("[]", "") + "..." : objClass
                    .getCanonicalName();
            parameterType += " ";
        } else {
            parameterType = "";
        }

        String convertBlock = "";
        if( covertTypeClass != Object.class ){
            convertBlock = "(" + covertTypeClass.getCanonicalName() + ")";
        }
        return String.format(EXPECTED_METHOD_FORMAT, VARIABLE_UPPER_LOWER, methodNameUpLow, parameterType,
                objName, analyticTestClass.getSimpleName(), SIMPLE_METHOD_NAME, convertBlock,
                VARIABLE_VALUE, methodName, objName);
    }

    private AnalyticTestCodeGenerator newCodeGenerator() throws ProcessingException{
        MockTypeElement typeElement = newMockTypeElement();
        MockExecutableElement executableElement = newMockExecutableElement();

        AnalyticTestClass testClass = new AnalyticTestClass(typeElement, elementUtils);
        AnalyticMapMethod mapMethod = new AnalyticMapMethod(executableElement, typeUtils, elementUtils);

        return new AnalyticTestCodeGenerator(testClass, mapMethod);
    }

    private MockTypeElement newMockTypeElement(){
        MockTypeElement typeElement = new MockTypeElement(analyticTestClass);

        AnalyticTest spyAnno = mock(AnalyticTest.class);
        when(spyAnno.varClass()).thenReturn(String.class);
        when(spyAnno.name()).thenReturn("TestUtils");

        typeElement.setAnnotation(spyAnno);

        return typeElement;
    }

    private MockExecutableElement newMockExecutableElement(){
        MockExecutableElement executableElement = new MockExecutableElement();
        executableElement.setSimpleName(new MockName(SIMPLE_METHOD_NAME));

        Set<Modifier> modifiers = new HashSet<>();
        modifiers.add(Modifier.PUBLIC);
        modifiers.add(Modifier.STATIC);
        executableElement.setModifier(modifiers);

        final MockTypeMirror mockTypeMirror = new MockTypeMirror(Map.class);

        List<MockTypeMirror> typeArguments = new ArrayList<>(2);
        typeArguments.add(new MockTypeMirror(String.class));
        typeArguments.add(new MockTypeMirror(Object.class));

        mockTypeMirror.setTypeArguments(typeArguments);

        executableElement.setReturnType(mockTypeMirror);

        return executableElement;
    }

    private MockVariableElement newMockVariableElement(String[] matchers){
        MockVariableElement variableElement = new MockVariableElement();

        AnalyticVar spyAnno = mock(AnalyticVar.class);
        when(spyAnno.matchers()).thenReturn(matchers);
        variableElement.setAnnotation(spyAnno);

        variableElement.setSimpleName(new MockName(VARIABLE));
        variableElement.setConstantValue(VARIABLE_VALUE);

        Set<Modifier> modifiers = new HashSet<>();
        modifiers.add(Modifier.PUBLIC);
        modifiers.add(Modifier.STATIC);

        variableElement.setModifiers(modifiers);

        return variableElement;
    }

}
