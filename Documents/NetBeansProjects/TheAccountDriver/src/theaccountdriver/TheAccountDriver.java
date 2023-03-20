/**
* Class: CSC1610-B Problem Solving with Java <br />
* Instructor: Noonan, Mary G <br />
* Description: Programming Project 6 or TheAccount is a basic program that uses a driver and main method. The main method declares
* all of the variables and sets everything up. The driver is then ran and uses the information from the main to output.
* Due: 11/17/2020 </ br>
* I pledge that I have completed the programming assignment independently. <br />
I have not copied the code from a student or any source. <br />
I have not given my code to any student. <br />
Sign here: Mark Case <br />
*/
package theaccountdriver;

/**
 * Purpose: This demo uses an Account class with private data members and sets/gets methods
 * and toString() method is used.
 * @author Mark Case
*/
public class TheAccountDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        TheAccount account = new TheAccount(1122, 20000); //Passing TheAccount data members to account
        account.setAnnualInterestRate(4.5); //Setting account's annual interest rate to 4.5%

        System.out.println("Starting " + account); //Outputing accounts id, balance, annual interest rate, and monthly interest rate.
        
        account.withdraw(2500); //Telling account to withdraw $2500
        account.deposit(3000); //Telling account to deposit $3000 back in
        
        System.out.println("After the 2500 withdraw and 3000 deposit, the balance is " + account.getBalance()); //Outputing balance
        System.out.println("The monthly interest rate is " + account.getMonthlyInterestRate()); //Outputing monthly interest
        
        account.deposit(account.getMonthlyInterestRate()); //Deposits monthly interest rate
        
        System.out.println("Now " + account); //Outputs final is, balance, and annual/monthly interest rate.
    }
    
}
