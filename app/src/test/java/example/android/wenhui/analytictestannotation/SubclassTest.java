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

    Subclass mSubclass;

    @Before
    public void setup() throws Exception {
        mSubclass = Subclass.getInstance();
    }

    @Test
    public void testMap(){
        SubclassUtils.assertVarOneContainsString("one");
        SubclassUtils.assertVarTwoEqualTo(2);
        SubclassUtils.assertVarThreeAnyOf(equalTo("three"), equalTo("four"));
        SubclassUtils.assertVarFourIs(true);
    }



}
