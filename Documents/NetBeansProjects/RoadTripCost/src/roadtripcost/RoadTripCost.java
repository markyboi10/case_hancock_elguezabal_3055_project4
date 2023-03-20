/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roadtripcost;

/**
 * Class: CSC1610-B Problem Solving with Java <br />
 * Instructor: Noonan, Mary G <br />
 * Description: Programming Project 1 or RoadTripCost is a basic program that takes input of distance, mpg, and price per gallon that in turn outputs the total cost of your road trip. <br />
 * Due: 9/17/2020 </ br>
 * I pledge that I have completed the programming assignment independently. <br />
 I have not copied the code from a student or any source. <br />
 I have not given my code to any student. <br />
<br />
 Sign here: Mark Case <br />
 */

import java.util.Scanner; //Creates Scanner object
import java.text.NumberFormat; //Formates final output to two decimal places to the right of the decimal point

public class RoadTripCost {
    
    public static void main(String[] args) {
    
    double total = 0; //declaring variables
    
    Scanner scan = new Scanner(System.in); //Makes new object for Scanner class
        
    System.out.println("Input your total driving distance:"); //String asking for input of driving distance
    double miles = scan.nextDouble();
    System.out.println("You will be driving " + miles + " miles."); //Output for driving distance
    
    System.out.println("What is your average MPG of your car?"); //String asking for input of MPG
    double mpg = scan.nextDouble();
    System.out.println("Understood! According to you, you will get " + mpg + " mpg." ); //Output for MPG
    
    System.out.println("How much will you be paying for gas?"); //String asking for gas price per gallon
    double gas = scan.nextDouble();
    System.out.println("Ok, so gas will cost you $" + gas + " per gallon."); //Output for gas price per gallon
    
    double cost = (miles / mpg * gas); //Formula to calculate total cost or the road trip
    total += cost; //Sets total equal to cost
    
    NumberFormat defaultFormat = NumberFormat.getCurrencyInstance(); //Decalring a specific format to get two decimal points right of the decimal point
    System.out.println("The total cost of your road trip will be " + defaultFormat.format(total)); //Outputs the final coat of the road trip and fromates the answer to get two decimal points right of the decimal point
// TODO code application logic here
    }
    
}
