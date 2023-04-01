package KDCServer;

/**
 *
 * @author Mark Case, William Hancock
 */
import KDCServer.config.Config;
import KDCServer.config.Secrets;
import KDCServer.config.SecretsConfig;
import KDCServer.crypto.GCMEncrypt;
import communication.Communication;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
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
import packets.CHAPChallenge;
import packets.CHAPClaim;
import packets.CHAPResponse;
import packets.CHAPStatus;
import packets.Packet;
import static packets.PacketType.CHAPResponse;
import packets.SessionKeyRequest;
import packets.SessionKeyResponse;

public class KDCServer {

    public static ArrayList<Secrets> secrets = new ArrayList<>();
    private static SecretsConfig secretsConfig;
    private static Config config;

    private static ServerSocket server;
    //private static String nonce;
    //private static byte[] nonceBytes;
    
    //public static byte[] noncc = null;

    //private static File secretsFile = new File("C:\\Users\\willi\\Documents\\NetBeansProjects\\case_hancock_elguezabal_3055_project4\\kdc-config\\secrets.json");
    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidObjectException, IOException {

        OptionParser op = new OptionParser(args);
        LongOption[] ar = new LongOption[2];
        ar[0] = new LongOption("config", true, 'c');
        ar[1] = new LongOption("help", false, 'h');
        op.setLongOpts(ar);
        op.setOptString("hc:");
        Tuple<Character, String> opt = op.getLongOpt(false);
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

//            NonceCache nc = new NonceCache(32, 30);
//            byte[] nonceBytes = nc.getNonce();
//            nonce = Base64.getEncoder().encodeToString(nonceBytes);
            // Loop forever handing connections.
            /**
             * while (true) {
             *
             *
             * /**
             * // Random nonce NonceCache nc = new NonceCache(32, 30); byte[]
             * nonceBytes = nc.getNonce(); String nonce =
             * Base64.getEncoder().encodeToString(nonceBytes);
             *
             * // Get the username line from the client. String line =
             * recv.nextLine();
             *
             * // Check if user exists and demand password + send nonce if
             * correct, // error otherwise
             *
             * for (JsonNode secretNode : JSONSecrets()) { String userName =
             * secretNode.get("user").asText(); // Check if the current user is
             * the one you're looking for if (userName.equals(line)) { String
             * password = secretNode.get("secret").asText(); //
             * send.println("Enter your password below. Yout nonce is: " +
             * nonce); send.println(password + " your nonce is: " + nonce);
             * //https://stackoverflow.com/questions/5683486/how-to-combine-two-byte-arrays
             * // Get hashed pass and nonce from client, compare and validate to
             * kdc version String line2 = recv.nextLine();
             * send.println("Checking hash . . ."); MessageDigest digest =
             * MessageDigest.getInstance("SHA-256"); byte[] hashPass =
             * password.getBytes(StandardCharsets.UTF_8); byte[] hashNonce =
             * nonce.getBytes(StandardCharsets.UTF_8); byte[] combined = new
             * byte[hashPass.length + hashNonce.length];
             * System.arraycopy(hashPass, 0, combined, 0, hashPass.length);
             * System.arraycopy(hashNonce, 0, combined, hashPass.length,
             * hashNonce.length); combined = digest.digest(combined); if
             * (line2.equals(Arrays.toString(combined))) { send.println("You
             * have been authenticated"); sendSessionKey("", "", password); }
             * else { send.println("You have denied"); } break; } else {
             * send.println("User Error, name not found"); System.exit(0); } }
             *
             * // Close the connection.
            }*
             */
        } catch (IOException ioe) {
            ioe.printStackTrace();
            server.close();
            System.out.println("KDC Server IOException error, closing down.");
            System.exit(0);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            server.close();
            System.out.println("KDC Server IOException error, closing down.");
            System.exit(0);
        }

    }
    
    private static NonceCache nc = new NonceCache(32,30);
    private static ArrayList<byte[]> nonceList = new ArrayList<>();

    // Gobal noncecache, holding nonces.
    private static NonceCache nc = new NonceCache(32, 30);
    
    /**
     * Waits for a connection with a peer socket, then polls for a message being
     * sent. Each iteration of the loop operates for one message, as not to
     * block multi-peer communication.
     *
     * @throws IOException
     */
    private static void poll() throws IOException, NoSuchMethodException, NoSuchAlgorithmException {
        while (true) { // Consistently accept connections

            // Establish the connection & read the message
            Socket peer = server.accept();

            // Determine the packet type.
            System.out.println("Waiting for a packet...");
            final Packet packet = Communication.read(peer);
            
            System.out.println("Packet Recieved: ["+packet.getType().name()+"]");
            
            // Switch statement only goes over packets expected by the KDC, any other packet will be ignored.
            switch (packet.getType()) {

                case CHAPClaim: {
                    // Check if the user exists in the secretes && send a challenge back.
                    CHAPClaim chapClaim_packet = (CHAPClaim) packet;
                    if (secrets.stream().anyMatch(n -> n.getUser().equalsIgnoreCase(chapClaim_packet.getuName()))) {


                        // Construct the nonce
                        byte[] nonceBytes = nc.getNonce();
                        nonceList.add(nonceBytes);
                        // System.out.println("original nonce" + Arrays.toString(nonceBytes));
                        //String s = [61, 49, 70, 95, -15, -97, 30, 49, -2, 33, -61, -14, 17, 32, 87, -68, 5, 114, -54, -118, -70, -83, 30, -41, -66, 83, 87, -61, -114, 68, 63, -17];
                        String nonce = Base64.getEncoder().encodeToString(nonceBytes);
                        System.out.println("The original nonce, created by the server: " + nonce);
                        
                        

                        // Create the packet and send
                        CHAPChallenge chapChallenge_packet = new CHAPChallenge(nonce);
                        Communication.send(peer, chapChallenge_packet);
                    }
                }
                ;
                break;
                case CHAPResponse: {
                    CHAPResponse chapResponse_packet = (CHAPResponse) packet; // User's response to challenge, contains combined, hashed pass & nonce

                    // Decompile packet
                    String receivedHash = chapResponse_packet.getHash();
                    
                    System.out.println("Received combined hash(base64): " + receivedHash);

                    // Standard SHA-256
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");

                    boolean status = false; //Status of our comparison with recieved hash, this is returned

                    for (byte[] eachNonceByteArr : nonceList) {
                    // For every secret in secrets.json, combine with nonce and hash it. Then check if it equals the recieved hash
                    if (secrets.stream().anyMatch(secret -> {
                        // byte array and combine the two like the client did when they sent their challenge response
                        byte[] secretHashPass = secret.getSecret().getBytes(StandardCharsets.UTF_8);
                        byte[] clientHashNonce = eachNonceByteArr;
                        //System.out.println("server nonce lookup: " + Arrays.toString(clientHashNonce));
                        //System.out.println("server hashed passwords: " + Arrays.toString(secretHashPass));
                        byte[] combined = new byte[secretHashPass.length + clientHashNonce.length];
                        
                        System.arraycopy(secretHashPass, 0, combined, 0, secretHashPass.length);
                        System.arraycopy(clientHashNonce, 0, combined, secretHashPass.length, clientHashNonce.length);
                        combined = digest.digest(combined);

                        // Our final hash
                        String serverCombinedHash = Base64.getEncoder().encodeToString(combined);

                        System.out.println("Server checking its combined hash: " + serverCombinedHash);
                        
                        // Compare the final hash with the received hash
                        return serverCombinedHash.equalsIgnoreCase(receivedHash);
                    })) {
                        // If valid password, boolean is true 
                        status = true;
                        // Create the packet and send
                        CHAPStatus chapStatus_packet = new CHAPStatus(status);
                        Communication.send(peer, chapStatus_packet);
                    } else {
                        // If invalid password, boolean remains false
                        // Create the packet and send
                        CHAPStatus chapStatus_packet = new CHAPStatus(status);
                        Communication.send(peer, chapStatus_packet);

                    }

                }
                }
                ;
                break;
                case SessionKeyRequest: {
                    //String pw2 = "";
                    SessionKeyRequest SessionKeyRequest_packet = (SessionKeyRequest) packet; //Receive packet containing username and service name
                    // https://stackoverflow.com/questions/31789279/how-can-one-find-an-item-after-the-match-using-streams
                    secrets.stream() // create stream of secret objects in coll.
                            .filter(n -> n.getUser().equalsIgnoreCase(SessionKeyRequest_packet.getuName())) //If secret.username == packet.username equals
                            .findFirst() // first secreet obj that matches if statement 
                            .ifPresent(n -> {
                                System.out.println("Secret pw associated with user: " + n.getUser());
                                String pw = n.getSecret(); // get secret associated to the username 
                                String user = n.getUser();
                                String sessionName = SessionKeyRequest_packet.getsName();
                                //sendSessionKey(user, sessionName, pw);

                                try {
                                    //SessionKeyResponse chapStatus_packet = new SessionKeyResponse(sendSessionKey(user, sessionName, pw));
                                    Communication.send(peer, sendSessionKey(user, sessionName, pw));
                                } catch (IOException ex) {
                                    Logger.getLogger(KDCServer.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                }
                ;
                break;

            }
            
            // Close the connection
            // server.close();
        }
    }

    //this is the part where session key is sent to client 
    private static SessionKeyResponse sendSessionKey(String uname, String sName, String pw) {
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

        return toSend;

    }
    
}
