package resources;

import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyao on 3/10/16.
 */
@AnalyticTest(varClass = AnalyticsTestMapNotStatic.class)
public class AnalyticsTestMapNotStatic {

    @AnalyticVar
    private final static String VAR_ONE = "var_one";

    @AnalyticMap
    public Map<String, Object> getMap(){
        return new HashMap<>();
    }
}
