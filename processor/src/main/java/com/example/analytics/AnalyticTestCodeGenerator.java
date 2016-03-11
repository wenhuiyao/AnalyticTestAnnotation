package com.example.analytics;

import com.google.common.base.CaseFormat;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.hamcrest.MatcherAssert;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
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

    private final static String METHOD_ASSERT_THAT = "assertThat";
    private final static String OBJECT_MAP = "map";

    private final AnalyticTestClass analyticClass;
    private final AnalyticMapMethod analyticMapMethod;

    public AnalyticTestCodeGenerator(AnalyticTestClass analyticClass, AnalyticMapMethod mapMethod) {
        this.analyticClass = analyticClass;
        this.analyticMapMethod = mapMethod;
    }

    public void generateCode(List<AnalyticVarField> fields, Elements elementUtils,  Types typeUtils, Filer filer)
            throws IOException, ProcessingException {

        String finalClassName = analyticClass.getFinalClassName();
        if( finalClassName == null || finalClassName.length() == 0 ){
            finalClassName = analyticClass.getSimpleName() + CLASS_SUFFIX;
        }

        PackageElement pkg = elementUtils.getPackageOf(analyticClass.getTypeElement());
        String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(finalClassName).addModifiers(Modifier.PUBLIC);

        for (AnalyticVarField field : fields) {
            final Set<String> hamcrestMatchers = field.getHamcrestMatchers();
            for (String hamcrestMatcher : hamcrestMatchers) {
                typeSpec.addMethod(createMethod(elementUtils, typeUtils, hamcrestMatcher, field));
            }
        }

        // Write file
        JavaFile.builder(packageName == null ? "" : packageName, typeSpec.build()).indent("    ").build().writeTo(filer);

    }

    /**
     *
     * One example:
     * <code>
     *      public static void assertVarOneContainsString(String str) {
     *          Map map = SubclassTesting.getAnalyticMap();
     *          MatcherAssert.assertThat((String)map.get("VAR_FIRST"), CoreMatchers.containsString(str));
     *      }
     * </code>
     *
     */
    MethodSpec createMethod(Elements elementUtils, Types typeUtils, String hamcrestMatcher, AnalyticVarField field)
            throws ProcessingException {
        final CoreMatchersMethod coreMatchersMethod = CoreMatchersMethodFactory.newMethod(hamcrestMatcher);
        if (coreMatchersMethod == null) {
            throw new ProcessingException(analyticClass.getTypeElement(), "Please add %1s implementation to %2s",
                    hamcrestMatcher,
                    CoreMatchersMethodFactory.class.getCanonicalName());
        }

        String methodName = METHOD_PREFIX + upperToCamelCase(field.getFieldName()) + lowerToCamelCase
                (hamcrestMatcher);

        MethodSpec.Builder method = MethodSpec.methodBuilder(methodName).addModifiers(Modifier.PUBLIC, Modifier
                .STATIC).returns(TypeName.VOID);

        final ParameterSpec[] parameters = coreMatchersMethod.parameters();
        if( parameters != null ) {
            for (ParameterSpec parameter : parameters) {
                method.addParameter(parameter);
            }
        }

        if(coreMatchersMethod.isParameterVarargs()){
            method.varargs();
        }

        // Map map = TestAnalytic.getMap();
        method.addStatement( "$T $L = $L.$L()", Map.class, OBJECT_MAP, analyticClass.getSimpleName(),
                analyticMapMethod.getSimpleName());

        // (String)map.get("VAR_FIRST")
        CodeBlock expectBlock = CodeBlock.builder().add("($T)$L.get($S)", coreMatchersMethod.expectedObjectType(elementUtils, typeUtils,
                analyticMapMethod.getValueType()), OBJECT_MAP, field.getValue()).build();

        //  MatcherAssert.assertThat((String)map.get("VAR_FIRST"), CoreMatchers.containsString(str));
        method.addStatement("$T.$L($L, $L)", MatcherAssert.class, METHOD_ASSERT_THAT, expectBlock, coreMatchersMethod
                .methodBlock());

        return method.build();
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
