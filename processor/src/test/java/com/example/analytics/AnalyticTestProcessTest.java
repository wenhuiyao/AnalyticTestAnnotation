package com.example.analytics;

import com.google.testing.compile.JavaFileObjects;
import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticVar;
import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * Created by wyao on 3/9/16.
 */
public class AnalyticTestProcessTest {

    @Test
    public void testGenerateCode(){
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("EqualTo.java"))
                .processedWith(new AnalyticTestProcessor())
                .compilesWithoutError();
        // Bummer, I don't know where to put the generated file
//                .and().generatesSources(JavaFileObjects.forResource("GeneratedEqualTo.java"));
    }

    @Test
    public void testGenerateCodeWithoutError() {
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestNoError.java"))
                .processedWith(new AnalyticTestProcessor())
                .compilesWithoutError();
    }

    /**
     * Make sure Analytic variable is static
     */
    @Test
    public void testGenerateCode_varNotStatic()  {
        String errorMessage = String.format("The field @%s must be static field", AnalyticVar.class.getSimpleName());
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestVarNotStatic.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }

    /**
     * Make sure Analytic variable is final
     */
    @Test
    public void testGenerateCode_varNotFinal() {
        String errorMessage = String.format("Expect %s to be final, and non null", "VAR_ONE");
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestVarNotFinal.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }

    /**
     * Make sure Analytic variable is non null
     */
    @Test
    public void testGenerateCode_varIsNull() {
        String errorMessage = String.format("Expect %s to be final, and non null", "VAR_ONE");
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestVarIsNull.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }

    /**
     * Make sure Analytic getMap() method is public
     */
    @Test
    public void testGenerateCode_mapMethodNotPublic() {
        String errorMessage = String.format("The @%s method must be public", AnalyticMap.class.getSimpleName());
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestMapNoPublic.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }

    /**
     * Make sure Analytic getMap() method is static
     */
    @Test
    public void testGenerateCode_mapMethodNotStatic() {
        String errorMessage = String.format("The @%s method must be static", AnalyticMap.class.getSimpleName());
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestMapNotStatic.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }

    /**
     * Make sure Analytic getMap() method take no argument
     */
    @Test
    public void testGenerateCode_mapMethodTakeArgument() {
        String errorMessage = String.format("The @%s method must have no parameter", AnalyticMap.class.getSimpleName());
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestMapNotZeroParameter.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }

    /**
     * Make sure Analytic getMap() returns Map
     */
    @Test
    public void testGenerateCode_mapMethodNotReturnMap() {
        String errorMessage = String.format("The @%1s method's return type must be %2s, but get %3s",
                AnalyticMap.class.getSimpleName(), "java.util.Map", "java.lang.Integer");
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestMapNotReturnMap.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }

    /**
     * Make sure Analytic getMap() returns Map
     */
    @Test
    public void testGenerateCode_mapMethodReturnPrimitive() {
        String errorMessage = String.format("The @%1s method's return type must be %2s", AnalyticMap.class
                .getSimpleName(), "java.util.Map");
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestMapReturnPrimitive.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }

    /**
     * Make sure Analytic variable matcher is supported
     */
    @Test
    public void testGenerateCode_analyticVariableMatcherNotSupported() {
        String errorMessage = String.format("Please add %1s implementation to %2s",
                "notSupported",
                CoreMatchersMethodFactory.class.getCanonicalName());
        assert_().about(javaSource()).that(JavaFileObjects.forResource("AnalyticsTestVarNotSupportMatchers.java"))
                .processedWith(new AnalyticTestProcessor())
                .failsToCompile().withErrorContaining(errorMessage);
    }
}
