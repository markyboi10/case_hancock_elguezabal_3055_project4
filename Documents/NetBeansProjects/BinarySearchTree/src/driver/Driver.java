 /**
 * Class: CSCI1301-01 Introduction to Programming Principles <br />
 * Instructor: Noonan, Mary <br />
 * Description: A main method that implements all the methods<br />
 * Due: 4/22/2021 <br />
 * I pledge by honor that I have completed the programming assignment independently. <br />
 * I have not copied the code from a student or any source. <br />
 * I have not given my code to any student. <br />
 * Sign here: Mark Case <br />
 */

package driver;

import java.io.*;
import java.util.*;

public class Driver {
    
    private static Scanner file;
    static PrintWriter outputFilePrinter;
    static Scanner inputFileScanner;

    public static void main(String[] args) throws FileNotFoundException {
       
        Scanner inFile; //Declaring scanner obj
        PrintWriter printWriter = new PrintWriter("output.txt"); //Text file where results are printed to
        inFile = new Scanner(new File("input.txt")); //Text file where student names and associated number are read from
        
        BinarySearchTreeInterface<String> aTree = new BinarySearchTree<>() {}; //Giving access to BinarySearchTree methods

        
        while (inFile.hasNext()) { //Scans input file
            String name = inFile.nextLine(); //Assigns contents to string obj
            aTree.add(name); //Add string to a tree
        } //End while
        
    
        Iterator<String> traverse = aTree.getPreorderIterator(); //Using iterator to print tree in an "postorder" fashion 	
            while (traverse.hasNext())
		printWriter.println("Preorder Iterator: " + traverse.next()); //Prints tree
            
    
        System.out.println("Postorder Recrusive: ");
        aTree.postorderTraverse();
        System.out.println("");
        System.out.println("Inorder Recursive: ");
        aTree.inorderTraverse();
     
        printWriter.close(); //Closes file
       
    } //End Main

} //End Driver
