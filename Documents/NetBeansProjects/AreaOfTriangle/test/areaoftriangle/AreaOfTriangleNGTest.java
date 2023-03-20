/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package areaoftriangle;

import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Mark Case
 */
public class AreaOfTriangleNGTest {
    
    public AreaOfTriangleNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of main method, of class AreaOfTriangle.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        AreaOfTriangle.main(args);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of isValid method, of class AreaOfTriangle.
     */
    @Test
    public void testIsValid() {
        System.out.println("isValid");
        double side1 = 0.0;
        double side2 = 0.0;
        double side3 = 0.0;
        boolean expResult = false;
        boolean result = AreaOfTriangle.isValid(side1, side2, side3);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of area method, of class AreaOfTriangle.
     */
    @Test
    public void testArea() {
        System.out.println("area");
        double side1 = 0.0;
        double side2 = 0.0;
        double side3 = 0.0;
        double expResult = 0.0;
        double result = AreaOfTriangle.area(side1, side2, side3);
        assertEquals(result, expResult, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
