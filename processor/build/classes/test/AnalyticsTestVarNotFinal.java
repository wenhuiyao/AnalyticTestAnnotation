package resources;

import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyao on 3/10/16.
 */
@AnalyticTest(varClass = AnalyticsTestVarNotFinal.class)
public class AnalyticsTestVarNotFinal {

    @AnalyticVar
    public static String VAR_ONE = "var_one";

    @AnalyticMap
    public static Map<String, Object> getMap(){
        return new HashMap<>();
    }

}
