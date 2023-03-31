/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ClientServerCrypto;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author MarkC
 */
public class GCMDecrypt {
    
        
        public static byte[] decrypt(String ct, String IV, String uName) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
         String masterPass = "";
         
         
        int tagSize = 128; // 128-bit authentication tag.
        SecretKey key = scrypt.genKey(masterPass, uName);
        byte[] keyBytes = key.getEncoded();

        // Set up an AES cipher object.
        Cipher aesCipher = null;
        try {
            aesCipher = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(GCMDecrypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Setup the key.
        SecretKeySpec aesKey = new SecretKeySpec(keyBytes, "AES");

        try {
            // Put the cipher in encrypt mode with the specified key.
            aesCipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(tagSize, Base64.getDecoder().decode(IV)));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(GCMDecrypt.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        // Finalize the message.
        byte[] plaintext = null;
        try {
            plaintext = aesCipher.doFinal(Base64.getDecoder().decode(ct));
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(GCMDecrypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return plaintext;

    } // End 'decrypt' method
    
}
