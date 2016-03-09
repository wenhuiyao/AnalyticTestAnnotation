package example.android.wenhui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 *
 * Created by wyao on 3/8/16.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface AnalyticVar {

    /**
     * The string to matcher use by hamcrest matchers
     * http://hamcrest.org/JavaHamcrest/javadoc/1.3/
     *
     * @return
     */
    String[] matchers() default {"equalTo"};

}
