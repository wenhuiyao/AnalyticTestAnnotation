import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

/**
 * Created by wyao on 3/10/16.
 */
@AnalyticTest(varClass = AnalyticsTestMapReturnPrimitive.class)
public class AnalyticsTestMapReturnPrimitive {

    @AnalyticVar
    private final static String VAR_ONE = "var_one";

    @AnalyticMap
    public static boolean getMap(){
        return true;
    }
}
