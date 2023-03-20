/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package driver;
import java.util.Iterator;
/**
   An interface for a search tree.
   
   @author Frank M. Carrano
   @author Timothy M. Henry
   @version 5.0
*/
public interface BinarySearchTreeInterface<T extends Comparable<? super T>> 
       extends TreeInterface<T>
{
   /** Searches for a specific entry in this tree.
       @param anEntry  An object to be found.
       @return  True if the object was found in the tree. */
   public boolean contains(T anEntry);

   /** Retrieves a specific entry in this tree.
       @param anEntry  An object to be found.
       @return  Either the object that was found in the tree or
                null if no such object exists. */
   public T getEntry(T anEntry);

   /** Adds a new entry to this tree, if it does not match an existing 
       object in the tree. Otherwise, replaces the existing object with
       the new entry.
       @param anEntry  An object to be added to the tree.
       @return  Either null if anEntry was not in the tree but has been added, or
                the existing entry that matched the parameter anEntry
                and has been replaced in the tree. */
   public T add(T anEntry);

   /** Removes a specific entry from this tree.
       @param anEntry  An object to be removed.
       @return  Either the object that was removed from the tree or
                null if no such object exists. */
   public T remove(T anEntry);

   /** Creates an iterator that traverses all entries in this tree.
       @return  An iterator that provides sequential and ordered access
                to the entries in the tree. */
   public Iterator<T> getInorderIterator();

   /** Creates an iterator that traverses all entries in this tree.
       @return  An iterator that provides sequential and ordered access
                to the entries in the tree. */
    public String getInorderIterator(String name);
    
    /** Creates an iterator that traverses all entries in this tree.
       @return  An iterator that provides sequential and ordered access
                to the entries in the tree. */
    public Iterator<T> getPreorderIterator();
    
    /** Creates an iterator that traverses all entries in this tree.
       @return  An iterator that provides sequential and ordered access
                to the entries in the tree. */
    public String getPreorderIterator(String name);
    
    /** Creates an iterator that traverses all entries in this tree.
       @return  An iterator that provides sequential and ordered access
                to the entries in the tree. */
    public String postorderTraverse(String name);
    
    /** Creates an iterator that traverses all entries in this tree.
       @return  An iterator that provides sequential and ordered access
                to the entries in the tree. */
    public void inorderTraverse();

    /** Creates an iterator that traverses all entries in this tree.
       @return  An iterator that provides sequential and ordered access
                to the entries in the tree. */
    public void postorderTraverse();
    
} // end SearchTreeInterface