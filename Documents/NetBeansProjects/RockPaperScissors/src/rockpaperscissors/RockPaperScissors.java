/**
* Class: CSC1610-B Problem Solving with Java <br />
* Instructor: Noonan, Mary G <br />
* Description: Programming Project 2 or RockPaperScissors is a basic program that randomly generates between three numbers (0,1,and 2) and matches them with rock, paper, or scissors.
* The user will start the game and play against the computer. At the end, the computer will output the results/winner.<br />
* Due: 10/1/2020 </ br>
* I pledge that I have completed the programming assignment independently. <br />
I have not copied the code from a student or any source. <br />
I have not given my code to any student. <br />
<br />
Sign here: Mark Case <br />
*/
package rockpaperscissors;

/**
 *
 * @author Mark Case
 */

import java.util.Scanner; //Creates Scanner object

public class RockPaperScissors {

    public static void main(String[] args) {
        
    Scanner scan = new Scanner(System.in); //Makes new object for Scanner class
   
    int ComputerNum1 = (int)(Math.random() * 3); //Limits random number generator to 0,1, and 2
    int UserNum2 = (int)(Math.random() * 3);
    
    var rock = 0; //Assigns each variable tot he appropriate number
    var paper = 1;
    var scissors = 2;
    
    
    if(ComputerNum1 == 0) { //When computer has rock
        if(UserNum2 == 0){ //When user has rock
            System.out.println("Computer is the rock, you are also the rock. It’s a draw. :/"); //result
        } else if(UserNum2 == 1){ //When user has paper
            System.out.println("Computer is the rock, you are the paper. You've won! :)"); //result
        } else if(UserNum2 == 2){ //When user has scissors
            System.out.println("Computer is the rock, you are the scissors. You've lost. :("); //result
        }
    }
    
    if(ComputerNum1 == 1){ //When computer has paper
        if(UserNum2 == 1){ //When user has paper
            System.out.println("Computer is the paper, you are also the paper. It's a draw. :/"); //result
        } else if(UserNum2 == 0){ //When user has rock
            System.out.println("Computer is the paper, you are the rock. You lost. :("); //result
        } else if(UserNum2 == 2){ //When user has scissors
            System.out.println("Computer is the paper, you are the scissors! You win :)"); //result
        }
    }
    
    if(ComputerNum1 == 2){ //When computer has scissors
        if(UserNum2 == 2){ //When user has scissors
            System.out.println("Computer is the scissors, you are also the scissors. It's a draw. :/"); //result
        } else if(UserNum2 == 1){ //When user has paper
            System.out.println("Computer is the scissors, you are the paper. You lost. :("); //result
        } else if(UserNum2 == 0){ //When user has rock
            System.out.println("Computer is the scissors, you are the rock. You win! :)"); //result
        }
    }
     
    
    }  
    
}
    

