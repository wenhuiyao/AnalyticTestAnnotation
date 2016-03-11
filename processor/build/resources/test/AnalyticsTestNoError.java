package resources;

import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyao on 3/9/16.
 */
@AnalyticTest(varClass = AnalyticsTestNoError.class, name = "TestUtils")
public class AnalyticsTestNoError {

    @AnalyticVar
    public static final String VAR_ONE = "var_one";

    @AnalyticVar(matchers = {"containsString"})
    public static final String VAR_TWO = "var_two";

    @AnalyticVar(matchers = {"containsString", "is"})
    public static final String VAR_THREE = "var_three";

    @AnalyticVar
    public static final String VAR_FOUR = "var_four";

    private int a;

    @AnalyticMap
    public static Map<String, Object> getMap(){
        return new HashMap<>();
    }
}
