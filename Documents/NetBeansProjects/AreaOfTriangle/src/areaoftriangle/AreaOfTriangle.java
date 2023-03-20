/**
* Class: CSC1610-B Problem Solving with Java <br />
* Instructor: Noonan, Mary G <br />
* Description: Programming Project 4 or AreaOfTriangle is a basic program that asks the user for three sides of a triangle. The isValid method checks if the sum of every two sides is greater than the third.
* If the sides check true than the area is given inside the area method. Otherwise the input is invalid.<br />
* Due: 10/27/2020 </ br>
* I pledge that I have completed the programming assignment independently. <br />
I have not copied the code from a student or any source. <br />
I have not given my code to any student. <br />
<br />
Sign here: Mark Case <br />
*/
package areaoftriangle;

import java.util.Scanner; //Creates scanner sbject

public class AreaOfTriangle {

    public static void main(String[] args) { //Main method
        
      int j;     
      double side[]; //Declaring array (I thought this would be a more elegant way of programming instead of having the code be very reptitive)
      side = new double[3]; //Allocating memory to array
      
      Scanner input = new Scanner(System.in); //Makes the new object for the scanner class 
        
      System.out.println("Enter a number for each of the three sides of a triangle:");
      for (j = 0; j <= 2; j += 1) { 
        side[j] = input.nextDouble(); //Input for side
        if (j == 2) {
        if (isValid(side[j-2], side[j-1], side[j])) //Output of area
            System.out.println("The area of given triangle is: " + area(side[j-2], side[j-1], side[j]));
        else
            System.out.println("Invalid input");
        }
      }
    }
    
    public static boolean isValid(double side1, double side2, double side3) { //Method that checks if every two sides is greater than the third
        
        boolean valid = side1 + side2 > side3 && side1 + side3 > side2 && side2 + side3 > side1;
        
        return valid;
    }
    
    public static double area(double side1, double side2, double side3) { //Method that calculates the area of the triangle
        double s = (side1 + side2 + side3)/2;
        double Area = Math.sqrt(s * (s - side1) * (s - side2) * (side3));
        
    return (Area);
    }
}
