package KDCServer.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.JsonIO;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;

/**
 *
 * @author Alex
 */
public class Config implements JSONSerializable {
    
    private String path;
    
    private String secrets_file;
    private int port;
    private boolean debug;
    private int validity_period;
    
    public Config(String path) throws FileNotFoundException, InvalidObjectException {
        this.path = path;
        
        // Construct file
        File file = new File(path);
        
        if(file == null || !file.exists()) {
            throw new FileNotFoundException("File from path for KDCConfig does not point to a vadlid configuration json file.");
        }
        
        // Construct JSON Object and load configuration
        JSONObject obj = JsonIO.readObject(file);
        deserialize(obj);
    }

    @Override
    public String serialize() {
        return "";// We should never be converting this file to JSON, only read.
    }

    @Override
    public void deserialize(JSONType type) throws InvalidObjectException {
        
        JSONObject obj;
        if(type instanceof JSONObject) {
            obj = (JSONObject) type;
        } else { throw new InvalidObjectException("Expected KDCConfig Type - JsonObject. "); }
        
        if(obj.containsKey("secrets-file")) {
            this.secrets_file = obj.getString("secrets-file");
        } else { throw new InvalidObjectException("Expected an KDCConfig object -- secrets-file expected."); }
        
        if(obj.containsKey("port")) {
            this.port = obj.getInt("port");
        } else { throw new InvalidObjectException("Expected an KDCConfig object -- port expected."); }
        
        if(obj.containsKey("debug")) {
            this.debug = obj.getBoolean("debug");
        } else { throw new InvalidObjectException("Expected an KDCConfig object -- debug expected."); }
        
        if(obj.containsKey("validity-period")) {
            this.validity_period = obj.getInt("validity-period");
        } else { throw new InvalidObjectException("Expected an KDCConfig object -- validity-period expected."); }
                    
    }

    @Override
    public JSONType toJSONType() {
        return null; // We are never reading this file to JSON.
    }

     /**
     * Accessors
     */
    
    /**
     * @return the secrets_file
     */
    public String getSecrets_file() {
        return secrets_file;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @return the validity_period
     */
    public int getValidity_period() {
        return validity_period;
    }
    
}
