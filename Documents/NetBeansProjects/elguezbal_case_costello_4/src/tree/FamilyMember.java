package tree;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import javax.swing.*;

/**
 * Class for a Family Member
 * @author Alex, Derek, Mark
 */
public abstract class FamilyMember extends JPanel implements Readable {
    
    private String name;
    private String birthdate;
    private List<FamilyMember> parents;
    private List<FamilyMember> children;
    private FamilyMember spouse;
    private MapPanel map;
    
    // Used for traversing the graph
    private boolean isVisited = false;
    private Integer level = null;
    
    public FamilyMember(String name, String birthdate, List<FamilyMember> parents, List<FamilyMember> children, FamilyMember spouse) {
        this.name = name;
        this.birthdate = birthdate;
        this.parents = parents;
        this.children = children;
        this.spouse = spouse;
        this.map = null;
        
        init();
    }
    
    /**
     * Adds in a parent
     * @param parent FamilyMember for parent
     */
    public void addParent(FamilyMember parent) {
        if(parent != null) parents.add(parent);
    }
    
    /**
     * Removes a parent
     * @param parent FamilyMember for parent
     */
    public void removeParent(FamilyMember parent) {
        if(parent == null) return;
        
        // family member to be removed
        FamilyMember toRemove = null;
        
        // Loops through all the family members
        for(FamilyMember n : parents) {
            // If the parent is found adds him to be removed
            if(n.getName().equalsIgnoreCase(parent.getName())) toRemove = n;
        }
        
        // Removes the parent
        if(toRemove != null)
            parents.remove(toRemove);
    }
    
    public void addChild(FamilyMember child) {
        if(child != null) children.add(child);
    }
    
    public void removeChild(FamilyMember child) {
        if(child == null) return;
        
        // family member to be removed
        FamilyMember toRemove = null;
        
        // Loops through all the family members
        for(FamilyMember n : children) {
            // If the parent is found adds him to be removed
            if(n.getName().equalsIgnoreCase(child.getName())) toRemove = n;
        }
        
        // Removes the parent
        if(toRemove != null)
            children.remove(toRemove);
    }
    
    public void setSpouse(FamilyMember spouse) {
        if(spouse != null) this.spouse = spouse;
    }
    
    public void removeSpouse() {
        // Sets the current spouse to null
        setSpouse(null);
    }
    
    public void displayPersonalInfo() {
        // ToDo..
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the birthdate
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * @return the parents
     */
    public List<FamilyMember> getParents() {
        return parents;
    }

    /**
     * @return the children
     */
    public List<FamilyMember> getChildren() {
        return children;
    }

    /**
     * @return the spouse
     */
    public FamilyMember getSpouse() {
        return spouse;
    }
    
    /**
     * @return the isVisited
     */
    public boolean isVisited() {
        return isVisited;
    }

    /**
     * @param isVisited the isVisited to set
     */
    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }
    
    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }
    
    /**
     * Sets the reference to our map panel.
     * 
     * @param map reference to the map panel
     */
    public void setMap(MapPanel map) {
        this.map = map;
        map.add(this);
    }
    
    /**
     * Returns the name of this family member
     * 
     * @return 
     */
    @Override
    public String toString() {
        return name;
    }
    
    public boolean hasSpouse() {
        return getSpouse() != null;
    }
    
    /**
     * Used for comparing Family members
     * @param anObject
     * @return 
     */
    @Override
    public boolean equals(Object anObject) {    
      if (this == anObject) {    
          return true;  
      }
      
      if(anObject instanceof String) return this.name.equalsIgnoreCase((String)anObject);
      
      if(!(anObject instanceof FamilyMember)) return false;    
          
      return this.name.equalsIgnoreCase(((FamilyMember)anObject).getName());
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    /**
     *  GUI Stuff
     */
    
    private static ImageIcon profilePicture;
    
    private int panWidth; // 20 pixels for every character
    private final int panHeight = 40;
    private Point corner; // Top left corner
    
    private JLabel nameLabel;
    
    private static final Color selectedColor = new Color(115, 165, 245);
       
    private void init() {
        
        // Layout
        setLayout(new FlowLayout());
                
        // Size
        panWidth = name.length() * 10; // 20 pixels per character in the name
        setSize(panWidth, panHeight);
        setPreferredSize(new Dimension(panWidth, panHeight));
        
        // Position
        corner = new Point(); // initialized at 0,0 until changed
        
        // Attributes
        this.nameLabel = new JLabel(name);
        this.nameLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        
        // Adding attributes
        add(this.nameLabel);
    }
    
    /**
     * @return the top left corner
     */
    public Point getCorner() {
        return corner;
    }
    
    /**
     * Sets the corner
     * 
     * @param corner sets to a new corner
     */
    public void setCorner(Point corner) {
        this.corner = corner;
        this.setBounds((int)corner.getX(), (int)corner.getY(), panWidth, panHeight); // Set the bounds accordingly
    }
    
    /**
     * Removes this FamilyMember from the map.
     */
    public void removeFromMap() {
        map.remove(this);
    }
    
    /**
     * @return preferred size of this family member
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(panWidth, panHeight);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
        if(this.equals(map.getSelectedMember())) { // If it is selected, set a blue border
            // Source for using stroke to increase thickness: https://stackoverflow.com/questions/4219511/draw-rectangle-border-thickness
            g2.setColor(selectedColor);
            
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(5));
            g2.drawRect(0, 0, panWidth-1, panHeight-1);
            g2.setStroke(oldStroke);
        } else {
            g2.drawRect(0, 0, panWidth-1, panHeight-1);
        }
    }
    
}
