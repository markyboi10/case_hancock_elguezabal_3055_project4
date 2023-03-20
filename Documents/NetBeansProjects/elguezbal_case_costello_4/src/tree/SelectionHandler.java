package tree;

import driver.Software;
import java.awt.event.*;

/**
 * Basic focus listener that handles the focusing actions of our FamilyMember JPanels.
 *
 * @author Derek Costello
 */
public class SelectionHandler extends MouseAdapter {
    
    private MapPanel map;
    
    /**
     * Default constructor that gathers our MapPanel.
     * 
     * @param map MapPanel
     */
    public SelectionHandler(MapPanel map) {
        this.map = map;
    }

    /**
     * When the mouse is clicked on a FamilyMember, set the new selectedFamilyMember as that FamilyMember.
     * 
     * @param e mouse click
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // When clicked, set the new selected family member
        FamilyMember selMem = map.getSelectedMember();
        if(selMem != null && selMem.equals((FamilyMember)e.getSource())) {
            map.setSelectedMember(null);
        
            // Empties the selected information
            Software.getMYGUI().updateSelectedInformation(null , true);
        } else {
            FamilyMember newMem = (FamilyMember)e.getSource();
            map.setSelectedMember(newMem);
            
            // Displays the selected members information
            if(newMem != null) {
                if(newMem instanceof DeceasedRelative decMem) {
                    System.out.println("Deceased Relative.");
                    String children = decMem.getChildren().toString();
                    
                    String[] parameters = new String[6];
                    
                    // If there is no spouse or children
                    if(!decMem.hasSpouse() && (decMem.getChildren() == null || decMem.getChildren().isEmpty()))
                        Software.getMYGUI().updateSelectedInformation(new String[]{ decMem.getName(), decMem.getBirthdate(), decMem.getParamaters().get(0), "", "", ""} , false);
                    // If there is a spouse but no children
                    else if(decMem.getChildren() == null || decMem.getChildren().isEmpty())
                        Software.getMYGUI().updateSelectedInformation(new String[]{ decMem.getName(), decMem.getBirthdate(), decMem.getParamaters().get(0), "", decMem.getSpouse().getName(), ""} , false);
                    // If there is children but no spouse
                    else if(!decMem.hasSpouse())
                        Software.getMYGUI().updateSelectedInformation(new String[]{ decMem.getName(), decMem.getBirthdate(), decMem.getParamaters().get(0), "", "", children.substring(1, children.length() - 1)} , false);
                    // If there is a spouse and children
                    else 
                        Software.getMYGUI().updateSelectedInformation(new String[]{ decMem.getName(), decMem.getBirthdate(), decMem.getParamaters().get(0), "", decMem.getSpouse().getName(), children.substring(1, children.length() - 1)} , false);
                                        
                } else if(newMem instanceof LivingRelative livMem) {
                    System.out.println("Living Relative.");
                    String children = livMem.getChildren().toString();
                    
                    // If there is no spouse or children
                    if(!livMem.hasSpouse() && (livMem.getChildren() == null || livMem.getChildren().isEmpty()))
                        Software.getMYGUI().updateSelectedInformation(new String[]{ livMem.getName(), livMem.getBirthdate(), "", livMem.getParamaters().get(0) + ", " + livMem.getParamaters().get(1), "", ""} , false);
                    // If there is a spouse but no children
                    else if(livMem.getChildren() == null || livMem.getChildren().isEmpty())
                        Software.getMYGUI().updateSelectedInformation(new String[]{ livMem.getName(), livMem.getBirthdate(), "", livMem.getParamaters().get(0) + ", " + livMem.getParamaters().get(1), livMem.getSpouse().getName(), ""} , false);
                    // If there is children but no spouse
                    else if(!livMem.hasSpouse())
                        Software.getMYGUI().updateSelectedInformation(new String[]{ livMem.getName(), livMem.getBirthdate(), "", livMem.getParamaters().get(0) + ", " + livMem.getParamaters().get(1), "", children.substring(1, children.length() - 1)} , false);                    
                    // If there is a spouse and children
                    else 
                        Software.getMYGUI().updateSelectedInformation(new String[]{ livMem.getName(), livMem.getBirthdate(), "", livMem.getParamaters().get(0), livMem.getSpouse().getName(), children.substring(1, children.length() - 1)} , false);
        
                }
            }
        }
        map.repaint();
    }
    
}
