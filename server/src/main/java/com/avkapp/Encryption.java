package com.avkapp;

import java.security.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.security.spec.KeySpec;
import java.security.spec.InvalidKeySpecException;
import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class Encryption {
	private static int KeySize = 4096;
	private static String Algorithm = "RSA";

	public static void main(String[] args) {
		System.out.println("TESTING OUT ENCRYPTION");
		try { System.out.println("Max Key size : " + Cipher.getMaxAllowedKeyLength("AES/ECB/PKCS5Padding")); }
		catch (Exception e) {}
		KeyPair keypair = Encryption.generateKeyPair();
		String val = "TESTING OUT THAT SHIT";
		System.out.println("Original : " + val);

		String encryptString = Encryption.readableEncrypt(val, keypair.getPublic(), "password");
		System.out.println("--------- Encrypted : ");
		System.out.println(encryptString);

		String decryptString = Encryption.readableDecrypt(val, keypair.getPrivate(), "password");
		System.out.println("--------- Decrypted (good key)");
		System.out.println(decryptString);

		String wrongString = Encryption.readableDecrypt(val, keypair.getPrivate(), "notpassword");
		System.out.println("--------- Decrypted (wrong key)");
		System.out.println(wrongString);
	}

	public static KeyPair generateKeyPair() {
		KeyPairGenerator keyGen = null;
		KeyPair key;

		try {
			keyGen = KeyPairGenerator.getInstance(Algorithm);
			keyGen.initialize(KeySize);
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}


		// La KeyPair de l'utilisateur
		key = keyGen.generateKeyPair();
		
		return key;
	}

	public static String ToBase64(byte[] data) {
		return Encryption.ToBase64(new String(data));
	}
	public static String ToBase64(String data) {
		return Base64.encodeBase64String(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(data));
	}

	public static byte[] FromBase64(String data) {
		return StringUtils.newStringUtf8(Base64.decodeBase64(data)).getBytes();
	}

	public static byte[] SHA256(String text) {
		byte[] hash = null;
		try {
			// On génère un hash en SHA-256 : peu importe la longueur du mot de passe entré, on est certain que la sortie fera 32 bytes
			MessageDigest digester = MessageDigest.getInstance("SHA-256");
    		digester.update(text.getBytes("UTF-8"));
    		hash = digester.digest();
    	}
    	catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return hash;
	}

	public static byte[] encrypt(String text, PublicKey pKey, String password) {
		SecretKey secret = null;
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			// On génère un hash de 128 bits, avec 1024 rounds
			// Salt constant. oups.
			KeySpec spec = new PBEKeySpec(password.toCharArray(), "ConstantSalt".getBytes(), 1024, 128);
			SecretKey tmp = factory.generateSecret(spec);
			secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			System.out.println("Secret created");
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		byte[] data = encryptSymmetric(encryptAsymmetric(text.getBytes(), pKey), secret);

		return data;
	}

	public static String readableEncrypt(String text, PublicKey pKey, String password) {
		return ToBase64(Encryption.encrypt(text, pKey, password));
	}

	public static byte[] decrypt(String text, PrivateKey pKey, String password) {	
		SecretKey secret = null;
		// On génère un hash en SHA-256 : peu importe la longueur du mot de passe entré, on est certain que la sortie fera 32 bytes
		secret = new SecretKeySpec(Encryption.ToBase64(password).getBytes(), "AES/CBC/PKCS5Padding");

		byte[] data = decryptSymmetric(decryptAsymmetric(text.getBytes(), pKey), secret);

		return data;
	}

	public static String readableDecrypt(String text, PrivateKey pKey, String password) {
		return ToBase64(Encryption.decrypt(text, pKey, password));
	}

	private static byte[] encryptAsymmetric(byte[] text, PublicKey key) {
		byte[] chiffre = null;

		try {
			Cipher cipher = Cipher.getInstance(Algorithm);
			cipher.init(cipher.ENCRYPT_MODE, key);
			chiffre = cipher.doFinal(text);
		}
		catch (Exception e) {

		}

		return chiffre;
	}

	private static byte[] decryptAsymmetric(byte[] text, PrivateKey key) {
		byte[] decryptedText = null;
    	try {
      		// get an RSA cipher object and print the provider
      		final Cipher cipher = Cipher.getInstance(Algorithm);

      		// decrypt the text using the private key
      		cipher.init(Cipher.DECRYPT_MODE, key);
      		decryptedText = cipher.doFinal(text);

    	} catch (Exception ex) {
      		ex.printStackTrace();
    	}

    	return decryptedText;
	}

	private static byte[] encryptSymmetric(byte[] inpBytes, SecretKey key){
    	Cipher cipher = null;
    	byte[] out = null;
		try {
	    	cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    		cipher.init(Cipher.ENCRYPT_MODE, key);
    		out = cipher.doFinal(inpBytes);
    	}
    	catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		catch (BadPaddingException e) {
			e.printStackTrace();
		}
		catch (InvalidKeyException e) {
			e.printStackTrace();
		}
    	
    	return out;
    }
    private static byte[] decryptSymmetric(byte[] inpBytes, SecretKey key) {
    	Cipher cipher = null;
    	byte[] out = null;
    	try {
	    	cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    		cipher.init(Cipher.DECRYPT_MODE, key);
    		out = cipher.doFinal(inpBytes);
    	}
    	catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		catch (BadPaddingException e) {
			e.printStackTrace();
		}
		catch (InvalidKeyException e) {
			e.printStackTrace();
		}
    	
    	return out;
    }
}