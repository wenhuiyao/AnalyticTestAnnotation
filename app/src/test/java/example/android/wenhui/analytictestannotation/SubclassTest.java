package example.android.wenhui.analytictestannotation;

import example.android.wenhui.analytictestannotation.helper.AnalyticTestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;


/**
 * Created by wyao on 3/8/16.
 */
public class SubclassTest {

    Subclass mSubclass;

    @Before
    public void setup() throws Exception {
        mSubclass = Subclass.getInstance();
    }

    @Test
    public void testMap(){
        AnalyticTestUtils.assertVarOneEqualTo("one");
        AnalyticTestUtils.assertVarOneNotNullValue();
        AnalyticTestUtils.assertVarTwoEqualTo(2);
        AnalyticTestUtils.assertVarThreeAnyOf(equalTo("three"), equalTo("four"));
        AnalyticTestUtils.assertVarFourIs(true);
    }

}
