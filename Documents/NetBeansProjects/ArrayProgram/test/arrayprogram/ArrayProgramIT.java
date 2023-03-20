/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arrayprogram;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mark Case
 */
public class ArrayProgramIT {
    
    public ArrayProgramIT() {
    }

    /**
     * Test of main method, of class ArrayProgram.
     */
    @Test
    public void testMain() throws Exception {
    }

    /**
     * Test of loadArray method, of class ArrayProgram.
     */
    @Test
    public void testLoadArray() {
    }

    /**
     * Test of printArray method, of class ArrayProgram.
     */
    @Test
    public void testPrintArray() {
    }

    /**
     * Test of calcMean method, of class ArrayProgram.
     */
    @Test
    public void testCalcMean() {
        double[] array = {1.9, 2.5, 3.7, 2.0, 1.0, 6.0, 3.0, 4.0, 5., 2.};

        assertEquals("calcMean", ArrayProgram.calcMean(array, 10), 3.1100000000000003, 0);
    }

    /**
     * Test of calcDeviation method, of class ArrayProgram.
     */
    @Test
    public void testCalcDeviation() {
        double[] array = {1.9, 2.5, 3.7, 2.0, 1.0, 6.0, 3.0, 4.0, 5., 2.};
        
        assertEquals("calcDeviation", ArrayProgram.calcDeviation(array, 10, 3.1100000000000003),  1.5573838462127583, 0);
        
    }

    /**
     * Test of printResults method, of class ArrayProgram.
     */
    @Test
    public void testPrintResults() {
    }
    
}
