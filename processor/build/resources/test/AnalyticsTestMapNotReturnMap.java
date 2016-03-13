package resources;

import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

/**
 * Created by wyao on 3/10/16.
 */
@AnalyticTest(varClass = AnalyticsTestMapNotReturnMap.class)
public class AnalyticsTestMapNotReturnMap {

    @AnalyticVar
    private final static String VAR_ONE = "var_one";

    @AnalyticMap
    public static Integer getMap(){
        return 1;
    }
}
