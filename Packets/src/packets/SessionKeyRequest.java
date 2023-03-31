package packets;

import packets.Packet;
import java.io.InvalidObjectException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.JsonIO;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;

/**
 *
 * @author William Hancock
 */
public class SessionKeyRequest implements Packet, JSONSerializable {
    
    // Packet Type
    private final static PacketType packetType = PacketType.SessionKeyResponse;
    
    // Packet Data
    private String uName;
    private String sName;

    /**
     * Default Constructor for a SessionKeyResponse
     * @param uName
     * @param sName 
     */
    public SessionKeyRequest(String uName, String sName) {
        this.uName = uName;
        this.sName = sName;
    }

    public String getuName() {
        return uName;
    }

    public String getsName() {
        return sName;
    }
    

    /**
     * Converts a JSONObject into a ticket object
     * @param packet byte[] of information representing this packet
     * @throws InvalidObjectException Thrown if {@code object} is not a Ticket JSONObject
     */
    public SessionKeyRequest(String packet, PacketType packetType) throws InvalidObjectException {
        recieve(packet);
    }

    /**
     * JSONSerializable implementations
     */
    
    /**
     * Serializes the object into a JSON String
     * @return 
     */
    @Override
    public String serialize() {
        return toJSONType().toJSON();
    }

    /**
     * Converts a JSON type to this object
     * Converts types of Byte[] into strings for travel
     * @param jsont
     * @throws InvalidObjectException 
     */
    @Override
    public void deserialize(JSONType obj) throws InvalidObjectException {
        JSONObject tmp;

        if (obj instanceof JSONObject)
          {
            tmp = (JSONObject)obj;

            if (tmp.containsKey("uName"))
              this.uName = tmp.getString("uName");
            else
              throw new InvalidObjectException("Expected an Ticket object -- uName expected.");
            
            if (tmp.containsKey("sName"))
              this.sName = tmp.getString("sName");
            else
              throw new InvalidObjectException("Expected an Ticket object -- sName expected.");
          }
          else 
            throw new InvalidObjectException("Expected a Ticket - Type JSONObject not found.");
    }

    /**
     * Constructs a JSON object representing this Ticket.
     * @return 
     */
    @Override
    public JSONType toJSONType() {
        JSONObject object = new JSONObject();
        
        object.put("packetType", this.packetType.toString());
        object.put("uName", this.uName);
        object.put("sName", this.sName);

        return object;
    }

   /**
     * Packet implementations.
     */
    
    /**
     * Constructs a packet based off of this object
     * A packet is a byte[], we are using JSON to store the packet data.
     * @return A byte[] of packet information
     */    
    @Override
    public String send() {
        
        String jsonString = serialize(); // Convert to String from this class.
        return jsonString; // Convert JSON string to byte[]
    }

    /**
     * Constructs an object T based off of the given packet
     * Generally, this should be used when reading packets in.
     * @param packet input byte[]
     */
    @Override
    public void recieve(String packet) {
        
        try {
             JSONObject jsonObject = JsonIO.readObject(packet); // String to JSONObject
             deserialize(jsonObject); // Deserialize jsonObject
        } catch (InvalidObjectException ex) {
            Logger.getLogger(SessionKeyResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
 
    /**
     * The PacketType value of this packet.
     * @return 
     */
    @Override
    public PacketType getType() {
        return packetType;
    }
}
