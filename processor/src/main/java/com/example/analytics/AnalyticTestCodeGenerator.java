package com.example.analytics;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * As the name suggests, it generates code for the {@link AnalyticTestProcessor}
 * <p/>
 * Created by wyao on 3/8/16.
 */
public class AnalyticTestCodeGenerator {

    private static final String CLASS_SUFFIX = "Utils";
    private static final String METHOD_PREFIX = "assert";

    private final static String CLASS_CORE_MATCHERS = "org.hamcrest.CoreMatchers.";
    private final static String METHOD_ASSERT_THAT = "org.hamcrest.MatcherAssert.assertThat";

    private final static String MAP_NAME = Map.class.getCanonicalName();
    private final static String STRING_NAME = String.class.getSimpleName();
    private final static String OBJECT_NAME = Object.class.getSimpleName();

    private final AnalyticTestClass analyticClass;
    private final AnalyticMapMethod mAnalyticMapMethod;
    private final String MAP_OBJECT = "map";

    public AnalyticTestCodeGenerator(AnalyticTestClass analyticClass, AnalyticMapMethod mapField) {
        this.analyticClass = analyticClass;
        this.mAnalyticMapMethod = mapField;
    }

    public void generateCode(List<AnalyticVarField> fields, Elements elementUtils,  Filer filer)
            throws IOException, ProcessingException {

        String finalClassName = analyticClass.getFinalClassName();
        if( finalClassName == null || finalClassName.length() == 0 ){
            finalClassName = analyticClass.getSimpleName() + CLASS_SUFFIX;
        }

        PackageElement pkg = elementUtils.getPackageOf(analyticClass.getTypeElement());
        String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(finalClassName).addModifiers(Modifier.PUBLIC);

        for (AnalyticVarField field : fields) {
            createMethod(field, typeSpec);
        }

        // Write file
        JavaFile.builder(packageName, typeSpec.build()).indent("    ").build().writeTo(filer);

    }

    /**
     *
     * One example:
     * <code>
     *  public static void assertVarOneContainsString(String str) {
     *      java.util.Map<String, Object> map = example.android.wenhui.analytictestannotation.Subclass.getAnalyticMap();
     *      org.hamcrest.MatcherAssert.assertThat(( String )map.get("VAR_FIRST"), org.hamcrest.CoreMatchers.containsString
     *      (str));
     * }
     * </code>
     *
     */
    private void createMethod(AnalyticVarField field, TypeSpec.Builder typeSpec) throws ProcessingException {
        final Set<String> hamcrestMatchers = field.getHamcrestMatchers();
        for (String hamcrestMatcher : hamcrestMatchers) {
            final CoreMatchersMethod coreMatchersMethod = CoreMatchersMethodFactory.newMethod(hamcrestMatcher);
            if (coreMatchersMethod == null) {
                throw new ProcessingException(analyticClass.getTypeElement(), "Please add %1s implementation to %2s",
                        hamcrestMatcher,
                        CoreMatchersMethodFactory.class.getCanonicalName());
            }

            String methodName = METHOD_PREFIX + upperToCamelCase(field.getFieldName()) + lowerToCamelCase
                    (hamcrestMatcher);

            MethodSpec.Builder method = MethodSpec.methodBuilder(methodName).addModifiers(Modifier.PUBLIC, Modifier.STATIC);
            final ParameterSpec[] parameters = coreMatchersMethod.parameters();
            if( parameters != null ) {
                for (ParameterSpec parameter : parameters) {
                    method.addParameter(parameter);
                }
            }

            if(coreMatchersMethod.isParameterVarargs()){
                method.varargs();
            }

            method.returns(TypeName.VOID);

            method.addStatement(createAssignMapObjectStatement());

            String expectedValue = String.format("( %1s )%2s.get(\"%3s\")", coreMatchersMethod.objectType().getSimpleName(),
                    MAP_OBJECT, field.getValue());
            String matcher = CLASS_CORE_MATCHERS + coreMatchersMethod.methodBlock();
            String assertStatement = METHOD_ASSERT_THAT + String.format("(%1s, %2s)", expectedValue, matcher);

            method.addStatement(assertStatement);
            typeSpec.addMethod(method.build());
        }

    }

    /**
     * Map map = $s.{@link example.android.wenhui.annotation.AnalyticMap};
     * for instsance "Map<String, Object> map = TestAnalytic.getMap();"
     *
     * @return
     */
    private String createAssignMapObjectStatement() {
        final String methodName = analyticClass.getSimpleName() + "." + mAnalyticMapMethod.getSimpleName() + "()";
        return MAP_NAME + "<" + STRING_NAME + ", " + OBJECT_NAME + "> " + MAP_OBJECT + " = " + methodName;

    }

    /**
     * "VAR_NAME" => "VarName"
     */
    private String upperToCamelCase(String name) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name);
    }

    /**
     * "equalTo" => "EqualTo"
     */
    private String lowerToCamelCase(String name) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
    }

}
