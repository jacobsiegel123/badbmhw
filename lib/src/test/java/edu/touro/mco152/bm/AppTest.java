package edu.touro.mco152.bm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    /**
     * test for targetMarkSizeKb
     */
    @Test
    void targetMarkSizeKb() {
        long n = App.targetMarkSizeKb();
        assertEquals(App.blockSizeKb * App.numOfBlocks, n);
    }

    /**
     * did the c of bicep cross checked to make sure the method works by doing addition
     */
    @Test
    void crossChecktargetMarkSizeKb(){
        int total = 0;
        for (int i = 0; i < App.blockSizeKb; i++) {
            total += App.numOfBlocks;
        }

        assertEquals(total, App.targetMarkSizeKb());
    }
    /**
     * did the p for right bicep the test ran in correct amount of time
     * also t for time for correct
     */
    @Test
    void targetTxSizeKb() {
        long start = System.currentTimeMillis();
        long n = App.targetTxSizeKb();
        long end = System.currentTimeMillis();
        assertEquals(true,end - start < 15);
    }
}