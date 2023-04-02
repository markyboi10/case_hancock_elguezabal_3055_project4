package echoservice;

import communication.Communication;
import echoServiceCrypto.EchoTktDecryption;
import echoServiceCrypto.EchoSessionKeyEncryption;
import echoServiceCrypto.EchoSessionKeyDecryption;
import echoservice.config.Config;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
import packets.ClientHello;
import packets.ClientResponse;
import packets.Packet;
import packets.HandshakeStatus;
import packets.PacketType;
import static packets.PacketType.ClientHello;
import packets.ServerHello;
import packets.Ticket;

public class EchoService {

    private static Config config;
    private static ServerSocket server;
    private static byte[] serverSidesessionKey;
    private static boolean handshakeStatus = false;
    private static NonceCache nc = new NonceCache(32, 30);

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

            System.out.println("Packet Recieved: [" + packet.getType().name() + "]");

            // Switch statement only goes over packets expected by the KDC, any other packet will be ignored.
            switch (packet.getType()) {
                // ClientHello package
                case ClientHello: {
                    // MESSAGE 2:  decrypt ticket + send fresh nonceS, iv, and encryption of fresh nonceS
                    ClientHello ClientHello_packet = (ClientHello) packet;
                    // tkt in string format
                    String tkt = ClientHello_packet.getTkt();
                    // Break apart ticket to grab data inside
                    Ticket ticket = new Ticket(tkt, PacketType.Ticket);

                    // Grab and and add nonce to cache
                    String usedNonceC = ClientHello_packet.getNonce();
                    byte[] bytesUsedNonceC = Base64.getDecoder().decode(usedNonceC);
                    nc.addNonce(bytesUsedNonceC);

                    // Config strings
                    String serviceName = config.getService_name();
                    String serviceSecret = config.getService_secret();;

                    // Perform decryption with info from tkt, this gives us the session key
                    serverSidesessionKey = EchoTktDecryption.decrypt(ticket.geteSKey(), ticket.getIv(), serviceName, serviceSecret, ticket.getCreateTime(), ticket.getValidityTime(), ticket.getsName());

                    System.out.println("EchoService session key: " + Base64.getEncoder().encodeToString(serverSidesessionKey));

                    // Fresh nonce S
                    byte[] nonceSBytes = nc.getNonce();
                    String nonceSString = Base64.getEncoder().encodeToString(nonceSBytes);

                    // Encrypt nonce C 
                    byte[] EncNonceC = EchoSessionKeyEncryption.encrypt(serverSidesessionKey, usedNonceC, ticket.getValidityTime(), ticket.getCreateTime(), serviceName, ticket.getsName());

                    System.out.println("ct: " +  Base64.getEncoder().encodeToString(EncNonceC));
                    System.out.println("iv: " + Base64.getEncoder().encodeToString(EchoSessionKeyEncryption.getRawIv()));
                    System.out.println("session name : " + serviceName);
                    System.out.println("session key: " + Base64.getEncoder().encodeToString(serverSidesessionKey));
                    // Create the packet and send
                    ServerHello ServerHello_packet = new ServerHello(nonceSString, serviceName, Base64.getEncoder().encodeToString(EchoSessionKeyEncryption.getRawIv()), Base64.getEncoder().encodeToString(EncNonceC));
                    Communication.send(peer, ServerHello_packet);
                    System.out.println("packet sent");
                };
                break;
                // Client Response package
                case ClientResponse: {
                    //MESSAGE 4: Received client response, let's check nonce validity and give a status
                    ClientResponse ClientResponse_packet = (ClientResponse) packet;

                    System.out.println("GOT TO CLIENT RESPSONSE");
                    
                    //check nonce S is same
                    byte[] receivedNonceS = EchoSessionKeyDecryption.decrypt(ClientResponse_packet.geteSKey(), ClientResponse_packet.getIv(), ClientResponse_packet.getcName(), serverSidesessionKey);

                    if (nc.containsNonce(receivedNonceS)) {
                        System.out.println("Nonce matched");
                        handshakeStatus = true; // set status true
                        // Create packet containing status
                        HandshakeStatus handshakeStatus_packet = new HandshakeStatus(handshakeStatus);
                        Communication.send(peer, handshakeStatus_packet); // Send packet
                    } else {
                        System.out.println("Nonce doesn't macth, possible replay attack");
                        HandshakeStatus handshakeStatus_packet = new HandshakeStatus(handshakeStatus); // Status remains false
                        Communication.send(peer, handshakeStatus_packet); // Send packet

                    }

                }; break;
                
      

            }
        }

    }

}
