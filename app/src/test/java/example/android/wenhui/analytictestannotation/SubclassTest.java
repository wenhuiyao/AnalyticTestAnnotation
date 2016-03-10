package example.android.wenhui.analytictestannotation;

import example.android.wenhui.analytictestannotation.helper.AnalyticTestUtils;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.equalTo;


/**
 * Created by wyao on 3/8/16.
 */
@RunWith(JUnit4.class)
public class SubclassTest extends TestCase {

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
