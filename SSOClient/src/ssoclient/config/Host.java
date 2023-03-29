package ssoclient.config;

import java.io.InvalidObjectException;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;


public class Host implements JSONSerializable {
        
    private String host_name;
    private String address;
    private int port;
    
    public Host(JSONObject obj) throws InvalidObjectException {
        deserialize(obj); // Deserialize a host into this host object
    }         

    @Override
    public String serialize() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deserialize(JSONType jsont) throws InvalidObjectException {
        if(jsont instanceof JSONObject) {
            JSONObject obj = (JSONObject) jsont;
            
            if(obj.containsKey("host-name")) {
                this.host_name = obj.getString("host-namename");
            } else { throw new InvalidObjectException("Expected an Host object -- host-name expected."); }
            
            if(obj.containsKey("address")) {
                this.address = obj.getString("address");
            } else { throw new InvalidObjectException("Expected an Host object -- address expected."); }
            
            if(obj.containsKey("port")) {
                this.port = obj.getInt("port");
            } else { throw new InvalidObjectException("Expected an Host object -- port expected."); }
        }
    }

    @Override
    public JSONType toJSONType() {
        JSONObject obj = new JSONObject();
        obj.put("host-name", this.host_name);
        obj.put("address", this.address);
        obj.put("port", this.port);

        return obj; // We should never be writing to a file.
    }
    
    
}
