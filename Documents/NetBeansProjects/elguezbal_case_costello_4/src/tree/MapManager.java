package tree;

import driver.Software;
import javax.swing.JScrollPane;

/**
 *
 * @author Alex
 */
public class MapManager {
    
    private MapPanel map;
    
    public MapManager() {
       
    }
    
    /**
     * Assigns the current map to a new map
     * 
     * @param familyTree FamilyTree to be displayed
     */
    public void assignMap(FamilyTree familyTree) {
        
        MapPanel oldMap = null;
        
        // Remove drawings that are there
        if(map != null) {
            oldMap = map;
        }
        
        MapPanel map = new MapPanel(familyTree);
        this.map = map;
        
        // Assigns the map here
        Software.getMYGUI().displayTreePanel(map, oldMap, familyTree.getName());
    }

    /**
     * @return the map
     */
    public MapPanel getMap() {
        return map;
    }
    
}
