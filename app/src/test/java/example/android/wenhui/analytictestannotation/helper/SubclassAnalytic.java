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
public class SubclassAnalytic {

    private Map<String, Object> map = new HashMap<>();

    private static SubclassAnalytic sInstance;

    public static SubclassAnalytic getInstance(){
        if( sInstance == null ) {
            SubclassAnalytic instance = new SubclassAnalytic();
            instance.map.put(VarContainer.VAR_ONE, "4");
            instance.map.put(VarContainer.VAR_TWO, "two");
            instance.map.put("VAR_THREE", "three");
            instance.map.put("VAR_FOUR", "fourrrr");

            sInstance = instance;
        }
        return sInstance;
    }

    @AnalyticMap
    public static Map<String, Object> getAnalyticMap(){
        return getInstance().map;
    }
}
