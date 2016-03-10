package com.example.analytics;

import com.example.analytics.mock.MockExecutableElement;
import com.example.analytics.mock.MockName;
import com.example.analytics.mock.MockTypeElement;
import com.example.analytics.mock.MockVariableElement;
import com.squareup.javapoet.MethodSpec;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;
import org.hamcrest.Matcher;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.util.HashSet;
import java.util.Set;

import static com.example.analytics.CoreMatchersMethodFactory.*;
import static com.example.analytics.CoreMatchersMethodFactory.CONTAINS_STRING;
import static com.example.analytics.CoreMatchersMethodFactory.EQUAL_TO;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * Created by wyao on 3/10/16.
 */
public class AnalyticTestCodeGeneratorTest {

    private static final String SIMPLE_CLASS_NAME = "AnalyticTest";
    private static final String QUALIFIED_NAME = "com.example.test.AnalyticTest";

    private static final String SIMPLE_METHOD_NAME = "getMap";

    private static final String VARIABLE = "VAR_ONE";
    private static final String VARIABLE_UPPER_LOWER = "VarOne";
    private static final String VARIABLE_VALUE = "var_one";

    @Test
    public void testGenerateMethod_equalTo() throws Exception {
        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockFieldAdapter(new String[] {EQUAL_TO}));
        final MethodSpec method = generator.createMethod(EQUAL_TO, field);

        String got = method.toString();
        String expected = getStandardMethodString("EqualTo", Object.class, "obj", Object.class, EQUAL_TO);
        assertThat(got, equalTo(expected));
    }

    @Test
    public void testGenerateMethod_containsString() throws Exception {

        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockFieldAdapter(new String[] {CONTAINS_STRING}));
        final MethodSpec method = generator.createMethod(CONTAINS_STRING, field);

        String got = method.toString();
        String expected = getStandardMethodString("ContainsString", String.class, "str", String.class, CONTAINS_STRING);
        assertThat(got, equalTo(expected));
    }

    @Test
    public void testGenerateMethod_anyOf() throws Exception {
        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockFieldAdapter(new String[] {ANY_OF}));
        final MethodSpec method = generator.createMethod(ANY_OF, field);

        String got = method.toString();
        String expected = getStandardMethodString("AnyOf", Matcher[].class, "matchers", Object.class, ANY_OF);
        assertThat(got, equalTo(expected));
    }


    @Test
    public void testGenerateMethod_is() throws Exception {

        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockFieldAdapter(new String[] {IS}));
        final MethodSpec method = generator.createMethod(IS, field);

        String got = method.toString();
        String expected = getStandardMethodString("Is", Object.class, "object", Object.class, IS);
        assertThat(got, equalTo(expected));
    }

    @Test
    public void testGenerateMethod_notNullValue() throws Exception {

        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockFieldAdapter(new String[] {NOT_NULL_VALUE}));
        final MethodSpec method = generator.createMethod(NOT_NULL_VALUE, field);

        String got = method.toString();
        String expected = getStandardMethodString("NotNullValue", null, "", Object.class, NOT_NULL_VALUE);
        assertThat(got, equalTo(expected));
    }


    @Test
    public void testGenerateMethod_nullValue() throws Exception {

        AnalyticTestCodeGenerator generator = newCodeGenerator();

        AnalyticVarField field = new AnalyticVarField(newMockFieldAdapter(new String[] {NULL_VALUE}));
        final MethodSpec method = generator.createMethod(NULL_VALUE, field);

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
                        + "  org.hamcrest.MatcherAssert.assertThat((%s)map.get(\"%s\"), org.hamcrest.CoreMatchers.%s(%s));\n"
                        + "}\n";

        String parameterType ;

        if( objClass != null ) {
            parameterType = objClass.isArray() ? objClass.getCanonicalName().replace("[]", "") + "..." : objClass
                    .getCanonicalName();
            parameterType += " ";
        } else {
            parameterType = "";
        }

        return String.format(EXPECTED_METHOD_FORMAT, VARIABLE_UPPER_LOWER, methodNameUpLow, parameterType,
                objName, SIMPLE_CLASS_NAME, SIMPLE_METHOD_NAME, covertTypeClass.getCanonicalName(), VARIABLE_VALUE, methodName,
                objName);
    }

    private AnalyticTestCodeGenerator newCodeGenerator(){
        MockTypeElement typeElement = newMockTypeElement();
        MockExecutableElement executableElement = newMockExecutableElement();

        AnalyticTestClass testClass = new AnalyticTestClass(typeElement);
        AnalyticMapMethod mapMethod = new AnalyticMapMethod(executableElement);

        return new AnalyticTestCodeGenerator(testClass, mapMethod);
    }

    private MockTypeElement newMockTypeElement(){
        MockTypeElement typeElement = new MockTypeElement();

        AnalyticTest spyAnno = mock(AnalyticTest.class);
        when(spyAnno.varClass()).thenReturn(AnalyticTestCodeGeneratorTest.class);
        when(spyAnno.name()).thenReturn("TestUtils");

        typeElement.setAnnotation(spyAnno);
        typeElement.setSimpleName(new MockName(SIMPLE_CLASS_NAME));
        typeElement.setQualifiedName(new MockName(QUALIFIED_NAME));

        return typeElement;
    }

    private MockExecutableElement newMockExecutableElement(){
        MockExecutableElement executableElement = new MockExecutableElement();
        executableElement.setSimpleName(new MockName(SIMPLE_METHOD_NAME));

        return executableElement;
    }

    private AnalyticFieldAdapter newMockFieldAdapter(String[] matchers){
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

        return new AnalyticFieldAdapter(variableElement);
    }
}
