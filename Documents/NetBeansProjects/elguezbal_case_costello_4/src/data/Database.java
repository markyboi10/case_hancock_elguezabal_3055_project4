package data;

import clients.Client;
import driver.Software;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tree.FamilyMember;
import tree.FamilyTree;

/**
 * Manages all the data information for this project
 * Both for Trees and Clients
 * 
 * Deals with Creating, Managing, and Updating clients
 * Also deals with storing, loading, and managing all trees
 * 
 * @author Alex
 */
public class Database {
    
    private final File clientsDirectory = new File("clients");
    private final File treesDirectory = new File("trees");
    
    // Stores all the clients by name to client object
    private HashMap<String, Client> clients = new HashMap<>();
    // Stores all the family trees by the family tree name to the tree
    private HashMap<String, FamilyTree> familyTrees = new HashMap<>();
    
    public Database() {

    }
    
    /**
     * Loads all the trees and clients in.
     */
    public void initilize() {
        loadAllTrees();
        loadAllClients();
    }
    
    /**
     * Reloads a family tree from a string name
     * 
     * @param name Name of the family tree to be reloaded
     * @return If the reload works
     */
    public boolean reloadFamilyTree(String name) {
        if (!familyTrees.containsKey(name)) {
            return false;
        }

        // Saves the tree
        File file = saveTree(name);

        if(file == null) return false;
        
        // Reloads the family tree
        FamilyTree familyTree = Software.getFileManager().createFamilyTreeFromFile(file);

        // If the files doesn't compile corectly, don't add it
        if (familyTree != null) {
            getFamilyTrees().put(familyTree.getName(), familyTree);
        } // Error catching
        else {
            System.out.println("error re-loading trees");
            return false;
        }
        
        return true;
    }
    
    /**
     * Method to save a FamilyTree
     * 
     * @param name FamilyTree name
     */
    public File saveTree(String name) {
        
        FamilyTree familyTree = getFamilyTree(name);
        
        // Builds the file
        File file = new File(treesDirectory+"\\"+familyTree.getName()+".txt");
        
        if(file == null || !file.exists()) {
            // ToDo error catching
            System.out.println("File could not save, error in process 1");
            return null;
        }
        
        // Saves the file
        if(!Software.getFileManager().saveTree(familyTree, file)) {
            // ToDo error catching
            System.out.println("Error saving file " + file.getPath());
        }
        
        return file;
    }
    
    public void saveClient(String name) {
        FamilyTree familyTree = getFamilyTree(name);
        
        // Builds the file
        File file = new File(treesDirectory+"\\"+familyTree.getName()+".txt");
        
        if(file == null || !file.exists()) {
            // ToDo error catching
            System.out.println("File could not save, error in process 1");
            return;
        }
        
        // Saves the file
        if(!Software.getFileManager().saveTree(familyTree, file)) {
            // ToDo error catching
            System.out.println("Error saving file " + file.getPath());
        }
    }
    
     /**
     * Gets a family tree from a name
     * 
     * @param treeName name of the tree
     * @return FamilyTree or null if no tree is found
     */
    public FamilyTree getFamilyTree(String treeName) {
        return getFamilyTrees().get(treeName);
    }
    
    /**
     * Gets all the associated family trees with a client
     * 
     * @param clientName Name of the client
     * @return All the family trees with a clients name.
     */
    public List<FamilyTree> getFamilyTreesFromClientName(String clientName) {
        
        List<FamilyTree> possibleFamilyTrees = new ArrayList<>();
        
        familyTrees.values().forEach(n -> {
            for(FamilyMember m : n.getFamilyMembers()) {
                if(m.getName().equalsIgnoreCase(clientName)) possibleFamilyTrees.add(n);
            }
        });
        
        return possibleFamilyTrees;
    }
    
    /**
     * Private methods
     */
    
    private void loadAllTrees() {
        if(treesDirectory == null || !treesDirectory.exists()) {
            treesDirectory.mkdirs();
        }
        
        // Finds all the files in a directory and creates family trees from each file
        for(File n : treesDirectory.listFiles()) {
            FamilyTree familyTree = Software.getFileManager().createFamilyTreeFromFile(n);
            
            // If the files doesn't compile corectly, don't add it
            if(familyTree != null) {
                getFamilyTrees().put(familyTree.getName(), familyTree);
            } 
            // Error catching
            else {
                System.out.println("error loading trees");
            }
        }
        
    }
    
    private void loadAllClients() {
       if(clientsDirectory == null || !clientsDirectory.exists()) {
            clientsDirectory.mkdirs();
        }
        
        // Finds all the files in a directory and creates family trees from each file
        for(File n : clientsDirectory.listFiles()) {
            Client client = Software.getFileManager().createClientFromFile(n);
            
            // If the files doesn't compile corectly, don't add it
            if(client != null) {
                clients.put(client.getName(), client);
            } 
            // Error catching
            else {
                System.out.println("error loading clients");
            }
        } 
    } 

    /**
     * @return the familyTrees
     */
    public HashMap<String, FamilyTree> getFamilyTrees() {
        return familyTrees;
    }
            
}
