package KDCServer.crypto;

import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.InvalidKeyException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.security.InvalidAlgorithmParameterException;
import java.nio.charset.StandardCharsets;
import java.security.spec.InvalidKeySpecException;


public class GCMEncrypt {
	public static void encrypt(String mkey) throws
	    NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException,  InvalidAlgorithmParameterException, InvalidKeySpecException
	{
		int tagSize = 128;
		// Set up an AES cipher object.
		Cipher aesCipher = Cipher.getInstance("AES/GCM/NoPadding");

		// Get a key generator object.
		KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");

		// Set the key size to 128 bits.
		aesKeyGen.init(128);

		// Generate the session key.
		Key aesKey = aesKeyGen.generateKey();
                //generate the master key from the password.
                Key mKey = scrypt.genKey(mkey);
		// Generate the IV.
		SecureRandom rand = new SecureRandom();
		byte[] rawIv = new byte[16];		// Block size of AES.
		rand.nextBytes(rawIv);					// Fill the array with random bytes.
		GCMParameterSpec gcmParams = new GCMParameterSpec(tagSize, rawIv);

		// Put the cipher in encrypt mode with the specified key.
		aesCipher.init(Cipher.ENCRYPT_MODE, mKey, gcmParams);

		//encrypt the session key
		byte[] ciphertext = aesCipher.doFinal(aesKey.getEncoded());

    System.out.println("Ciphertext: " + Base64.getEncoder().encodeToString(ciphertext));
    System.out.println("Key: " + Base64.getEncoder().encodeToString(aesKey.getEncoded()));
		System.out.println("IV: " + Base64.getEncoder().encodeToString(rawIv));
		System.out.println("Tag Size: " + tagSize + " bits.");
	}
}
