/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carschema;

import static carschema.ConnectToDatabase.TryExecutingAStoredProcedure;
import static carschema.theGUI.con;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mark Case
 */
public class Main {
        public static void main(String[] args) throws SQLException {
        // user credentials
        String userName = "root"; // your username goes here
        String password = "Bern2002!"; // your password goes here
        // database name
        String databaseName = "carschema";
        // set connection equal to fields
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, userName, password);
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(theGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(theGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(theGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(theGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form for GUI */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                theGUI gui = new theGUI();
                gui.setVisible(true);
                gui.setTitle("BMW Forum");
            }
        });
         
        if (TryExecutingAStoredProcedure("carschema", userName, password))
        {
            System.out.println("Successfully connected to database");
        } else {
            System.out.println("Failed to connect to database");
        }
    } // end main
}
