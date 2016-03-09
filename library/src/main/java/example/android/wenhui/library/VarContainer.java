package example.android.wenhui.library;


import example.android.wenhui.annotation.AnalyticVar;

/**
 * Created by wyao on 3/8/16.
 */
public class VarContainer {

    @AnalyticVar(matchers = {"containsString"})
    public final static String VAR_ONE = "VAR_FIRST";

    @AnalyticVar
    public final static String VAR_TWO = "VAR_TWO";

    @AnalyticVar(matchers = {"anyOf"} )
    public final static String VAR_THREE = "VAR_THREE";

    @AnalyticVar(matchers = {"is", "anyOf"})
    final static String VAR_FOUR = "VAR_FOUR";

    public final static String VAR_NOT = "VAR_NOT";

    private String notVar;

}
