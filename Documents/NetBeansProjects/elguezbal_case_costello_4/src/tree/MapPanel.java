package tree;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.JPanel;

/**
 * Maps out and displays the contents of our selected family tree.
 *
 * @author Derek
 */
public class MapPanel extends JPanel {
    
    private FamilyMember selectedMember;
    private SelectionHandler selectionHandler;
    private FamilyTree familyTree;
    private int width;
    private int height;
    private ArrayList<ArrayList<FamilyMember>> famGraph;
    
    /**
     * Default constructor that initializes our Map, and sets the FamilyTree.
     * 
     * @param tree family tree we are displaying
     */
    public MapPanel(FamilyTree tree) {
        this.setLayout(null);
        this.setBackground(new Color(231,232,209));
        
        familyTree = tree;
        famGraph = new ArrayList<>();
        selectedMember = familyTree.getClient();
        selectionHandler = new SelectionHandler(this);
        
        init();
    }
    
    /**
     * Draws the tree to this panel's graphics context.
     * 
     * @param g graphics context of this component
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
                       
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(4.0F)); // set width of lines
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //smoothen lines
        // Sets everyone unvisited
        getFamilyTree().setAllUnvisited();
        
        int levelnum = 0;
        
        // Goes over each level
        for(ArrayList<FamilyMember> level : famGraph) {
            // Goes over the family members in a level
            for(FamilyMember n : level) {
                n.setBackground(new Color(184, 183, 180));
                // Draw line to children :
                /*
                    1. If child's parents are also each others' spouses, draw a line in the middle of the parents to the children
                    2. Else, draw directly to the children
                */
                
                // Drawing lines to children
                for(FamilyMember child : n.getChildren()) {
                    List<FamilyMember> parents = child.getParents();
                    // Check for married parents with children
                    if(parents != null && parents.size() == 2 && parents.get(0).getSpouse().equals(parents.get(1))) {
                        g.drawLine(
                                ((int) ((parents.get(0).getCorner().getX() + parents.get(0).getWidth()) + parents.get(1).getCorner().getX()) / 2), 
                                ((int) parents.get(0).getCorner().getY()) + (parents.get(0).getHeight() / 2), 
                                ((int) child.getCorner().getX() + (child.getWidth() / 2)),
                                ((int) child.getCorner().getY())
                        );
                    } else { // If the parents are not married, draw normally
                        g.drawLine(
                                ((int) n.getCorner().getX()) + (n.getWidth() / 2), 
                                ((int) n.getCorner().getY()) + n.getHeight(), 
                                ((int) child.getCorner().getX()) + (child.getWidth() / 2), 
                                ((int) child.getCorner().getY())
                        );
                    }
                }
                
                // Does drawing to spouse
                if(n.getSpouse() != null && !n.isVisited() && !n.getSpouse().isVisited()) {
                                        
                    FamilyMember spouse = n.getSpouse();
                    
                    if(spouse.getCorner().getX() < n.getCorner().getX()) continue;
                    
                    g.drawLine(((int) n.getCorner().getX()) + n.getWidth(), ((int) n.getCorner().getY()) + (n.getHeight() / 2), 
                               ((int) spouse.getCorner().getX()), ((int) spouse.getCorner().getY()) + (spouse.getHeight() / 2));
                    
                    // Sets both people visited
                    n.setVisited(true);
                    spouse.setVisited(true);
                    
                }                 
            }
            levelnum++;
            // Sets everyone unvisited on different levels
            getFamilyTree().setAllUnvisited();
        }
        // Again sets everyone unvisited.
        getFamilyTree().setAllUnvisited();
        
        /*
        // After they are drawn, draw relationships.
        famGraph.forEach(level -> { // For each level
            level.forEach(m -> { // For each family member
                if(m.hasSpouse()) // If they have a spouse, draw a line connecting them
                    paintLine(g, m, m.getSpouse());
                List<FamilyMember> children = m.getChildren();
                if(!children.isEmpty()) // If they have children, draw a line to each child
                    children.forEach(c -> {
                        paintLine(g, m, c);
                    });
            });
        });
        
        */
        
    }
    
    /**
     * @return the familyTree
     */
    public FamilyTree getFamilyTree() {
        return familyTree;
    }
    
    /**
     * @return the selectedMember
     */
    public FamilyMember getSelectedMember() {
        return selectedMember;
    }

    /**
     * @param selectedMember the selectedMember to set
     */
    public void setSelectedMember(FamilyMember selectedMember) {
        this.selectedMember = selectedMember;
    }
    
    
    /*
        Private Methods
    */
    
    private void init() {
        // Builds the boundaries for the MapPanel based on the family members in the tree
        // Also takes care of assigning levels, sorting the graph into our 2D array, and assigning the bounds
        findBounds();
        // Assigns locations for the family members to go when drawn
        assignLocations();
    }
    
    /**
     * Draws a line from the center of one family member to another.
     * 
     * @param g graphics context to drawn on
     * @param startMem starting family member
     * @param endMem destination family member
     */
    private void paintLine(Graphics g, FamilyMember startMem, FamilyMember endMem) {
        Point start = getCenter(startMem);
        Point end = getCenter(endMem);
        
        // Changes the color and draws the line
        g.setColor(Color.ORANGE);
        g.drawLine(start.x, start.y, end.x, end.y);
    }
    
    /**
     * Gets the center of a family member component.
     * 
     * @param m family member component
     * @return point at the center of the component
     */
    private Point getCenter(FamilyMember m) {
        Dimension mDim = m.getPreferredSize();
        return new Point(mDim.width / 2, mDim.height / 2);
    }
    
    /**
     * Draw the tree.
     */
    private void assignLocations() {
        // Average the height of the panel by the number of levels to determine height intervals
        int heightInterval = height / famGraph.size(); 
        // Draw each level
        for(int h = 0; h < famGraph.size(); h++) {
            ArrayList<FamilyMember> level = famGraph.get(h);
            // Average the width of the panel by the number of family members on this level to determine width intervals
            int widthInterval = width / level.size();
            // Draw each family member within the level
            for(int w = 0; w < level.size(); w++) { 
                FamilyMember m = level.get(w);
                m.setCorner(new Point(w * widthInterval, h * heightInterval));
                m.setMap(this);
                m.addMouseListener(selectionHandler);
            }
        }
    }
    
    /**
     * Using the widest level / the levels of the tree for scalability, determine the bounds.
     */
    private void findBounds() {
        List<FamilyMember> members = getFamilyTree().getFamilyMembers();
        // Add family members 
        int memHeight = determineLevels(members); // Number of levels (height of tree in terms of members)
        width = getMaxWidth(); // Width based on max number of family members at any level (width)
        
        // Random family member to get preferred size values for scaling
        FamilyMember m = members.get(0);
        // height = # of levels * (height of the member object at every level + space between each level) + space at the top [padding; bottom already covered in previous step]
        height = memHeight * (m.getSize().height + 50) + 50;
        // Set the bounds
        this.setPreferredSize(new Dimension(width, height));
    }
    
    /**
     * Find the levels that each family member is on (what generation), and then add them to
     * the graph according to their level. Return the number of levels (height of the tree).
     * 
     * @param members family members
     * @return the number of levels (height of the tree)
     */
    private int determineLevels(List<FamilyMember> members) {
        // Get the client and start our level search from them
        FamilyMember client = getFamilyTree().getClient();
        client.setLevel(0); // Client starts at level "0"
        allocateLevels(client); // Allocate levels to each member based on what level 'client' is (children are - levels, parents are + levels)
        // Find the difference (the absolute value of the lowest level is used as the difference so that the lowest level is indexed at 0 in the ArrayList)
        int difference = Math.abs(findLowestLevel());
        int highestLevel = findHighestLevel();
        // Create the list levels (based on the height of the tree; +1 for including client)
        createListLevels(highestLevel, difference);
        fillListLevels(highestLevel, difference); // Fill the list levels
        
        return famGraph.size(); // Return the height of our tree
    }
    
    /**
     * Finds the maximum amount of family members at any level. This helps us determine the width of our tree.
     * 
     * @return maximum amount of family members at any level
     */
    private int getMaxWidth() {
        ArrayList<FamilyMember> maxLevel = new ArrayList<>(); // Stores reference to level with max amount of family members
        for(ArrayList<FamilyMember> level : famGraph) { // For every level of the tree
            // If the number of family members at this level is > max
            if (level.size() > maxLevel.size()) {
                maxLevel = level; // Make it the new max
            }
        }
        
        // Calculate width based on maxLevel
        int w = 0; // Store width
        for(int i = 0; i < maxLevel.size(); i++) {
            // Add the width of the component (+50 for padding) to the cumulative width
            w += maxLevel.get(i).getPreferredSize().width + 50;
        }
        return w;
    }
    
    /**
     * Performs a breadth-first search to gather levels of our tree.
     * 
     * @param client 
     */
    private void allocateLevels(FamilyMember client) {
        Queue<FamilyMember> q = new LinkedList<>();
        q.add(client);
        // BFS functionality
        while(!q.isEmpty()) {
            // Pop the queue to get the latest family member iteration
            FamilyMember m = q.peek();
            q.remove();
            // Set the current family member as visited
            m.setVisited(true);
            List<FamilyMember> parents = m.getParents();
            List<FamilyMember> children = m.getChildren();
            // If this family member has parents
            if(!parents.isEmpty()) {
                for(int i = 0; i < parents.size(); i++) {
                    FamilyMember p = parents.get(i);
                    if(!p.isVisited()) { // For every unvisited parent
                        q.add(p); // Add to the queue for processing
                        p.setLevel(m.getLevel() + 1); // Set their level
                    }
                }
            }
            // If this family member has children
            if(!children.isEmpty()) {
                for(int i = 0; i < children.size(); i++) {
                    FamilyMember c = children.get(i);
                    if(!c.isVisited()) { // For every unvisited parent
                        q.add(c); // Add to the queue for processing
                        c.setLevel(m.getLevel() - 1); // Set their level
                    }
                }
            }
            // If this family member has a spouse
            if(m.hasSpouse() && !m.getSpouse().isVisited()) {
                FamilyMember s = m.getSpouse();
                q.add(s);
                s.setLevel(m.getLevel());
            }
        }
        // Reset visitations
        getFamilyTree().setAllUnvisited();
    }
    
    /**
     * Creates the list's levels.
     * 
     * @param highestLevel highest level in our tree
     * @param diff difference determined by depth of lowest levels
     */
    private void createListLevels(int highestLevel, int diff) {
        // Fill in the list levels with empty ArrayLists so data can be packaged.
        // Size is determined by the height of the tree (the highest level plus the bottom levels' depth, plus the middle level, 0)
        for(int i = 0; i < highestLevel + diff + 1; i++)
            famGraph.add(new ArrayList<>());
    }
    
    /**
     * Fill our 2D ArrayList (representing levels and family members in our graph) to match
     * the family members and levels of each family member.
     */
    private void fillListLevels(int topmostLevel, int diff) {
        // Try this way:
        // Add to top level:
        ArrayList<FamilyMember> topLevel = famGraph.get(0); // Get the topmost level by adding the difference from negative levels
        List<FamilyMember> famMembers = getFamilyTree().getFamilyMembers();
        for(int i = 0; i < famMembers.size(); i++) { // Check every family member
            FamilyMember m = famMembers.get(i);
            if(m.getLevel() != null && m.getLevel() == topmostLevel && !topLevel.contains(m)) { // If they are in the topmost level (grandest parent), and aren't already added
                topLevel.add(m); // Add them to the topLevel
                if(m.hasSpouse()) // If they have a spouse, add the spouse right after
                    topLevel.add(m.getSpouse());
            }
        }
        // Add to every level after the top level
        getNextLevels(topLevel);
    }
    
    /**
     * Recursively sort the next levels.
     * 
     * @param currLevel current level we are testing for children for
     */
    private void getNextLevels(ArrayList<FamilyMember> currLevel) {
        ArrayList<FamilyMember> nextLevel = famGraph.get(famGraph.indexOf(currLevel) + 1); // Reference of the next level
        for(int i = 0; i < currLevel.size(); i++) { // For everyone in the current level
            FamilyMember m = currLevel.get(i);
            if(!m.getChildren().isEmpty()) { // If they have children
                checkChildren(m, currLevel, nextLevel);
            } // Otherwise continue to the next person
        }
        if(famGraph.indexOf(nextLevel) == famGraph.size() - 1) // If we filled the last level, no need to continue
            return;
        else
            getNextLevels(nextLevel); // Otherwise, rinse and repeat for the next level
    }
    
    /**
     * Check if the children already exist, and if they don't, add them.
     * 
     * @param m member who's children we are checking
     * @param currLevel current level of the tree we're iterating through
     * @param nextLevel next level to add children to
     */
    private void checkChildren(FamilyMember m, ArrayList<FamilyMember> currLevel, ArrayList<FamilyMember> nextLevel) {
        List<FamilyMember> children = m.getChildren();
        for(int j = 0; j < children.size(); j++) { // Go through every child
            FamilyMember c = children.get(j);
            if(!nextLevel.contains(c)) { // If they don't already exist
                checkDivorcedParents(c, currLevel);
                nextLevel.add(c); // Add them
                if(c.hasSpouse()) // If they have a spouse, add them adjacent to this child
                    nextLevel.add(c.getSpouse());
            }
        }
    }
    
    /**
     * Check if the child's parents are divorced (no parents / no spouses in the graph)
     * 
     * @param c child we are checking parents for
     * @param currLevel current level of the tree we're iterating through (will place parent here)
     */
    private void checkDivorcedParents(FamilyMember c, ArrayList<FamilyMember> currLevel) {
        if(!c.getParents().isEmpty()) { // Check for divorced parents
            for(int k = 0; k < c.getParents().size(); k++) {
                FamilyMember p = c.getParents().get(k); // For each parent of the child
                if(!currLevel.contains(p)) { // If they aren't already there
                    currLevel.add(p); // Add them (catches divorced cases)
                }
            }
        }
    }
    
    /**
     * @return lowest level in our tree
     */
    private int findLowestLevel() {
        int lowest = 0; // Holds lowest value
        List<FamilyMember> members = getFamilyTree().getFamilyMembers();
        for(int i = 0; i < members.size(); i++) { // Iterate through the family members and find the lowest level
            FamilyMember m = members.get(i);
            if(m.getLevel() != null && m.getLevel() < lowest)
                lowest = m.getLevel();
        }
        // After the lowest level is found, return it
        return lowest;
    }
    
    /**
     * @return highest level in our tree
     */
    private int findHighestLevel() {
        int highest = 0; // Holds lowest value
        List<FamilyMember> members = getFamilyTree().getFamilyMembers();
        for(int i = 0; i < members.size(); i++) { // Iterate through the family members and find the highest level
            FamilyMember m = members.get(i);
            if(m.getLevel() != null && m.getLevel() > highest)
                highest = m.getLevel();
        }
        // After the lowest level is found, return it
        return highest;
    }

}
