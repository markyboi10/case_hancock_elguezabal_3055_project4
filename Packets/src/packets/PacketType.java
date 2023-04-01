package packets;

import java.util.Arrays;

/**
 * Represents the type of packet being sent/received
 * Uses for determining responses & information.
 * @author Alex
 */
public enum PacketType {
    
      SessionKeyRequest(packets.CHAPChallenge.class),
      SessionKeyResponse(packets.SessionKeyResponse.class),
      CHAPChallenge(packets.CHAPChallenge.class),
      CHAPResponse(packets.CHAPResponse.class),
      CHAPStatus(packets.CHAPStatus.class),
      CHAPClaim(packets.CHAPClaim.class) 
      ;

      
    private Class packetClass;
    
    PacketType(Class packetClass) {
        this.packetClass = packetClass;
    }
    
    /**
     * The associated class representing a PacketType
     * @return 
     */
    public Class getPacketClass() {
        return this.packetClass;
    }
    
    /**
     * Gets a packet from a String {@code name}
     * @param name
     * @return 
     */
    public static PacketType getPacketTypeFromString(String name) {
        return Arrays.stream(values())
                .filter(n -> n.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
      
    public static PacketType getPacketFromClass(Class classObj) {
        return Arrays.stream(values())
                .filter(n -> n.getPacketClass().getName().equalsIgnoreCase(classObj.getName()))
                .findFirst()
                .orElse(null);
    }
}
