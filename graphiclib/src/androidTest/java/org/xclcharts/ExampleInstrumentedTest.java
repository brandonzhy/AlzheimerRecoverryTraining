package org.xclcharts;

import android.content.Context;

import org.junit.runner.RunWith;
import org.testng.annotations.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *

 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("org.xclcharts.test", appContext.getPackageName());
    }
}

