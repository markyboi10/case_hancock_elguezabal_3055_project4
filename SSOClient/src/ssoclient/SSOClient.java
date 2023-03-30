package ssoclient;

import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.util.Tuple;
import packets.CHAPClaim;
import ssoclient.config.Config;
import ssoclient.config.Host;

public class SSOClient {
   
    public static ArrayList<Host> hosts = new ArrayList<>();
    private static Config config;
    private static String svcName;
    private static String usrName;
    
    private static Scanner scan = new Scanner(System.in);
    private static Socket sock;
    private static Scanner recv;
    private static PrintWriter send;
    
    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidObjectException {
        //temporarily commented out, will be needed in the final version  
        
        OptionParser op = new OptionParser(args);
        LongOption[] ar = new LongOption[2];
        ar[0] = new LongOption("hosts", true, 'h');
        ar[1] = new LongOption("user", true, 'u');
        ar[1] = new LongOption("service", true, 's');
        op.setLongOpts(ar);
        Tuple<Character, String> opt = op.getLongOpt(false);
        if (opt == null) {
            System.out.println("usage:\n"
                    + "   client --hosts <configfile> --user <user> --service <service>\n"
                    + "   client --user <user> --service <service>\n"
                    + "options:\n"
                    + "   -h, --hosts Set the hosts file.\n"
                    + "   -u, --user The user name.\n"
                    + "   -s, --service The name of the service");
            System.exit(0);
        } else if (Objects.equals(opt.getFirst(), 'h')) {
            config = new Config(opt.getSecond()); // Construct the config & hosts parameters.
        }
        
        
        
        // Connect to the KDC_Server
        connectWithServer("kdcd");
        // Runs the CHAP protocol
        CHAP();
        
       
        

        // Extract nonce
        String ExtractedNonce = "";
        Pattern pattern = Pattern.compile("nonce is: (\\S+)");
        Matcher matcher = pattern.matcher(recvMsg);
        if (matcher.find()) {
            ExtractedNonce = matcher.group(1);
        } else {
            System.exit(0);
        }

        // Client sends hashed password and nonce
        //https://stackoverflow.com/questions/5683486/how-to-combine-two-byte-arrays
        String msg2 = scan.nextLine();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] clientHashPass = msg2.getBytes(StandardCharsets.UTF_8);
        byte[] clientHashNonce = ExtractedNonce.getBytes(StandardCharsets.UTF_8);
        byte[] combined = new byte[clientHashPass.length + clientHashNonce.length];
        
        System.arraycopy(clientHashPass, 0, combined, 0, clientHashPass.length);
        System.arraycopy(clientHashNonce, 0, combined, clientHashPass.length, clientHashNonce.length);
        combined = digest.digest(combined);
        //put combined in a JSON string
        send.println(Arrays.toString(combined));

        // Server recieved hash pass and nonce and will check validity via comparison
        String recvMsg2 = recv.nextLine();
        System.out.println("Server Said: " + "\n" + recvMsg2);
        String recvMsg3 = recv.nextLine();
        System.out.println(recvMsg3);
        //send the session key request
        //we need to send a packet containing the username and the svc name
    }
    
    /**
     * Connects to a server based off of {@code host_name} 
     * Looks up the server in hosts array.
     * @param host_name 
     */
    private static void connectWithServer(String host_name) {
        // Construct socket variables.
        try {
            // Get the responsable host.
            Host host = hosts.stream().filter(n -> n.getHost_name().equalsIgnoreCase(host_name)).findFirst().orElse(null);
            
            if(host == null) {
                System.out.println("Host with name ["+host_name+"] is unknown.");
                return;
            }
            
            // Set up a connection to the echo server running on the same machine.
            sock = new Socket(host.getAddress(), host.getPort());

            // Set up the streams for the socket.
            recv = new Scanner(sock.getInputStream());
            send = new PrintWriter(sock.getOutputStream(), true);
        } catch (UnknownHostException ex) {
            System.out.println("Host is unknown.");
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    /**
     * Runs the CHAP-modified for JSON protocol.
     * All protocols should be ran on one single thread.
     * @return boolean value on if the chap protocol finished correctly
     */
    private static boolean CHAP() {
       
        // MESSAGE 1
        packets.CHAPClaim claim = new CHAPClaim(usrName);
        send.println(claim.send()); // send username
       
        // KDC checks username validity and if valid, demands password and gives a nonce
        String recvMsg = recv.nextLine();
               
        
        return true; // completed CHAP
    }    
    
}
