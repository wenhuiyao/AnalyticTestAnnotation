package example.android.wenhui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to automatically generated a set of helper method for testing Analytic.
 *
 * Created by wyao on 3/8/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AnalyticTest {

    /**
     * The class that contains all the Analytic variables which is annotated {@link AnalyticVar}
     *
     * @return
     */
    Class varClass();


    /**
     * The name for the final generate class
     *
     * @return
     */
    String name() default "AnalyticTestUtils";
}
