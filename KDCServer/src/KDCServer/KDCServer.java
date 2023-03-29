package KDCServer;

/**
 *
 * @author Mark Case
 */
import KDCServer.config.Config;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.util.NonceCache;
import merrimackutil.util.Tuple;

public class KDCServer {

    private static String[] userAndPass = {"Alice", "123321"};
    
    private static Config config;

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidObjectException {
        OptionParser op = new OptionParser(args);
        LongOption[] ar = new LongOption[2];
        ar[0] = new LongOption("config", true, 'c');
        ar[1] = new LongOption("help", false, 'h');
        op.setLongOpts(ar);
        op.setOptString("hc:");
        Tuple<Character,String> opt = op.getLongOpt(false);
        if (opt == null || Objects.equals(opt.getFirst(), 'h')) {
            System.out.println("usage:\n"
                    + "kdcd\n"
                    + " kdcd --config <configfile>\n"
                    + " kdcd --help\n"
                    + "options:\n"
                    + " -c, --config Set the config file.\n"
                    + " -h, --help Display the help.");
            System.exit(0);
        } else if (Objects.equals(opt.getFirst(), 'c')) {
            // Initialize config
            config = new Config(opt.getSecond());
        }
        
        System.out.println(Arrays.toString(userAndPass));
 
        try {
            ServerSocket server = new ServerSocket(config.getPort());

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
