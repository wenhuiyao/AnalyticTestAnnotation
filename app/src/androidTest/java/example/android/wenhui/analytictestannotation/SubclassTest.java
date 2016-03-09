package example.android.wenhui.analytictestannotation;

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

    @Before
    public void setup() throws Exception {
    }

    @Test
    public void testMap(){
        SubclassTestingUtils.assertVarOneEqualTo("one");
        SubclassTestingUtils.assertVarOneNotNullValue();
        SubclassTestingUtils.assertVarTwoEqualTo(2);
        SubclassTestingUtils.assertVarThreeAnyOf(equalTo("three"), equalTo("four"));
        SubclassTestingUtils.assertVarFourIs(true);
    }

}
