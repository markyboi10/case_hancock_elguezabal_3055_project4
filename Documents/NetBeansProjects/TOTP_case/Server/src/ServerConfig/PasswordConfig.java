package ServerConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
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
 * @author Mark Case
 */
public class PasswordConfig implements JSONSerializable {
    
    private String path;
    
    public PasswordConfig(String path) throws FileNotFoundException, InvalidObjectException {
        this.path = path;     
        
        // Construct file
        File file = new File(path);
        
        if(file == null || !file.exists()) {
            throw new FileNotFoundException("File from path for SecretsConfig does not point to a vadlid configuration json file.");
        }
        
        // Construct JSON Object and load hosts
        JSONObject obj = JsonIO.readObject(file);
        JSONArray array = obj.getArray("entries");
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
            List<Password> passwd = array.stream()
                    .filter(n -> n instanceof JSONObject)
                    .map(n -> (JSONObject)n)
                    .map(n -> {
                        try {
                            return new Password(n);
                        } catch(InvalidObjectException e) {
                            System.out.println("NOTHING ADDED TO ARRAY");
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            // Add all hosts to the SSO Client.
            server.Server.passwd.addAll(passwd);
            
        }       
    }

    @Override
    public JSONType toJSONType() {
        JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
        
        arr.addAll(server.Server.passwd); // Add all hosts to the array.
        
        obj.put("entries", arr); // Assign the hosts array.
        return obj; // We are never reading this file to JSON.
    }

}
