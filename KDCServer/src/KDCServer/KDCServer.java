/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package KDCServer;

/**
 *
 * @author Mark Case
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import merrimackutil.util.NonceCache;

public class KDCServer {

    private static String[] userAndPass = {"Alice", "123321"};

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(Arrays.toString(userAndPass));
 
        try {
            ServerSocket server = new ServerSocket(5000);

            // Loop forever handing connections.
            while (true) {
                // Wait for a connection.
                Socket sock = server.accept();

                System.out.println("Connection received.");

                // Setup the streams for use.
                Scanner recv = new Scanner(sock.getInputStream());
                PrintWriter send = new PrintWriter(sock.getOutputStream(), true);

                // Random nonce       
                NonceCache nc = new NonceCache(32, 30);
                byte[] nonceBytes = nc.getNonce();
                String nonce = Base64.getEncoder().encodeToString(nonceBytes);
                
                // Get the username line from the client.
                String line = recv.nextLine();

                // Check if user exists and demand password + send nonce if correct,
                // error otherwise
                if(line.equals(userAndPass[0])) {
                    send.println("Enter your password below. Your nonce is: " + nonce);
                } else {
                    send.println("User Error, name not found");
                    System.exit(0);
                }
              
                // Get hashed pass and nonce from client, compare and validate to kdc version
                String line2 = recv.nextLine();
                String line3 = recv.nextLine();
                send.println("Checking hash . . .");
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hashPass = digest.digest(userAndPass[1].getBytes(StandardCharsets.UTF_8));
                byte[] hashNonce = digest.digest(nonce.getBytes(StandardCharsets.UTF_8));
                if(line2.equals(Arrays.toString(hashPass)) && line3.equals(Arrays.toString(hashNonce))) {
                    send.println("You have been authenticated");
                } else {
                    send.println("You have denied");
                }

                // Close the connection.
                sock.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
