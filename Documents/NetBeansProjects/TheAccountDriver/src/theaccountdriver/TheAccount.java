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
 *
 * @author Mark Case
 */

/**
 *  Purpose:  This class manages data for an account
 *  data members : id and balance and annualInterestRate 
 * and private methods get/set and toString shown here
 *  methods get/set and toString shown here
 * @author Mark Case
 */

class TheAccount {
    
    //Private data members
    private int id;
    private double balance;
    private double annualInterestRate;
    
    /*
    The default constructor
    */
    public TheAccount() {
        System.out.println("Default constructor called: ");
        id = 1122;
        balance = 20000;
    }
    /*
    Contructor
    @param newId
    @param newBalance
    */
    public TheAccount(int newId, double newBalance) {
        id = newId;
        balance = newBalance;
    }
    
    //Set and Get methods that are a must because data is private
    
    /*
    Sets private data member id
    */
    public void setId(int i) {
        id = i;
    }
    
    /*
    returns id
    */
    public int getId() {
        return id;
    }
    
    /*
    Sets private data member balance
    */
    public void setBalance(double newBalance) {
        balance = newBalance;
    }
    
    /*
    returns balance
    */
    public double getBalance() {
        return balance;
    }
    
    /*
    Sets private data member annualInterestRate
    */
    public void setAnnualInterestRate(double interest) {
        annualInterestRate = interest;
    }
    
    /*
    Returns annualInterestRate
    */
    public double getAnnualInterestRate() {
        return annualInterestRate;
    }
    
    /*
    Returns MonthlyInterestRate
    */
    public double getMonthlyInterestRate() {
        return (annualInterestRate / 100) * balance / 12;
    }
    
    /*
    Calculates withdraw
    */
    public void withdraw(double amount) {
        balance -= amount;
    }
    
    /*
    Calculates deposit
    */
    public void deposit(double amount) {
        balance += amount;
    }
    
    /*
    Return mthos class data as a String
    */
    @Override
    public String toString() {
        return "the account's id is " + id + ", balance " + balance + ", annual interest rate "
                + getAnnualInterestRate() + ",and monthly interest rate " + getMonthlyInterestRate();
    }
    
}