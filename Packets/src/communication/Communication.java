package communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import merrimackutil.json.JsonIO;
import merrimackutil.json.types.JSONObject;
import packets.CHAPChallenge;
import packets.CHAPClaim;
import packets.CHAPResponse;
import packets.CHAPStatus;
import packets.Packet;
import packets.PacketType;
import static packets.PacketType.CHAPChallenge;
import static packets.PacketType.CHAPResponse;
import static packets.PacketType.CHAPStatus;
import static packets.PacketType.SessionKeyRequest;
import static packets.PacketType.SessionKeyResponse;
import packets.SessionKeyRequest;
import packets.SessionKeyResponse;

/**
 * Utility class used for sending packets across servers
 * @author Alex Elguezabal
 */
public class Communication {
    
    /**
     * Used to send a message to a socket {@code peer}
     * @param peer
     * @param message
     * @throws IOException 
     */
    public static void send(Socket peer, Packet message) throws IOException {
        new PrintWriter(peer.getOutputStream(), true).print(message.send());
    }
    
    /**
     * Connects to a socket and sends a packet
     * Used for client-side messaging where we are sending single message instances.
     * 
     * @param address Address of the server
     * @param port Port to connect too on the server
     * @param messgae Packet to be sent
     */
    public static void connectAndSend(String address, int port, Packet messgae) throws IOException {
        Socket peerSocket = null;
        try {
            // Set up a connection to the echo server running on the same machine.
            peerSocket = new Socket(address, port);
        } catch (UnknownHostException ex) {
            System.out.println("Host ["+address+" "+port+"] connected could not be established.");
        } catch (IOException ioe) {
            System.out.println("Host ["+address+" "+port+"] connected could not be established.");
            ioe.printStackTrace();
        }
        
        send(peerSocket, messgae);
    }
    
    /**
     * Used to reed a message from a socket {@code peer}
     * Must update the switch statement with all packet types.
     * **Note: This method is blocking, it will wait for a response before allowing the thread too continue.
     * @param peer
     * @return
     * @throws IOException
     * @throws NoSuchMethodException 
     */
    public static Packet read(Socket peer) throws IOException, NoSuchMethodException {
        Scanner scanner = new Scanner(peer.getInputStream());
        
        // Input from peer
        String line = scanner.nextLine(); // Blocking call, will not run anything until this finished on thread.
    
        // Determine the type of packet.
        JSONObject object = JsonIO.readObject(line); // All packets are type JSON object with identifier "packetType"
        String identifier = object.getString("packetType");
        PacketType packetType = PacketType.getPacketTypeFromString(identifier);
       
        // Assure we have the packet type
        if(packetType == null) {
            throw new NullPointerException("No packet called ["+identifier+"] found.");
        }
       
        // Switch over all of the packet types
        // Using a switch statement to avoid reflection
        switch(packetType) {
            case SessionKeyRequest: return new SessionKeyRequest(line, packetType);
            case SessionKeyResponse: return new SessionKeyResponse(line, packetType);
            case CHAPChallenge: return new CHAPChallenge(line, packetType);
            case CHAPResponse: return new CHAPResponse(line, packetType);
            case CHAPStatus: return new CHAPStatus(line, packetType);
            case CHAPClaim: return new CHAPClaim(line, packetType);
            default: return null;
        }    
    }
    
}
