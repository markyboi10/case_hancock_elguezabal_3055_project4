/**
* Class: CSC2820-B Data Structures <br />
* Instructor: Mark Case <br />
* Description: This program is the Driver class that tests TimerInterface and
       the Timer class
* Due: 2/12/2021 </ br>
* I pledge that I have completed the programming assignment independently. <br />
I have not copied the code from a student or any source. <br />
I have not given my code to any student. <br />
Sign here: Mark Case <br />
*/
package timerinterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class TimeDriver {
    

public static void main(String[] args) {

PrintWriter toFile = null;
    try {
        toFile = new PrintWriter("outfile1.txt");
    } catch (FileNotFoundException e) {
        System.out.println("PrintWriter error opening the file");
        System.out.println(e.getMessage());
    }
    
//Create a myClock/yourClock object to instantiate a Timer object    

TheTimer myClock = new TheTimer();
TheTimer yourClock = new TheTimer(86399);

//Output the clock value of myClock/yourClock object using toString
toFile.println(myClock);
toFile.println(yourClock);

//Set value of myClock object to 2
myClock.setTimer(2);

//Output clock value of fun object using toString
toFile.println("myClock: " + myClock);

//Increment the yourClock value twice
yourClock.incrementTimer();
//increment again
yourClock.incrementTimer();


//Output yourclock using toString
toFile.println("yourClock: " + yourClock);

//Output whether timer value is zero using method
toFile.println("myClock is zero: " + myClock.isZero());

//Set value of myClock object to -1
myClock.setTimer(-1);

//Output whether timer value is zero using method
toFile.println("myClock is zero: " + myClock.isZero());

//Output current time of myClock
toFile.println("myClock's current time is: " + myClock.getCurrentTimer());

toFile.close();

    }


}