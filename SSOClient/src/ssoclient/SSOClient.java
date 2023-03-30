package ssoclient;

import communication.Communication;
import java.io.Console;
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
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.util.Tuple;
import packets.CHAPChallenge;
import packets.CHAPClaim;
import packets.CHAPResponse;
import packets.CHAPStatus;
import ssoclient.config.Config;
import ssoclient.config.Host;

public class SSOClient {
   
    public static ArrayList<Host> hosts = new ArrayList<>();
    private static Config config;
    private static String svcName;
    private static String usrName;
    
    private static Socket peerSocket;
    
    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidObjectException, IOException, NoSuchMethodException {
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
        
        // Runs the CHAP protocol
        CHAP();
       
        
    }
    
    /**
     * Finds a host based off {@code host_name}
     * @param host_name 
     */
    private static Host getHost(String host_name) {
        return hosts.stream().filter(n -> n.getHost_name().equalsIgnoreCase(host_name)).findFirst().orElse(null);
    }
    
    /**
     * Runs the CHAP-modified for JSON protocol.
     * All protocols should be ran on one single thread.
     * @return boolean value on if the chap protocol finished correctly
     */
    private static boolean CHAP() throws IOException, NoSuchMethodException, NoSuchAlgorithmException {
       
        Host host = getHost("kdcd");
        
        // MESSAGE 1
        CHAPClaim claim = new CHAPClaim(usrName); // Construct the packet
        Communication.connectAndSend(host.getAddress(), host.getPort(), claim); // Send the packet
       
        
        // MESSAGE 2
        CHAPChallenge chapChallenge_Packet = (CHAPChallenge) Communication.read(peerSocket); // Read for a packet  // KDC checks username validity and if valid, demands password and gives a nonce
        
        // MESSAGE 3
        // Client sends hashed password and nonce
        //https://stackoverflow.com/questions/12076165/how-to-obscure-scanner-input-text
        //https://stackoverflow.com/questions/5683486/how-to-combine-two-byte-arrays
        Console console = System.console();
        String pw = new String(console.readPassword("KDC Password: "));
        //Concatenate Password+Nonce and hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] clientHashPass = pw.getBytes(StandardCharsets.UTF_8);
        byte[] clientHashNonce = chapChallenge_Packet.getNonce().getBytes(StandardCharsets.UTF_8);
        byte[] combined = new byte[clientHashPass.length + clientHashNonce.length];
        System.arraycopy(clientHashPass, 0, combined, 0, clientHashPass.length);
        System.arraycopy(clientHashNonce, 0, combined, clientHashPass.length, clientHashNonce.length);
        combined = digest.digest(combined);
        CHAPResponse response = new CHAPResponse(Base64.getEncoder().encodeToString(combined));
        Communication.connectAndSend(host.getAddress(), host.getPort(), response);
        
        //MESSAGE 4
        //Receive the status message
        CHAPStatus chapStatus_Packet = (CHAPStatus) Communication.read(peerSocket);
        if (chapStatus_Packet.getMsg() == false) {
            return false;
        }
        return true; // completed CHAP
    }    
    
}
