
package KDCServer;

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
public class Ticket implements Packet, JSONSerializable {
    private long createTime;
    private long validityTime;
    private String uName;
    private String sName;
    private byte[] iv;
    private byte[] eSKey;

    /**
     * Default Constructor for a Ticket
     * @param createTime
     * @param validityTime
     * @param uName
     * @param sName
     * @param iv
     * @param eSKey 
     */
    public Ticket(long createTime, long validityTime, String uName, String sName, byte[] iv, byte[] eSKey) {
        this.createTime = System.currentTimeMillis();
        this.validityTime = validityTime;
        this.uName = uName;
        this.sName = sName;
        this.iv = iv;
        this.eSKey = eSKey;
    }
    
    /**
     * Converts a JSONObject into a ticket object
     * @param packet byte[] of information representing this packet
     * @throws InvalidObjectException Thrown if {@code object} is not a Ticket JSONObject
     */
    public Ticket(byte[] packet) throws InvalidObjectException {
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
            
            if (tmp.containsKey("createTime"))
              this.createTime = (Long) tmp.get("createTime");
            else
              throw new InvalidObjectException("Expected an Ticket object -- createTime (long) expected.");
            
            if (tmp.containsKey("validityTime"))
              this.validityTime = (Long) tmp.get("validityTime");
            else
              throw new InvalidObjectException("Expected an Ticket object -- validityTime (Long) expected.");
            
            if (tmp.containsKey("uName"))
              this.uName = tmp.getString("uName");
            else
              throw new InvalidObjectException("Expected an Ticket object -- uName expected.");
            
            if (tmp.containsKey("sName"))
              this.sName = tmp.getString("sName");
            else
              throw new InvalidObjectException("Expected an Ticket object -- sName expected.");
            
            if (tmp.containsKey("iv"))
              this.iv = Base64.getDecoder().decode(tmp.getString("iv"));
            else 
              throw new InvalidObjectException("Expected an Ticket object -- iv (byte[] array) expected.");
            
            if (tmp.containsKey("iv"))
              this.iv = Base64.getDecoder().decode(tmp.getString("iv"));
            else 
              throw new InvalidObjectException("Expected an Ticket object -- iv (String) expected.");
            
            if (tmp.containsKey("eSKey"))
              this.iv = Base64.getDecoder().decode(tmp.getString("eSKey"));
            else 
              throw new InvalidObjectException("Expected an Ticket object -- eSKey (String) expected.");
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
        
        object.put("createTime", this.createTime);
        object.put("validityTime", this.validityTime);
        object.put("uName", this.uName);
        object.put("sName", this.sName);
        object.put("iv", Base64.getEncoder().encode(this.iv));
        object.put("eSKey", Base64.getEncoder().encode(this.eSKey));

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
    public byte[] send() {
        
        String jsonString = serialize(); // Convert to String from this class.
        return Base64.getDecoder().decode(jsonString); // Convert JSON string to byte[]
    }

    /**
     * Constructs an object T based off of the given packet
     * Generally, this should be used when reading packets in.
     * @param packet input byte[]
     */
    @Override
    public void recieve(byte[] packet) {
        
        try {
             String jsonString = Base64.getEncoder().encodeToString(packet); // Convert byte[] to string
             JSONObject jsonObject = JsonIO.readObject(jsonString); // String to JSONObject
             deserialize(jsonObject); // Deserialize jsonObject
        } catch (InvalidObjectException ex) {
            Logger.getLogger(Ticket.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
