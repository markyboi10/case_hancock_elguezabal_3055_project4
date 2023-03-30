package packets;

/**
 *
 * @author Alex
 */
public interface Packet {
    
    /**
     * Constructs a packet based off of this object
     * A packet is a byte[], we are using JSON to store the packet data.
     * @return A byte[] of packet information
     */    
    public String send();
    
    /**
     * Constructs an object T based off of the given packet
     * Generally, this should be used when reading packets in.
     * @param packet input byte[]
     */
    public void recieve(String packet);
    
}
