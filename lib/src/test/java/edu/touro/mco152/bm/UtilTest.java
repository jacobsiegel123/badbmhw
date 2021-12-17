package edu.touro.mco152.bm;

import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    /**
     * testing boundary's
     *
     */
    @Test
    void randInt() {
        int min = 20;
        int max = 50;
        for (int i = 0; i < 50; i++) {
            assertEquals(true,min <= Util.randInt(min,max)  && max >= Util.randInt(min,max));
        }
    }

    /**
     * testing upper boundry to make sure got the correct results
     * testing b for bounds r for range for correct
     */
    @Test
    void randIntUpper(){
        int min = 25;
        int max = 50;
        for (int i = 0; i < 50; i++) {
            assertEquals(true,max >= Util.randInt(min,max));
        }
    }

    /**
     * testing for o orderin in correct
     * the number should not be in the correct order
     */
    @Test
    void randIntOrder(){
        int min = 25;
        int max = 50;
        int [] a = new int[70];
        boolean outOfOrder = false;
        a[0] = Util.randInt(min,max);
        for (int i = 1; i <a.length-1; i++) {
            a[i] = Util.randInt(min, max);
            if (a[i - 1] > a[i]){
                outOfOrder = true;
                break;
            }
        }
        assertEquals(true , outOfOrder);



    }

    /**
     * testing lower boundry to make sure got the correct results
     * testing b for bounds r as range for correct
     */
    @Test
    void randIntLower(){
        int min = 20;
        int max = 50;
        for (int i = 0; i < 50; i++) {
            assertEquals(true,min <= Util.randInt(min,max));
        }
    }


    /**
     * testing  E of right bicep
     * forcing error when min > max
     */
    @Test
    void randIntError(){
        int min = 50;
        int max = 3;

        Exception illegal = assertThrows(Exception.class, () -> Util.randInt(min, max));
        assertEquals("bound must be positive", illegal.getMessage());
    }

    /**
     * testing right of right bicep
     */
    @Test
    void displayString() {
        assertEquals("4.5",Util.displayString(4.5));
    }

    /**
     * made a paramertized test
     * @param num
     */
    @ParameterizedTest
    @ValueSource(doubles = { 4.9,5.2,6.7,3.6 })

    void displayStringChecker(double num) {
        assertTrue(Util.displayString(num) instanceof String);
    }
}