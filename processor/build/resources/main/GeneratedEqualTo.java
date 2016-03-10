import java.lang.Object;

public class GeneratedEqualTo {

    public static void assertVarOneEqualTo(Object obj){
        java.util.Map<String, Object> map = EqualTo.getMap();
        org.hamcrest.MatcherAssert.assertThat(( Object )map.get("VAR_FIRST"), org.hamcrest.CoreMatchers.equalTo(obj));
    }

}
