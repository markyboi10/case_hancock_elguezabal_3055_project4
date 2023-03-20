/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timerinterface;

/**
* Class: CSC2820-B Data Structures <br />
* Instructor: Mark Case <br />
* Description: * An interface for a class that keeps track of a non-negative count less than 86,400.
    * The behavior includes:
    * setting the timer, incrementing/decrementing the timer, checking to see if the timer
    * is zero, getting the current time and returning a
    * message with the timer's value.
* Due: 2/12/2021 </ br>
* I pledge that I have completed the programming assignment independently. <br />
I have not copied the code from a student or any source. <br />
I have not given my code to any student. <br />
Sign here: Mark Case <br />
*/

public interface TimerInterface {
    
/** Sets this timer to a given value.
@param TheClock. The integer initial value of the clock.
*/  
public void setTimer(int TheTimer);

/**
    Increases this timer's count by one, guarantees that the result is not above 86,400.
*/
public void incrementTimer();

/**
    Decreases the timer by one, and guarantees that the result is not negative.
*/
public void decrementTimer();

/** 
    @return true if the timer is zero, false otherwise.
*/
public boolean isZero();
  
/**
    * Returns the value of timer 
    * @return The non-negative value/equal or above 86,400.
*/
public int getCurrentTimer();
   
/**
    Creates a string that stores a message and the clock value
    @return labeled string with the timer value
*/ 

@Override
public String toString();

}
