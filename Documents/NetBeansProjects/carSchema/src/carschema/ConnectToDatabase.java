package carschema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Mark Case
 */
public class ConnectToDatabase {
    
    /*
    Open connection to mysql database on local machine
    */
    public static Connection getMySQLConnection(String databaseName, String user, String password) throws SQLException {
        
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, user, password);

        try
        {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, user, password);
        } catch (Exception exception)
        {
            System.out.println("Failed to connect to the database" + exception.getMessage());
            return null;
        }
    } // end getMySQLConnection

    /*
    Execute stored procedure method
    Takes in parameters
    */
    public static boolean TryExecutingAStoredProcedure(String databaseName, String user, String password) throws SQLException {
        return true;
    }
    
}
