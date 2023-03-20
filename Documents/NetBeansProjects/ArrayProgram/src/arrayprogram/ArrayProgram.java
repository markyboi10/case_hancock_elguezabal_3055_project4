/**
* Class: CSC1610-B Problem Solving with Java <br />
* Instructor: Noonan, Mary G <br />
* Description: Programming Project 5 or ArrayProgram is a basic program that uses multiple methods, an array, and passing variables.
* Overall, the program will load a set of doubles from a text file and then output them to another text file 
* as an array. Lastly, using that array, the average and standard deviation will be calculated and printed out
* with the final method.<br />
* Due: 11/5/2020 </ br>
* I pledge that I have completed the programming assignment independently. <br />
I have not copied the code from a student or any source. <br />
I have not given my code to any student. <br />
<br />
Sign here: Mark Case <br />
*/

package arrayprogram;

import java.util.Scanner; //Creates scanner object
import java.io.File; //Allows access to other files
import java.io.PrintWriter; //Let's me write statments to other files
import java.io.FileNotFoundException; //Tells me if the file could be found or not

public class ArrayProgram { //Name of program
   
    static PrintWriter outputFilePrinter; //Declare file output globally to be used in all methods
    static Scanner inputFileScanner; //Declare file output globally to be used in all methods
   
    public static void main(String[] args) throws FileNotFoundException { //Main method
        
        outputFilePrinter = new PrintWriter("output.txt"); //Created file where results will be printed to
       
        File inputFile = new File("input.txt"); //Creates file where doubles are copied from
       
        inputFileScanner = new Scanner(inputFile); //Pulls doubles from file
       
        int n = inputFileScanner.nextInt(); //Amount of numbers from file
       
        double[] numbers = new double[n]; //Creates the array
        double sum = 0; //Initializes the sum
        
        loadArray(numbers, n); //Loads array from file
        
        printArray(numbers, n); //Prints array to file
       
        double average; //Brings average or mean up
        average = calcMean(numbers, n); //Calculates average or mean     
        
        double deviation; //Brings standard deviation up
        deviation = calcDeviation(numbers, n, average); //Calculates standard deviation
       
        printResults(average, deviation); //Prints results
       
        outputFilePrinter.close(); //closes file
    }
    
    public static void loadArray(double[] buffer, int n) { //Method that loads array from file
   
        for(int i = 0; i < n; i++) //For loop
            buffer[i] = inputFileScanner.nextDouble();
    }

    public static void printArray(double[] buffer, int n) { //Method that outputs array to file
   
        outputFilePrinter.println("The array elements are: ");
           
        for(int i = 0; i < n; i++) //For loop
            outputFilePrinter.println(buffer[i]); //Prints the doubles and declares them as an array
    }
   
    public static double calcMean(double[] buffer, int n) { //Method that calculates the average
   
        double sum = 0; //Initializes sum
 
        for (int i = 0; i < n; i++) { //For loop
            sum += buffer[i]; //Sets sum equal to the arrray amount
        }
        double average = sum/n; //Divides sum by 10 to get average
        
        return(average); //Returning to pass average to another method
    }
   
    public static double calcDeviation(double[] buffer, int n, double average) { //Method that calculates the standard deviation
       
        double sum = 0; //Initializing sum
        double deviation; //Declaring deviation
        
        for (int i = 0; i < n; i++) //For loop
        {
            sum += Math.pow(buffer[i] - average, 2); //Takes each array element and subtracts average from it. Then squares it by 2.
        }
        
        deviation = Math.sqrt(sum / (n - 1)); //Here that sum is taken and subtracts 10 - 1 and takes the square root.
     
     return(deviation); //Returning to pass deviation to another method
     
    }
   
    public static void printResults(double average, double deviation) { //Method that prints the results
        
        outputFilePrinter.println("The mean of the array is: " + average); //Prints the average
        
        outputFilePrinter.println("The standard deviation of the array is " + deviation); //Prints the standard deviation
        
    }
}
