package example.android.wenhui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for {@link AnalyticTest}. This is used to annotated the method that returns the {@link java.util.Map}
 * object for {@link AnalyticTest}, and the method must meet the constraints:
 *
 * 1. Public
 * 2. Static
 * 3. Has not parameters
 * 4. Must return {@link java.util.Map}
 *
 * Created by wyao on 3/8/16.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface AnalyticMap {
}
