package tree;

import driver.Software;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Alex, Derek, Mark
 */
public class FamilyTree {

    private FamilyMember client;
    private List<FamilyMember> familyMembers;

    private String name;

    public FamilyTree(String name) {
        setName(name);
    }

    public void setClient(FamilyMember familyMember) {
        this.client = familyMember;
    }

    /**
     * @return the client
     */
    public FamilyMember getClient() {
        return this.client;
    }

    /**
     * Sets the name
     *
     * @param name Name of the FamilyTree
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Remove familyMember
     * 
     * @param name Name of the person to be removed
     * @return determines if this removal was executed correctly
     */
    public boolean removeFamilyMember(String name) {
        // Finds and asserts that the family member exists in this family tree.
        FamilyMember toRemove = familyMembers.stream().filter(n -> n.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        
        if(toRemove == null) return false;
        
        // Loops through all the family members and removes any relations.
        for(int i = 0; i < familyMembers.size(); i++) {
            
            FamilyMember n = familyMembers.get(i);
            
            // Removes any spouses
            if(n.getSpouse() != null && n.getSpouse().equals(toRemove))
                n.setSpouse(null);
            
            // Removes parents
            n.removeParent(toRemove);
            
            // Removes childen
            n.removeChild(toRemove);
        }
        
        // Remove outgoing references if they exist
        if(toRemove.getChildren() != null)
            toRemove.getChildren().clear();
        if(toRemove.getParents() != null)
            toRemove.getParents().clear();
        
        // Removes the family member from the familyMembers list and the map
        toRemove.removeFromMap();
        familyMembers.remove(toRemove);
        
        return true;
    }
    
    /**
     * Used for updating the state of a family member.
     * Main use is for converting from living to deceased.
     * 
     * @param familyMember New family member object to convert to.
     */
    public void updateFamilyMembersSate(FamilyMember familyMember) {
        String name = familyMember.getName();
        
        // We only need to update the "familyMembers" list because it is all that is saved.
        for(int i = 0; i < familyMembers.size(); i++) {
            // If the family member is found, updates it.
            if(familyMembers.get(i).getName().equalsIgnoreCase(familyMember.getName())) {
                familyMembers.set(i, familyMember);
            }
        }
        
        // Reloads this family tree
        Software.getDatabase().reloadFamilyTree(getName());

        // ReDisplay
        Software.getMapManager().assignMap(this);
    }

    /**
     * Adds a FamilyMember to this FamilyTree
     * 
     * @param familyMember New instance of the family member to be added
     * @param namesOfParents List of the names of parents, can be empty if there are none
     * @param namesOfChildren List of the names of children, can be empty if there are none
     * @param nameOfSpouse Name of the spouse, can be empty if there isn't one
     * @return If adding this member worked.
     */
    public boolean addMember(FamilyMember familyMember, List<String> namesOfParents, List<String> namesOfChildren, String nameOfSpouse) {

        // Asserts that there is a family member
        if (familyMember == null) {
            return false;
        }

        // Gets a list of the parents
        List<FamilyMember> parents = namesOfParents.stream().map(n -> {
            for (FamilyMember m : familyMembers) {
                if (n.equalsIgnoreCase(m.getName())) {
                    return m;
                }
            }
            return null;
        })
                .filter(Objects::nonNull).collect(Collectors.toList());

        // Gets a list of the children
        List<FamilyMember> children = namesOfChildren.stream().map(n -> {
            for (FamilyMember m : familyMembers) {
                if (n.equalsIgnoreCase(m.getName())) {
                    return m;
                }
            }
            return null;
        })
                .filter(Objects::nonNull).collect(Collectors.toList());

        // Gets the spouse for this family member
        FamilyMember spouse = familyMembers.stream().filter(n -> nameOfSpouse.equalsIgnoreCase(n.getName())).findFirst().orElse(null);

        /**
         * Assigns the values
         */
        
        // Asigning spouse
        if (spouse != null) {
            familyMember.setSpouse(spouse);
            spouse.setSpouse(familyMember);
        }

        // Assigns all the parents
        parents.forEach(n -> {
            familyMember.addParent(n);
            n.addChild(familyMember);
        });

        // Assigns all the children
        children.forEach(n -> {
            familyMember.addChild(n);
            n.addParent(familyMember);
        });
         
        // Adds the member into the map
        familyMembers.add(familyMember);
        
        return true;
    }
    
    /**
     * Gets a Family Member by their name
     * 
     * @param name Name of the Family Member to be found
     * @return The Family Member object.
     */
    public FamilyMember getByName(String name) {
        return familyMembers.stream().filter(n -> n != null).filter(n -> n.getName() != null).filter(n -> n.getName().equalsIgnoreCase(name)).findFirst().orElseGet(null);
    }

    /**
     * Sets each member in this family tree to unvisted. Used after traversing
     * the tree.
     */
    public void setAllUnvisited() {
        familyMembers.forEach(n -> n.setVisited(false));
    }

    /**
     * @return the familyMembers
     */
    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }
   

    /**
     * @param familyMembers the familyMembers to set
     */
    public void setFamilyMembers(List<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

}
