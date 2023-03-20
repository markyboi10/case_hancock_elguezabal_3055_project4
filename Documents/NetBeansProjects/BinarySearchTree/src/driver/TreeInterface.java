 /**
 * Class: CSCI1301-01 Introduction to Programming Principles <br />
 * Instructor: Noonan, Mary <br />
 * Description: Interface w/ methods<br />
 * Due: 4/22/2021 <br />
 * I pledge by honor that I have completed the programming assignment independently. <br />
 * I have not copied the code from a student or any source. <br />
 * I have not given my code to any student. <br />
 * Sign here: Mark Case <br />
 */
package driver;

/**
 *
 * @author Mark Case
 */
public interface TreeInterface<T>
{
   public T getRootData();
   public int getHeight();
   public int getNumberOfNodes();
   public boolean isEmpty();
   public void clear();
} // end TreeInterface