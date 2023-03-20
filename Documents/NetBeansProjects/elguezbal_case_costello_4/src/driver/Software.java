package driver;

import data.Database;
import data.FileManager;
import gui.myGUI;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import tree.FamilyTree;
import tree.MapManager;

/**
 * Driver class for our family tree software
 * @author Alex, Derek, Mark
 */
public class Software {

    private static FileManager fileManager;
    private static Database database;
    private static MapManager mapManager;
    
    // Frame object
    private static myGUI myGUI;
    
    /*
    //  Main Method for testing files and loading the project
    public static void main(String[] args) {
       
        // Creates and loads the files and data
        fileManager = new FileManager();
        database = new Database();
        mapManager = new MapManager();
        
        // Loads all the trees and clients in
        database.initilize();
        
        // Frame object
        SwingUtilities.invokeLater(() -> {
            //myGUI = new myGUI();
            //myGUI.setVisible(true);
            
            JFrame frame = new JFrame();
            frame.setLayout(new GridLayout(3,15));
            frame.setSize(1700, 1000);
            frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            
             // Loads all the default family members in
             database.getFamilyTree("sampleInput2").getFamilyMembers().forEach(n -> {
                frame.add(n);
              });

             frame.setVisible(true);
             
             getDatabase().saveTree("sampleInput2");
        });
       
    }
    
    */

    
    
    // Main method for the GUI currently
    public static void main(String args[]) {
        
        // Managers
        // Creates and loads the files and data
        fileManager = new FileManager();
        database = new Database();
        mapManager = new MapManager();
        
        // Loads all the trees and clients in
        database.initilize();
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(myGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(myGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(myGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(myGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                myGUI = new myGUI();
                myGUI.setVisible(true);
                myGUI.setTitle("Finn's Family Tree");
                //new myGUI().setVisible(true);
                
                /*JPanel testPanel = new JPanel();
                testPanel.setSize(700, 300);
                testPanel.setLayout(new GridLayout(3,15));

                database.getFamilyTree("sampleInput2").getFamilyMembers().forEach(n -> {
                 testPanel.add(n);
                });
        
                myGUI.displayTreePanel(testPanel);*/
                //FamilyTree tree = database.getFamilyTree("sampleInput2");
                //mapManager.assignMap(tree);
                
                myGUI.setUpAddTreesComboBox(database.getFamilyTrees());
            }
        });
        
    } // End main
    


    /**
     * @return the fileManager
     */
    public static FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @return the database
     */
    public static Database getDatabase() {
        return database;
    }
    
    /**
     * @return the frame
     */
    public static myGUI getMYGUI() {
        return myGUI;
    }

    /**
     * @return the mapManager
     */
    public static MapManager getMapManager() {
        return mapManager;
    }
    
}
