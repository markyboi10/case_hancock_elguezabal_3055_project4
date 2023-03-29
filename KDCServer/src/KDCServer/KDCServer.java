package KDCServer;

/**
 *
 * @author Mark Case
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.File;
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

    //private static File secretsFile = new File(System.getProperty("user.home") + File.separator + "case_hancock_elguezabal_3055_project4-master\\test-data\\kdc-config\\secrets.json");
    private static File secretsFile = new File("C:\\Users\\willi\\Documents\\NetBeansProjects\\case_hancock_elguezabal_3055_project4\\kdc-config\\secrets.json");
    private static File hostsFile = new File("C:\\Users\\willi\\Documents\\NetBeansProjects\\case_hancock_elguezabal_3055_project4\\hosts.json");

    public static JsonNode JSONSecrets() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(secretsFile);
        JsonNode secretsNode = rootNode.get("secrets");
        return secretsNode;

    }

    public static JsonNode JSONHosts() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(hostsFile);
        JsonNode hostsNode = rootNode.get("hosts");
        return hostsNode;

    }

    //System.out.println(JSON().toString());
    // private static Config config;
    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidObjectException, IOException {

//        OptionParser op = new OptionParser(args);
//        LongOption[] ar = new LongOption[2];
//        ar[0] = new LongOption("config", true, 'c');
//        ar[1] = new LongOption("help", false, 'h');
//        op.setLongOpts(ar);
//        op.setOptString("hc:");
//        Tuple<Character,String> opt = op.getLongOpt(false);
//        if (opt == null || Objects.equals(opt.getFirst(), 'h')) {
//            System.out.println("usage:\n"
//                    + "kdcd\n"
//                    + " kdcd --config <configfile>\n"
//                    + " kdcd --help\n"
//                    + "options:\n"
//                    + " -c, --config Set the config file.\n"
//                    + " -h, --help Display the help.");
//            System.exit(0);
//        } else if (Objects.equals(opt.getFirst(), 'c')) {
//            // Initialize config
//            config = new Config(opt.getSecond());
//        }
        String host = null;
        int port = 0;

        for (JsonNode hostNode : JSONHosts()) {
            String hostName = hostNode.get("host-name").asText();
            // Check if the current user is the one you're looking for
            if (hostName.equals("kdcd")) {
                host = hostNode.get("host-name").asText();
                port = 5000;
                // send.println("Enter your password below. Yout nonce is: " + nonce);
                System.out.println(host);
            }
        }

        try {
            System.out.println(port);
//            ServerSocket server = new ServerSocket(config.getPort());
            ServerSocket server = new ServerSocket(port);

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
                            sendSessionKey("", "");
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
                sock.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    //yes this code doesn't work, I just started it -william
    //this is the part where session key is sent to client 
    private static void sendSessionKey(String uname, String sName){
        //validity period comes from config file  
        Ticket toSend = new Ticket(System.currentTimeMillis(), 0, uname, sName);
        
        
    }

}
