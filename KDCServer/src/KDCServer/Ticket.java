
package KDCServer;

import merrimackutil.json.types.JSONObject;

/**
 *
 * @author William Hancock
 */
public class Ticket {
    private long createTime;
    private long validityTime;
    private String uName;
    private String sName;
    private byte[] iv;
    private byte[] eSKey;

    public Ticket(long createTime, long validityTime, String uName, String sName, byte[] iv, byte[] eSKey) {
        this.createTime = System.currentTimeMillis();
        this.validityTime = validityTime;
        this.uName = uName;
        this.sName = sName;
        this.iv = iv;
        this.eSKey = eSKey;
    }
    
    public JSONObject ticketToJSONObject(){
        //we just need to convert it into a JSON object 
        return null;
    }
    
}
