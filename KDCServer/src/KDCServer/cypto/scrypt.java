
package KDCServer.cypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import org.bouncycastle.jcajce.spec.ScryptKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author William Hancock
 */
public class scrypt {
/**
     * Derives the AES-128 key from the password.
     * @param password the password to derive the key from
     * @return the AES-128 key
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static SecretKey genKey(String password) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        final int COST = 2048;          // A.K.A Iterations
        final int BLK_SIZE = 8;
        final int PARALLELIZATION = 1;  // Number of parallel threads to use.
        final int KEY_SIZE = 128;
        ScryptKeySpec scryptSpec;

        // Register bouncy castle provider.
        Security.addProvider(new BouncyCastleProvider());

        SecretKeyFactory factory = SecretKeyFactory.getInstance("SCRYPT");

        // Get a 16-byte IV for an AES key if it does not exist.
            byte[] salt = new byte[16];
            SecureRandom rand = new SecureRandom();
            rand.nextBytes(salt);

        // Derive an AES key from the password using the password. The memory
        // required to run the derivation, in bytes, is:
        //    128 * COST * BLK_SIZE * PARALLELIZATION.
        // The password argument expects and array of charaters *not* bytes.
        //
        scryptSpec = new ScryptKeySpec(password.toCharArray(), salt, COST, BLK_SIZE,
                PARALLELIZATION, KEY_SIZE);

        // Generate the secrete key.
        SecretKey key = factory.generateSecret(
                scryptSpec);
        return key;

    }
}
