package ssoclient;

import ClientServerCrypto.ClientMasterKeyDecryption;
import ClientServerCrypto.ClientSessionKeyDecryption;
import ClientServerCrypto.ClientSessionKeyEncryption;
import communication.Communication;
import java.io.Console;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
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
import packets.HandshakeStatus;
import packets.ClientHello;
import packets.ClientResponse;
import packets.ServerHello;
import packets.SessionKeyRequest;
import packets.SessionKeyResponse;
import packets.Ticket;
import ssoclient.config.Config;
import ssoclient.config.Host;

public class SSOClient {

    public static ArrayList<Host> hosts = new ArrayList<>();
    private static Config config;
    private static String pw;
    private static NonceCache nc = new NonceCache(32, 30);
    // Command line variables
    private static String user;
    private static String service;
    private static byte[] sessionKeyClient;

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, InvalidObjectException, IOException, NoSuchMethodException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        System.out.println("args: " + Arrays.toString(args));

        // Initializing the CLI
        boolean shortlen = false;

        OptionParser op = new OptionParser(args);
        op.setLongAndShortOpts(new LongOption[]{
            new LongOption("hosts", true, 'h'),
            new LongOption("user", true, 'u'),
            new LongOption("service", true, 's')
        });

        // op.setLongOpts(ar);
        op.setOptString("h:u:s:");

        Tuple<Character, String> opt = op.getLongOpt(false);
        System.out.println(opt.getSecond());
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
            config = new Config(opt.getSecond());
        } else if (Objects.equals(opt.getFirst(), 'u')) {
            // If the host is not specified then it is the local hosts.json file
            user = opt.getSecond();
            config = new Config("hosts.json");
        }

        Tuple<Character, String> opt2 = op.getLongOpt(false);
        System.out.println(opt2.getSecond());
        if (Objects.equals(opt2.getFirst(), 'u')) {
            user = opt2.getSecond();
        } else if (Objects.equals(opt2.getFirst(), 's')) {
            service = opt2.getSecond();
            shortlen = true;
        }

        if (shortlen != true) {
            Tuple<Character, String> opt3 = op.getLongOpt(false);
            System.out.println(opt3.getSecond());
            if (opt3.getSecond() != null && Objects.equals(opt3.getFirst(), 's')) {
                // Init the username and service
                service = opt3.getSecond();
            }
        }

        // Check the service type and operate such.
        if (service.equalsIgnoreCase("echoservice")) { // KDC --> EchoService

            System.out.println("Running Chap.");

            // Runs the CHAP protocol
            // If chap returns true, run session key request
            if (CHAP()) {
                SessionKeyRequest();
                Ticket toSend = SessionKeyRequest();
                Handshake(toSend);
                //find kdcd address
            } else { // If chap returns false
                System.exit(0);
            }

        } else {
            System.out.println("Service not found with name [" + service + "]. Closing program ");
            System.exit(0);
        } // If we do the bonus then we add another condition here.

    }

    /**
     * Finds a host based off {@code host_name}
     *
     * @param host_name
     */
    private static Host getHost(String host_name) {
        return hosts.stream().filter(n -> n.getHost_name().equalsIgnoreCase(host_name)).findFirst().orElse(null);
    }

    /**
     * Runs the CHAP-modified for JSON protocol. All protocols should be ran on
     * one single thread.
     *
     * @return boolean value on if the chap protocol finished correctly
     */
    private static boolean CHAP() throws IOException, NoSuchMethodException, NoSuchAlgorithmException {

        Host host = getHost("kdcd");

        // MESSAGE 1
        System.out.println("creating claim");
        CHAPClaim claim = new CHAPClaim(user); // Construct the packet
        System.out.println("sending packet");
        Socket peer1 = Communication.connectAndSend(host.getAddress(), host.getPort(), claim); // Send the packet

        System.out.println("reading packet");
        // MESSAGE 2
        CHAPChallenge chapChallenge_Packet = (CHAPChallenge) Communication.read(peer1); // Read for a packet  // KDC checks recieved hash by hashing its version of the password and nonce

        // MESSAGE 3
        // Client sends hashed password and nonce
        //https://stackoverflow.com/questions/12076165/how-to-obscure-scanner-input-text
        //https://stackoverflow.com/questions/5683486/how-to-combine-two-byte-arrays
        Console console = System.console();
        pw = new String("password");
        //Concatenate Password+Nonce and hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] clientHashPass = Base64.getDecoder().decode(pw);
        byte[] clientHashNonce = Base64.getDecoder().decode(chapChallenge_Packet.getNonce());
        System.out.println("The received nonce displayed client side: " + chapChallenge_Packet.getNonce());
//        byte[] clientHashNonce = KDCServer.getNocc();
        //System.out.println("Client recieved hashed nonce: " + Arrays.toString(clientHashNonce));
        //System.out.println("Client original hashed pass: " + Arrays.toString(clientHashPass));
        byte[] combined = new byte[clientHashPass.length + clientHashNonce.length];
        System.arraycopy(clientHashPass, 0, combined, 0, clientHashPass.length);
        System.arraycopy(clientHashNonce, 0, combined, clientHashPass.length, clientHashNonce.length);
        combined = digest.digest(combined);
        CHAPResponse response = new CHAPResponse(Base64.getEncoder().encodeToString(combined));
        Socket peer2 = Communication.connectAndSend(host.getAddress(), host.getPort(), response);

        //MESSAGE 4
        //Receive the status message
        CHAPStatus chapStatus_Packet = (CHAPStatus) Communication.read(peer2);
        if (chapStatus_Packet.getMsg() == false) {
            System.out.println("Incorrect credentials");
            return false;
        }

        System.out.println("GOT TO THE END! :)");

        return true; // completed CHAP
    }

    private static Ticket SessionKeyRequest() throws IOException, NoSuchMethodException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
        Host host = getHost("kdcd");

        // MESSAGE 1: Client sends kdc username and service name
        SessionKeyRequest req = new SessionKeyRequest(user, service); // Construct the packet
        Socket socket = Communication.connectAndSend(host.getAddress(), host.getPort(), req); // Send the packet

        // MESSAGE 2
        SessionKeyResponse sessKeyResp_Packet = (SessionKeyResponse) Communication.read(socket); // Read for a packet  // KDC checks username validity and if valid, demands password and gives a nonce
        System.out.println("IV");
        System.out.println(sessKeyResp_Packet.getuIv());

        System.out.println("Session key");
        System.out.println(sessKeyResp_Packet.geteSKeyAlice());
        System.out.println(sessKeyResp_Packet.getValidityTime());
        System.out.println(sessKeyResp_Packet.getCreateTime());
        System.out.println(sessKeyResp_Packet.getsName());
        //alice's session key

        sessionKeyClient = ClientMasterKeyDecryption.decrypt(sessKeyResp_Packet.geteSKeyAlice(), sessKeyResp_Packet.getuIv(), user, pw, sessKeyResp_Packet.getCreateTime(), sessKeyResp_Packet.getValidityTime(), sessKeyResp_Packet.getsName());
        System.out.println("Client session key: " + Arrays.toString(sessionKeyClient));

        //send a ticket
        return new Ticket(sessKeyResp_Packet.getCreateTime(), sessKeyResp_Packet.getValidityTime(), sessKeyResp_Packet.getuName(), sessKeyResp_Packet.getsName(), sessKeyResp_Packet.getIv(), sessKeyResp_Packet.geteSKey());
    }

    private static boolean Handshake(Ticket in) throws IOException, NoSuchMethodException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        //Client connects to echoservice
        Host host = getHost("echoservice");

        // Get fresh nonce C
        byte[] nonceCBytes = nc.getNonce();
        // Convert nonceCBytes to Base64 string format
        String nonceC = Base64.getEncoder().encodeToString(nonceCBytes);

        // Serialize ticket that we will send
        String tkt = in.serialize();

        // MESSAGE 1: Client sends echoservice the nonce C and ticket
        ClientHello hi = new ClientHello(nonceC, tkt); // Construct the packet
        Socket socket = Communication.connectAndSend(host.getAddress(), host.getPort(), hi); // Send the packet

        //MESSAGE 3: Recieved the server hello
        ServerHello ServerHello_Packet = (ServerHello) Communication.read(socket);

        System.out.println("ct: " + ServerHello_Packet.geteSKey());
        System.out.println("iv: " + ServerHello_Packet.getIv());
        System.out.println("user: " + user);
        System.out.println("session name : " + ServerHello_Packet.getsName());    
         System.out.println("session key: " + Base64.getEncoder().encodeToString(sessionKeyClient));
        //Decrypt nonce c
        byte[] checkNonceCBytes = ClientSessionKeyDecryption.decrypt(ServerHello_Packet.geteSKey(), ServerHello_Packet.getIv(), user, sessionKeyClient, ServerHello_Packet.getsName());

        if (Arrays.equals(checkNonceCBytes, nonceCBytes)) {

            // Get nonce S ready for encryption and add to cache
            String stringNonceS = ServerHello_Packet.getNonce();
            System.out.println(stringNonceS);
            byte[] usedNonceSBytes = Base64.getDecoder().decode(stringNonceS);
            nc.addNonce(usedNonceSBytes);
            // Fresh nonce R
            byte[] nonceBytesR = nc.getNonce();
            String nonceR = Base64.getEncoder().encodeToString(nonceBytesR);

            // Encrypt nonce S

            byte[] encNonceS = ClientSessionKeyEncryption.encrypt(sessionKeyClient, usedNonceSBytes, user, ServerHello_Packet.getsName());


            // Packet everything together to send to echo server
            ClientResponse clientResponse_packet = new ClientResponse(nonceR, user, Base64.getEncoder().encodeToString(ClientSessionKeyEncryption.getRawIv()), Base64.getEncoder().encodeToString(encNonceS));
        
            // Send packet off

            Socket socket2 = Communication.connectAndSend(host.getAddress(), host.getPort(), clientResponse_packet);

            Socket socket20 = Communication.connectAndSend(host.getAddress(), host.getPort(), clientResponse_packet);

            //MESSAGE 4: Client recieves status
            HandshakeStatus handshakeStatus_packet = (HandshakeStatus) Communication.read(socket20);
            // If message returns true
            if (handshakeStatus_packet.getMsg() == true) {
                // Handshake protocol checks out

                System.out.println("STATUS RECEIVED: TRUE");

                return true;
            } else {
                System.out.println("STATUS RECEIVED: FALSE");
                //Otherwise false, exit system
                System.exit(0);
            }

        
        return false;

    }

}
