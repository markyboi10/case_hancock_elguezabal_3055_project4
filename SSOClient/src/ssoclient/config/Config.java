package ssoclient.config;


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
public class Config implements JSONSerializable {
    
    private String path;
    
    public Config(String path) throws FileNotFoundException, InvalidObjectException {
        this.path = path;
        
        // Construct file
        File file = new File(path);
        
        if(file == null || !file.exists()) {
            throw new FileNotFoundException("File from path for EchoServiceConfig does not point to a vadlid configuration json file.");
        }
        
        // Construct JSON Object and load hosts
        JSONArray array = JsonIO.readArray(file);
        // deserialize
        deserialize(array);
    }

    @Override
    public String serialize() {
        return "";// We should never be converting this file to JSON, only read.
    }

    @Override
    public void deserialize(JSONType type) throws InvalidObjectException {
        if(type instanceof JSONArray) {
            JSONArray array = (JSONArray) type;
            
            // Construct a list of hosts
            List<Host> hosts = array.stream()
                    .filter(n -> n instanceof JSONObject)
                    .map(n -> (JSONObject)n)
                    .map(n -> {
                        try {
                            return new Host(n);
                        } catch(InvalidObjectException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            
            
        }       
    }

    @Override
    public JSONType toJSONType() {
        return null; // We are never reading this file to JSON.
    }

}
