package echoservice;

import communication.Communication;
import echoServiceCrypto.EchoDecryption;
import echoServiceCrypto.EchoEncryption;
import echoservice.config.Config;
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
import merrimackutil.json.types.JSONType;
import merrimackutil.util.NonceCache;
import merrimackutil.util.Tuple;
import packets.CHAPChallenge;
import packets.CHAPClaim;
import packets.ClientHello;
import packets.ClientResponse;
import packets.Packet;
import packets.PacketType;
import static packets.PacketType.ClientHello;
import packets.ServerHello;
import packets.Ticket;

public class EchoService {
    
    private static Config config;  
    
    private static ServerSocket server;
    
    private static byte[] sessionKey;
    private static byte[] EncNonce;
    
    private static NonceCache nc = new NonceCache(32,30);

    public static void main(String[] args) throws FileNotFoundException, InvalidObjectException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException {
        OptionParser op = new OptionParser(args);
        LongOption[] ar = new LongOption[2];
        ar[0] = new LongOption("config", true, 'c');
        ar[1] = new LongOption("help", false, 'h');
        op.setLongOpts(ar);
        op.setOptString("hc:");
        Tuple<Character, String> opt = op.getLongOpt(false);
        if (opt == null || Objects.equals(opt.getFirst(), 'h')) {
            System.out.println("usage:\n"
                    + " echoservice\n"
                    + " echoservice --config <configfile>\n"
                    + " echoservice --help\n"
                    + "options:\n"
                    + " -c, --config Set the config file.\n"
                    + " -h, --help Display the help.");
            System.exit(0);
        } else if (Objects.equals(opt.getFirst(), 'c')) {
            config = new Config(opt.getSecond()); //load config
        }
        try {
            // Create the server
            server = new ServerSocket(config.getPort());

            // Poll for input
            poll();
            
            // Close the server when completed or error is thrown.
            server.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NoSuchMethodException ex) {
            System.out.println("EchoService No Such Method Exception");
            Logger.getLogger(EchoService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("EchoService No Such Algorithm Exception");
            Logger.getLogger(EchoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Waits for a connection with a peer socket, then polls for a message being
     * sent. Each iteration of the loop operates for one message, as not to
     * block multi-peer communication.
     *
     * @throws IOException
     */
    private static void poll() throws IOException, NoSuchMethodException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException, BadPaddingException {
        while (true) { // Consistently accept connections

            // Establish the connection & read the message
            Socket peer = server.accept();

            // Determine the packet type.
            System.out.println("Waiting for a packet...");
            final Packet packet = Communication.read(peer);
            
            System.out.println("Packet Recieved: ["+packet.getType().name()+"]");
            
            // Switch statement only goes over packets expected by the KDC, any other packet will be ignored.
            switch (packet.getType()) {

                 case ClientHello:  {
                     
                     // MESSAGE 2
                    // Check if the user exists in the secretes && send a challenge back.
                    ClientHello ClientHello_packet = (ClientHello) packet;
                    String tkt = ClientHello_packet.getTkt();
                    Ticket ticket = new Ticket(tkt, PacketType.Ticket);
                    String nonce1 = ClientHello_packet.getNonce();
                    byte[] usedNonce1 = Base64.getDecoder().decode(nonce1);
                    
                    nc.addNonce(usedNonce1);
                    ticket.geteSKey();
                    ticket.getIv();
     
                    String serviceName = config.getService_name();
                     String serviceSecret = config.getService_secret();
                     ticket.getValidityTime();
                     ticket.getCreateTime();
       
                    
                    sessionKey =EchoDecryption.decrypt(ticket.geteSKey(),ticket.getIv(), serviceName,serviceSecret,ticket.getCreateTime(), ticket.getValidityTime(), ticket.getsName());
                   
                    System.out.println("EchoService session key: " + Arrays.toString( sessionKey));
                    System.out.println(tkt);
                    ticket.getsName();
                    System.out.println(nonce1);
                    
                    //String stringSesKey = Base64.getEncoder().encodeToString(sessionKey);
                    
                    byte[] nonceBytes = nc.getNonce();
                    String nonceString = Base64.getEncoder().encodeToString(nonceBytes);
                    
                    EncNonce = EchoEncryption.encrypt(sessionKey, nonce1, ticket.getValidityTime(), ticket.getCreateTime(), serviceName, ticket.getsName());
                    
                    // Create the packet and send
                     ServerHello ServerHello_packet = new ServerHello(nonceString, serviceName, Base64.getEncoder().encodeToString(EchoEncryption.getRawIv()), Base64.getEncoder().encodeToString(EncNonce) );
                     Communication.send(peer, ServerHello_packet);
                };
                break;
                 case ClientResponse: {
                     ClientResponse ClientResponse_packet = (ClientResponse) packet;
                     
                     //check nonce if same
                 }

                
            } 
        }

    }

}
