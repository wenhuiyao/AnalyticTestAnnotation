package example.android.wenhui.analytictestannotation.helper;

import example.android.wenhui.annotation.AnalyticMap;
import example.android.wenhui.annotation.AnalyticTest;
import example.android.wenhui.library.VarContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyao on 3/9/16.
 */
@AnalyticTest(varClass = VarContainer.class)
public class SubclassTesting {

    private Map<String, Object> map = new HashMap<>();

    private static SubclassTesting sInstance;

    public static SubclassTesting getInstance(){
        if( sInstance == null ) {
            SubclassTesting instance = new SubclassTesting();
            instance.map.put(VarContainer.VAR_ONE, "one");
            instance.map.put(VarContainer.VAR_TWO, 2);
            instance.map.put("VAR_THREE", "three");
            instance.map.put("VAR_FOUR", true);

            sInstance = instance;
        }
        return sInstance;
    }

    @AnalyticMap
    public static Map<String, Object> getAnalyticMap(){
        return getInstance().map;
    }
}
