package example.android.wenhui.library;


import example.android.wenhui.annotation.AnalyticVar;

import static example.android.wenhui.annotation.AnalyticMatchers.ANY_OF;
import static example.android.wenhui.annotation.AnalyticMatchers.CONTAINS_STRING;
import static example.android.wenhui.annotation.AnalyticMatchers.IS;

/**
 * Created by wyao on 3/8/16.
 */
public class VarContainer {

    @AnalyticVar(matchers = {CONTAINS_STRING})
    public final static String VAR_ONE = "VAR_FIRST";

    @AnalyticVar
    public final static String VAR_TWO = "VAR_TWO";

    @AnalyticVar(matchers = {ANY_OF} )
    private final static String VAR_THREE = "VAR_THREE";

    @AnalyticVar(matchers = {IS, ANY_OF})
    final static String VAR_FOUR = "VAR_FOUR";

    public final static String VAR_NOT = "VAR_NOT";

    private String notVar;

}
