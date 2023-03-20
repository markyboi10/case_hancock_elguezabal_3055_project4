/**
* Class: CSC1610-B Problem Solving with Java <br />
* Instructor: Noonan, Mary G <br />
* Description: Programming Project 3 or Averages is a basic program that calculates a professor's class averages. 
* The user will input grades until accidentally typing 0. Once 0 is inputted, the program will give an average for each class.
* It will also give the average of both classes combined at the end.<br />
* Due: 10/15/2020 </ br>
* I pledge that I have completed the programming assignment independently. <br />
I have not copied the code from a student or any source. <br />
I have not given my code to any student. <br />
<br />
Sign here: Mark Case <br />
*/
package averages;

/**
 *
 * @author Mark Case
 */

import java.util.Scanner; //Creates Scanner object

public class Averages {

public static void main(String[] args) {

Scanner input = new Scanner(System.in); //Makes new object for Scanner class

double total; //Total grade number
double finalGrade1; //Average grade for Class 1
double finalGrade2; //Average grade for Class 2
double number; //Number of classes the professor is allowed to have
double numClasses; //Number of classes
double saveNumClasses; //Lets computer know number of classes as supposed to hard save
int kounter; //Counts number of grade inputs
double numberGrade; //Grade input
boolean check = false; //Checks loop inputs

final int minimum = 2; //Restrains minimum to 2
final int maximum = 2; //Restrains maximum to 2

do { //Do-While Loop
    System.out.println("How man classes does Professor Poindexter have?");
    System.out.println("");
    number = input.nextDouble();
    if (number >= minimum && number <= maximum) { //Only allows 2 as answer
        check = false;
    }
    else { //if not 2, than loop repeats
        check = true;
        System.out.println("");
        System.out.println("Invalid input. Please try again");
        System.out.println("");}
    } while(check);

numClasses = number;
saveNumClasses = numClasses;

System.out.println("");
System.out.println("Input a student grade for class 1:"); //Calling for inputs
System.out.println("");

kounter = 0; //Setting all to 0 to initialize 
total = 0;
finalGrade1 = 0;
numberGrade = 0;
do {
    numberGrade = input.nextDouble();
    if (numberGrade == 0 && kounter == 0) { //If user enters 0 first, finalGrade is 0
        finalGrade1 = 0;
        break; //Breaks out of loop
    } 
    if (numberGrade == 0) { 
        finalGrade1 = total / kounter; //Takes total amount and divides by amount of inputs
    } else {
            kounter = kounter + 1; //Recognizes how many times a user inputs a grade
            total = total + numberGrade; //Sum of grades
    }
} while(numberGrade != 0); //Stays in loop when input is not 0

System.out.println("");
System.out.println("The average for Class 1 is: " + finalGrade1); //Average grade for Class 1

System.out.println("");
System.out.println("Now please enter a student grade for Class 2.");
System.out.println("");

kounter = 0; //Setting all to 0 to initialize 
total = 0;
finalGrade2 = 0;
numberGrade = 0;
do {
    numberGrade = input.nextDouble();
    if (numberGrade == 0 && kounter == 0) { //If user enters 0 first, finalGrade is 0
        finalGrade2 = 0;
        break; //Breaks out of loop
    }
    if (numberGrade == 0) {
        finalGrade2 = total / kounter; //Takes total amount and divides by amount of inputs
    } else {
        kounter = kounter + 1; //Recognizes how many times a user inputs a grade
        total = total + numberGrade; //Sum of grades
    }
} while(numberGrade != 0); //Stays in loop when input is not 0

System.out.println("");
System.out.println("The average for Class 2 is: " + finalGrade2); //Average grade for Class 2


double allClasses = (finalGrade1 +  finalGrade2) / saveNumClasses; //Takes average of both classes combined and outputs answer
System.out.println("");
System.out.println("The average for all of Professor Poindexter's two classes is: " + allClasses);



    }    

}
