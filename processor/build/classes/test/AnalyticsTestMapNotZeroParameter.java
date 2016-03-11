package resources;

import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyao on 3/10/16.
 */
@AnalyticTest(varClass = AnalyticsTestMapNotZeroParameter.class)
public class AnalyticsTestMapNotZeroParameter {

    @AnalyticVar
    private final static String VAR_ONE = "var_one";

    @AnalyticMap
    public static Map<String, Object> getMap(int one){
        return new HashMap<>();
    }
}
