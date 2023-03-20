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
    public interface BinaryTreeInterface<T> extends TreeInterface<T>, 
                                                TreeIteratorInterface<T> {

   /** Sets the data in the root of this binary tree.
       @param rootData  The object that is the data for the tree's root.
   */
   public void setRootData(T rootData);

   /** Sets this binary tree to a new binary tree.
      @param rootData   The object that is the data for the new tree's root.
      @param leftTree   The left sub tree of the new tree.
      @param rightTree  The right sub tree of the new tree. */
   public void setTree(T rootData, BinaryTreeInterface<T> leftTree,
                                   BinaryTreeInterface<T> rightTree);
} // end BinaryTreeInterface

