package example.android.wenhui.analytictestannotation;


import example.android.wenhui.library.SuperClass;
import example.android.wenhui.library.VarContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyao on 3/8/16.
 */
public class Subclass extends SuperClass {

    private Map<String, Object> map = new HashMap<>();

    private static Subclass sInstance;

    public static Subclass getInstance(){
        if( sInstance == null ) {
            Subclass instance = new Subclass();
            instance.map.put(VarContainer.VAR_ONE, "one");
            instance.map.put(VarContainer.VAR_TWO, 2);
            instance.map.put("VAR_THREE", "three");
            instance.map.put("VAR_FOUR", true);

            sInstance = instance;
        }
        return sInstance;
    }

    public static Map<String, Object> getAnalyticMap(){
        return getInstance().map;
    }

    public void testUsage(){
//        SubclassUtils.assertVarOneEqualTo("one");
    }

}
