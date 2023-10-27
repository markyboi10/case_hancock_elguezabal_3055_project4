/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package javaapplication70;

import java.security.SecureRandom;
import java.util.Base64;
import merrimackutil.codec.Base32;

/**
 *
 * @author Mark Case
 */
public class JavaApplication70 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
// generate TOTP key
    byte[] secretKey = new byte[32];
    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(secretKey);
    
    String totpKey = Base64.getEncoder().encodeToString(secretKey);
    
    // check if addition was successful
        
        String nah = "bdBbsg97DtC+L95pk0OQh3ZHrW0Mreo9ds+HNA5laXlTjBpRR2F3HCl46x6OAdoCF69jSqNsDePpAb/a468x2g==";
      byte[] test = Base64.getDecoder().decode(nah);
      String base32Key = Base32.encodeToString(test, false).replaceAll("=", "");

      System.out.println(base32Key);



    
}
}
