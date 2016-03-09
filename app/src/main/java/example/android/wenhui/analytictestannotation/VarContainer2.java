package example.android.wenhui.analytictestannotation;

import example.android.wenhui.annotation.AnalyticVar;

/**
 * Created by wyao on 3/9/16.
 */
public class VarContainer2 {

    @AnalyticVar(matchers = {"containsString"})
    public final static String VAR_ONE = "VAR_FIRST";

    @AnalyticVar
    public final static String VAR_TWO = "VAR_SECOND";

    @AnalyticVar(matchers = {"anyOf"} )
    public final static String VAR_THREE = "VAR_THREE";

    @AnalyticVar(matchers = {"is", "anyOf"})
    final static String VAR_FOUR = "VAR_FOUR";
}
