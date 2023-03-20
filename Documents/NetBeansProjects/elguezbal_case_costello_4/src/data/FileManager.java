package data;

import clients.Client;
import driver.Software;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import tree.DeceasedRelative;
import tree.FamilyMember;
import tree.FamilyTree;
import tree.LivingRelative;

/**
 * Manages file processes with our software.
 *
 * @author Alex, Derek
 */
public class FileManager {
        
    /**
     * Saves a FamilyTree to a file
     * @param familyTree Family tree to be saved
     * @param file File to be saved too
     * 
     * @return If the saving process worked, or not
     */
    public boolean saveTree(FamilyTree familyTree, File file) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(file));
            
            // Loads in all the family members first            
            for(FamilyMember n : familyTree.getFamilyMembers()) {                
                bf.append(n.toString());
                bf.newLine();
            }
            
            // Adds whitespace
            bf.newLine();
            
            // Makes sure all the vertexes are unvisited, for the traversal.
            familyTree.setAllUnvisited();
            // Adds relationships
            traverseTreeForWriting(familyTree.getFamilyMembers().get(0), bf);
            // Unvisit all vertexes
            familyTree.setAllUnvisited();
            
            
            // Ends the writing process
            bf.flush();
            bf.close();

        } catch(IOException e) {
            System.out.println("IO Exception with saving family trees.");
            return false;
        }
        
        return true;
    }
    
    /**
     * Saves a Client and FamilyTree relationship to a file
     * 
     * @param client Client tree to be saved
     * @param file File to be saved too
     * 
     * @return If the saving process worked, or not
     */
    public boolean saveClient(Client client, File file) {
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(file));
            Object[] treeNames = client.getFamilyTrees().keySet().toArray();
            
            // Start the line with "trees" so that the file can be read.
            bf.append("trees");
            
            // Write all family tree names associated with this client to the file
            for(Object obj : treeNames) {
                String treeName = (String)obj;
                bf.append("," + treeName);
            }
            
            // Ends the writing process
            bf.flush();
            bf.close();

        } catch(IOException e) {
            System.out.println("IO Exception with saving family trees.");
            return false;
        }
        
        return true;
    }
            
    /**
     * Creates a client from a file
     * 
     * @param file File to be created from
     * @return Client from a file
     */
    public Client createClientFromFile(File file) {
        Client client = new Client(file.getName().substring(0,file.getName().indexOf(".")));
        
        try {
           if(file == null || !file.exists()) return null;
            
           BufferedReader writer = new BufferedReader(new FileReader(file));
            
           while(writer.ready()) {
               String line = writer.readLine();
               
               // Builds and adds the trees to the client
               if(line.startsWith("trees")) {
                   String[] args = line.split(",");
                   
                   // If this client has no trees in the arguments
                   if(args.length <= 1) continue;
                   
                   // Loads in all the family trees in the line
                   for(int i = 1; i < args.length; i++) {
                       String treeName = args[i];
                       
                       // FamilyTree from the name
                       FamilyTree familyTree = Software.getDatabase().getFamilyTree(treeName);
                       
                       // If the FamilyTree wasn't found
                       if(familyTree == null) continue;
                       
                       client.addFamilyTree(familyTree);
                   }
               }
           }
           
        } catch(IOException e) {
            System.out.println("IO Exception with loading clients.");
            return null;
        }
        
        // Returns the cline with the built in family trees
        return client;
    }
    
    /**
     * Creates a family tree from the input file
     * @param file File to create the family tree
     * 
     * @return Family tree from the file
     */
    public FamilyTree createFamilyTreeFromFile(File file) {
        FamilyTree familyTree = new FamilyTree(file.getName().substring(0,file.getName().indexOf(".")));
        List<FamilyMember> members = new ArrayList<>();
        
        try {
            if(file == null || !file.exists()) return null;
                        
            BufferedReader writer = new BufferedReader(new FileReader(file));
            
            while(writer.ready()) {
                String line = writer.readLine();
                
                // If the line is a space then continue
                if(line.isEmpty() || line.isBlank()) continue;
                
                String[] args = line.split(",");
                
                if(line.contains("parentof") || line.contains("marriedto")) {
                    String name1 = args[0].trim();                    
                    String name2 = args[2].trim();
                    
                   
                    // Family member objects
                    FamilyMember member1 = getFamilyMemberFromName(members, name1);
                    FamilyMember member2 = getFamilyMemberFromName(members, name2);
                    
                     // Exit if the family member wasn't loaded
                     if(member1 == null || member2 == null) return null;
                     
                     // If the relation is a parent relation
                     if(line.contains("parentof")) {
                         // Sets the relation
                        member1.addChild(member2);
                        member2.addParent(member1);
                     } 
                     // If the relation is a marage relation
                     else {
                        // Sets the relation
                        member1.setSpouse(member2);
                        member2.setSpouse(member1);
                     }
                }
                else {
                    // The line must have enough arguments to be for a family tree
                    if(args.length < 3) return null;
                    
                    String name = args[0].trim();
                    String birthdate = args[1];
                    // Determines if this line is a living member
                    if(args.length >= 4) {
                        String city = args[2];
                        String state = args[3];
                        
                        // Adds the family members
                        members.add(new LivingRelative(name, birthdate, new ArrayList<>(), new ArrayList<>(), null, city, state));
                    } 
                    // Determines if this line is a Deceased Relative
                    else {
                        String deathdate = args[2];
                        
                        // Adds the DeceasedRelative
                        members.add(new DeceasedRelative(name, birthdate, new ArrayList<>(), new ArrayList<>(), null, deathdate));
                    }
                }
            }
            
        } catch(IOException e) {
            System.out.println("IO Exception with loading trees.");
            return null;
        }
        
        // Create the family tree
        
        /*
        // Finds the root of the family trees
        // There could be one root, or more than one root
        FamilyMember root1 = findRootFamilyMember(members, null);
        FamilyMember root2 = findRootFamilyMember(members, root1);
        
        // If no roots are found, exit.
        if(root1 == null && root2 == null) return null;
        
        // Assigns the roots
        familyTree.setRoot1(root1);
        familyTree.setRoot2(root2);
        */
        
        // ToDo need to set this to the lowest level leaf
        FamilyMember client = findRootFamilyMember(members, null);
        familyTree.setClient(client);
                           
        // Sets all the family members
        familyTree.setFamilyMembers(members);
        
        // Returns the created family tree
        return familyTree;
    }
    
    /**
     * Private methods
     */
    
    /**
     * Gets a family member from a name
     * 
     * @param members List of FamilyMembers
     * @param name Name of the FamilyMember that is being searched for
     * @return Family member
     */
    private FamilyMember getFamilyMemberFromName(List<FamilyMember> members, String name) {        
        return members.stream().filter(n -> n.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
    
    /**
     * Finds the root of a family tree
     * Defined as a family member with no parents
     * 
     * @param members List of Family members to search over
     * @param check A check to see if this family member was previously used
     * @return A FamilyMember that is the root of a family tree
     */
    private FamilyMember findRootFamilyMember(List<FamilyMember> members, FamilyMember check) {
        for(FamilyMember n : members) {
            // Checks to see if the 
            if(n.getChildren().isEmpty() && n != check) return n;
        }
        return null;
    }
    
    private void traverseTreeForWriting(FamilyMember member, BufferedWriter writer) throws IOException {
        
        // If the member has already been visited, return.
        if(member.isVisited()) {
            return;
        }
        
        // Set the member to fisited
        member.setVisited(true);
        
        //Do data
        //System.out.println(member.getName() + " "+ member.getSpouse() + " " + member.getChildren().size() + " " + member.getParents().size());
      
        /**
         * DATA START
         */
        
        // Adds in spouses
        if(member.getSpouse() != null && !member.getSpouse().isVisited()) {
            writer.write(member.getName()+",marriedto,"+member.getSpouse().getName());
            writer.newLine();
        }
        
        // Adds in all children, that this member is a parent of
        for(FamilyMember child : member.getChildren()) {
            writer.write(member.getName()+",parentof,"+child.getName());
            writer.newLine();
        }
        
        /**
         * DATA END
         */
        
        // Traverses spouses
        if(member.getSpouse() != null) {
            traverseTreeForWriting(member.getSpouse(), writer);
        }
        
        // Traverses parents
        for(FamilyMember parent : member.getParents()) {
            traverseTreeForWriting(parent, writer);
        }
        
        // Traverses children
        for(FamilyMember child : member.getChildren()) {
            traverseTreeForWriting(child, writer);
        }
      
    }
}
