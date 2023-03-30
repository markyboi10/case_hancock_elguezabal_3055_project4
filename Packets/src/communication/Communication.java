package communication;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import merrimackutil.json.JsonIO;
import merrimackutil.json.types.JSONObject;
import packets.CHAPClaim;
import packets.Packet;
import packets.PacketType;

/**
 * Utility class used for sending packets across servers
 * @author elguezabala
 */
public class Communication {
    
    /**
     * Used to send a message to a socket {@code peer}
     * @param peer
     * @param message
     * @throws IOException 
     */
    public static void send(Socket peer, Packet message) throws IOException {
        new PrintWriter(peer.getOutputStream()).print(message.send());
    }
    
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
        // Using a switch statement to prevent reflection
        switch(packetType) {
            case SessionKeyRequest: return new CHAPClaim(line, packetType);
            case SessionKeyResponse: return new CHAPClaim(line, packetType);
            case CHAPChallenge: return new CHAPClaim(line, packetType);
            case CHAPResponse: return new CHAPClaim(line, packetType);
            case CHAPStatus: return new CHAPClaim(line, packetType);
            case CHAPClaim: return new CHAPClaim(line, packetType);
            default: return null;
        }    
    }
    
}
