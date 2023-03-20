/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;
/**
   A class of run time exceptions thrown by methods to
   indicate that a queue is empty.
   @author Frank M. Carrano
   @author Timothy M. Henry
   @version 5.0
*/
public class EmptyQueueException extends RuntimeException
{
   public EmptyQueueException()
   {
      this(null);
   } // end default constructor
   
   public EmptyQueueException(String message)
   {
      super(message);
   } // end constructor
} // end EmptyQueueException