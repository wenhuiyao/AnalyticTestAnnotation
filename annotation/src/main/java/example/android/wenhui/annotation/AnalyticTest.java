package example.android.wenhui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to automatically generated a set of helper method for testing.
 *
 * Created by wyao on 3/8/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AnalyticTest {

    Class varClass();

}
