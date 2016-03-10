package example.android.wenhui.annotation;

/**
 * Created by wyao on 3/10/16.
 */
public interface AnalyticMatchers {

    String EQUAL_TO = "equalTo";

    String NOT_NULL_VALUE = "notNullValue";

    String NULL_VALUE = "nullValue";

    String CONTAINS_STRING = "containsString";

    /**
     * Creates a matcher that matches if the examined object matches ANY of the specified matchers.
     * For example:
     *
     * assertThat("myValue", anyOf(startsWith("foo"), containsString("Val")))
     */
    String ANY_OF = "anyOf";

    /**
     * A shortcut to the frequently used is(equalTo(x))
     */
    String IS = "is";

}
