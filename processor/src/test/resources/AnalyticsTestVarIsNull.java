package resources;

import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyao on 3/10/16.
 */
@AnalyticTest(varClass = AnalyticsTestVarIsNull.class)
public class AnalyticsTestVarIsNull {

    @AnalyticVar
    private final static String VAR_ONE = null;

    @AnalyticMap
    public static Map<String, Object> getMap(){
        return new HashMap<>();
    }
}
