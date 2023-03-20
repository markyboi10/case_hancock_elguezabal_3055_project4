package clients;

import java.util.HashMap;
import tree.FamilyTree;

/**
 *
 * @author Alex
 */
public class Client {
    
    private String name;
    
    // Map of Family Tree names to Family Trees
    private HashMap<String, FamilyTree> familyTrees;
    
    /**
     * Default Constructor
     * Used for creating a new client
     * 
     * @param name Name of the client
     */
    public Client(String name) {
        this(name, new HashMap<>());
    }
    
    /**
     * Overload Constructor
     * Used for managing a client
     * 
     * @param name
     * @param familyTrees 
     */
    public Client(String name, HashMap<String, FamilyTree> familyTrees) {
        this.name = name;
        this.familyTrees = familyTrees;
    }
    
    /**
     * Adds a family tree to this client
     * 
     * @param familyTree Family tree
     */
    public void addFamilyTree(FamilyTree familyTree) {
        familyTrees.put(familyTree.getName(), familyTree);
    }
    
    /**
     * Removes a family tree from this client
     * 
     * @param familyTree Family Tree
     */
    public void removeFamilyTree(FamilyTree familyTree) {
        familyTrees.remove(familyTree.getName());
    }
    
    /**
     * Removes a family tree from this client
     * 
     * @param familyTreeName Family Tree's name
     */
    public void removeFamilyTree(String familyTreeName) {
        familyTrees.remove(familyTreeName);
    }

    /**
     * Determines if this client has a specific family tree
     * 
     * @param familyTree Family tree
     * @return true if it does
     */
    public boolean hasFamilyTree(FamilyTree familyTree) {
        return familyTrees.containsKey(familyTree.getName()) || familyTrees.containsValue(familyTree);
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the familyTrees
     */
    public HashMap<String, FamilyTree> getFamilyTrees() {
        return familyTrees;
    }
    
}
