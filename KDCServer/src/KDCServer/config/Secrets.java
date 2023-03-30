/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KDCServer.config;

import java.io.InvalidObjectException;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;


public class Secrets implements JSONSerializable {
        
    private String user;
    private String secret;

    public Secrets(JSONObject obj) throws InvalidObjectException {
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
            
            if(obj.containsKey("user")) {
                this.user = obj.getString("user");
            } else { throw new InvalidObjectException("Expected an Secret object -- user expected."); }
            
            if(obj.containsKey("secret")) {
                this.secret = obj.getString("secret");
            } else { throw new InvalidObjectException("Expected an Secret object -- secret expected."); }
            
        }
    }

    @Override
    public JSONType toJSONType() {
        JSONObject obj = new JSONObject();
        obj.put("user", this.getUser());
        obj.put("secret", this.getSecret());


        return obj; // We should never be writing to a file.
    }

    public String getUser() {
        return user;
    }

    public String getSecret() {
        return secret;
    }

    

    
    
}

