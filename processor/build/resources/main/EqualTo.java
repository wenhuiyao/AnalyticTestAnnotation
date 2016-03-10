import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.annotation.AnalyticVar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyao on 3/9/16.
 */
@AnalyticTest(varClass = EqualTo.class, name="TestUtils")
public class EqualTo {

    @AnalyticVar
    public static final String VAR_ONE = "var_one";

    @AnalyticMap
    public static Map<String, Object> getMap(){
        return new HashMap<>();
    }
}
