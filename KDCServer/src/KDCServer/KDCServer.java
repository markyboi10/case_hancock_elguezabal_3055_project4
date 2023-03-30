package KDCServer;

/**
 *
 * @author Mark Case, William Hancock
 */
import KDCServer.config.Config;
import KDCServer.config.Secrets;
import KDCServer.config.SecretsConfig;
import KDCServer.crypto.GCMEncrypt;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.util.NonceCache;
import merrimackutil.util.Tuple;
import packets.SessionKeyResponse;

public class KDCServer {

//    private static String[] userAndPass = {"Alice", "123321"};
    
    public static ArrayList<Secrets> secrets = new ArrayList<>();
    private static SecretsConfig secretsConfig;
    private static String user;
    private static String secret;

    private static ServerSocket server;
    
    //private static File secretsFile = new File(System.getProperty("user.home") + File.separator + "case_hancock_elguezabal_3055_project4-master\\test-data\\kdc-config\\secrets.json");
    private static File secretsFile = new File("C:\\Users\\willi\\Documents\\NetBeansProjects\\case_hancock_elguezabal_3055_project4\\kdc-config\\secrets.json");

    //System.out.println(JSON().toString());
    private static Config config;
    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidObjectException, IOException {     
        
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
           // Initialize the Secrets config from the path "secrets_file" of config.
           secretsConfig = new SecretsConfig(config.getSecrets_file());
        }
       

        try {
            System.out.println(config.getPort());
            System.out.println("Amount of Secrets " + secrets.size());
            // Initializie the server with the config port
            server = new ServerSocket(config.getPort());

            // Accept packets & communicate
            poll();
            
            // Loop forever handing connections.
            /**
            while (true) {
                

                /**
                // Random nonce       
                NonceCache nc = new NonceCache(32, 30);
                byte[] nonceBytes = nc.getNonce();
                String nonce = Base64.getEncoder().encodeToString(nonceBytes);

                // Get the username line from the client.
                String line = recv.nextLine();

                // Check if user exists and demand password + send nonce if correct,
                // error otherwise
                
                for (JsonNode secretNode : JSONSecrets()) {
                    String userName = secretNode.get("user").asText();
                    // Check if the current user is the one you're looking for
                    if (userName.equals(line)) {
                        String password = secretNode.get("secret").asText();
                        // send.println("Enter your password below. Yout nonce is: " + nonce);
                        send.println(password + " your nonce is: " + nonce);
                        //https://stackoverflow.com/questions/5683486/how-to-combine-two-byte-arrays
                        // Get hashed pass and nonce from client, compare and validate to kdc version
                        String line2 = recv.nextLine();
                        send.println("Checking hash . . .");
                        MessageDigest digest = MessageDigest.getInstance("SHA-256");
                        byte[] hashPass = password.getBytes(StandardCharsets.UTF_8);
                        byte[] hashNonce = nonce.getBytes(StandardCharsets.UTF_8);
                        byte[] combined = new byte[hashPass.length + hashNonce.length];
                        System.arraycopy(hashPass, 0, combined, 0, hashPass.length);
                        System.arraycopy(hashNonce, 0, combined, hashPass.length, hashNonce.length);
                        combined = digest.digest(combined);
                        if (line2.equals(Arrays.toString(combined))) {
                            send.println("You have been authenticated");
                            sendSessionKey("", "", password);
                        } else {
                            send.println("You have denied");
                        }
                        break;
                    } else {
                        send.println("User Error, name not found");
                        System.exit(0);
                    }
                }

                // Close the connection.
            }**/
        } catch (IOException ioe) {
            ioe.printStackTrace();
            server.close();
            System.out.println("KDC Server IOException error, closing down.");
            System.exit(0);
        }
        
    }
    
    private static void poll() throws IOException {
        while(true) { // Consistently accept connections
            
            // Establish the connection & read the message
            Socket peer = server.accept();
            // Determine the packet type.
           
            
            
            // Close the connection
            server.close();
        }
    }
    
    
    
    //this is the part where session key is sent to client 
    private static void sendSessionKey(String uname, String sName, String pw){
        //validity period comes from config file  
        SessionKeyResponse toSend = new SessionKeyResponse(System.currentTimeMillis(), 0, uname, sName);
        try {
            Tuple<byte[], byte[]> keyiv = GCMEncrypt.encrypt(pw, toSend.getValidityTime(), toSend.getCreateTime(), uname, sName);
            toSend.seteSKey(keyiv.getFirst());
            toSend.setIv(keyiv.getSecond());
            //now we send!
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException | InvalidKeySpecException ex) {
            Logger.getLogger(KDCServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
