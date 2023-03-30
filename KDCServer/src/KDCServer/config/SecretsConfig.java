/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KDCServer.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.JsonIO;
import merrimackutil.json.types.JSONArray;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;

/**
 *
 * @author Alex
 */
public class SecretsConfig implements JSONSerializable {
    
    private String path;
    
    public SecretsConfig(String path) throws FileNotFoundException, InvalidObjectException {
        this.path = path;
        
        // Construct file
        File file = new File(path);
        
        if(file == null || !file.exists()) {
            throw new FileNotFoundException("File from path for EchoServiceConfig does not point to a vadlid configuration json file.");
        }
        
        // Construct JSON Object and load hosts
        JSONObject obj = JsonIO.readObject(file);
        JSONArray array = obj.getArray("secrets");
        // deserialize
        deserialize(array);
    }

    @Override
    public String serialize() {
        return toJSONType().getFormattedJSON();// We should never be converting this file to JSON, only read.
    }
    @Override
    public void deserialize(JSONType type) throws InvalidObjectException {
        if(type instanceof JSONArray) {
            JSONArray array = (JSONArray) type;
            
            // Construct a list of hosts
            List<Secrets> secrets = array.stream()
                    .filter(n -> n instanceof JSONObject)
                    .map(n -> (JSONObject)n)
                    .map(n -> {
                        try {
                            return new Secrets(n);
                        } catch(InvalidObjectException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            // Add all hosts to the SSO Client.
            KDCServer.KDCServer.secrets.addAll(secrets);
        }       
    }

    @Override
    public JSONType toJSONType() {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        
        arr.addAll(KDCServer.KDCServer.secrets); // Add all hosts to the array.
        
        obj.put("hosts", arr); // Assign the hosts array.
        return obj; // We are never reading this file to JSON.
    }

}
