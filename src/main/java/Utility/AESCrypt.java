package Utility;

import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCrypt {

	private final Cipher cipher;
	private final SecretKeySpec key;
	private AlgorithmParameterSpec spec;
	public String SEED_16_CHARACTER = "";

	public AESCrypt(String env) throws Exception {

		if(env.equalsIgnoreCase("live")) {
			SEED_16_CHARACTER = "A778M5U1MGHUZ4Qz";
		} else if(env.equalsIgnoreCase("staging")){
			SEED_16_CHARACTER = "ARMjU1M674OUZ4Qz";
		}

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(SEED_16_CHARACTER.getBytes("UTF-8"));
		byte[] keyBytes = new byte[32];
		System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		key = new SecretKeySpec(keyBytes, "AES");
		spec = getIV();

	}

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public AlgorithmParameterSpec getIV() {
		byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
		bytesToHex(iv);
		IvParameterSpec ivParameterSpec;
		ivParameterSpec = new IvParameterSpec(iv);
		return ivParameterSpec;
	}

	public String encrypt(String plainText) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, key, spec);
		byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
		//String encryptedText = new String(Base64.encode(encrypted), "UTF-8");
		//String encodedBytes = Base64.getEncoder().encodeToString(new String(encrypted,Charset.forName("UTF-8")).getBytes());
		String encodedBytes = Base64.getEncoder().encodeToString(encrypted);
		return  encodedBytes;
	}

	public String decrypt(String cryptedText) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, key, spec);
		byte[] bytes = Base64.getDecoder().decode(cryptedText.getBytes("UTF-8"));
		byte[] decrypted = cipher.doFinal(bytes);
		String decryptedText = new String(decrypted, "UTF-8");

		return decryptedText;
	}
}
