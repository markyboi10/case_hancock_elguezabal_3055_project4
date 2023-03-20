/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timerinterface;

/**
* Class: CSC2820-B Data Structures <br />
* Instructor: Mark Case <br />
* Description: This class is in charge of a timer
 *   It implements the Timer Interface
 *   It stores an integer clock and will
 *   set the clock, increment/decrement, check for zero, get the current count and return
 *   a message with the clock's value.
* Due: 2/12/2021 </ br>
* I pledge that I have completed the programming assignment independently. <br />
I have not copied the code from a student or any source. <br />
I have not given my code to any student. <br />
Sign here: Mark Case <br />
*/


public class TheTimer implements TimerInterface {
    
    private
            int timer; //the timer value which is private
   

/**
    * The constructor used to initialize count to zero
*/
  
/* The counstructor that sets the count to zero
  
*/    

public TheTimer(){timer = 0;} //This is a contructor, same name as the class, no return
                              //value, can take arguments, can be ovewrloaded.
                              //Overload -- class has two or more methods with same name
                              //but different parameters or return values.
                              //Contructors are never called by user,
                              //they get executed when object of that type has been
                              //instantiated.
    
public TheTimer(int x)
    {timer = x;}
   
/*@ Sets the timer, the private data member and if time is below 0
    @param TheTimer used to set the count value
*/
    
    public void setTimer(int theTimer) {
        timer = theTimer;
        if(timer < 0)
            timer = 0;
        
}
   

/**
    Increases the count by one.
*/
    
    public void incrementTimer() {
    if(timer<86400)
        timer++;
    else
        System.out.println("Can't increment beyond 86,400");
        timer = 0;
}

/**
    Decreases this count by one and guarantees that the result is not negative
*/
   
    public void decrementTimer() {
    if(timer>0)
        timer--;
    else
        System.out.println("Can't decrement, timer will be zero");
}

/**
    * Tests to see whether timer is zero
    * @return true if the timer is zero/less than or equal to 86,400, false otherwise
*/
    
    public boolean isZero() {
    if(timer == 0)
        return true;
    else
        return false;
}

/**
    * Returns the value of timer
    * 
    * @return The non-negative value/value less than or equal too 86,400
*/
public int getCurrentTimer() {
    return timer;
}

 /**
    * Creates a string that stores a message and the timer value
    * @return labeled string with the timer value
    * 
*/ 
@Override
public String toString() {
    return "The value of timer is: " + timer;
   }

}

  

