package com.avkapp;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.*;

public class Encryption {
	private int KeySize = 4096;
	private int Algorithm = "RSA";

	public KeyPair generateKeyPair(String password) {
		String MYPBEALG = "PBEWithSHA1AndDESede";

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Algorithm);
		keyGen.initialize(KeySize);

		KeyPair key = keyGen.generateKeyPair();

		PBEKeySpec keyspec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory keyFac = SecretKeyFactory.getInstance(MYPBEALG);
		SecretKey pbeKey = keyFac.generateSecret(keyspec);

		Cipher pbeCipher = Cipher.getInstance(MYPBEALG);

		// Initialize PBE Cipher with key and parameters
		pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

		// Encrypt the encoded Private Key with the PBE key
		byte[] ciphertext = pbeCipher.doFinal(encodedprivkey);

		// Now construct  PKCS #8 EncryptedPrivateKeyInfo object
		AlgorithmParameters algparms = AlgorithmParameters.getInstance(MYPBEALG);
		algparms.init(pbeParamSpec);
		EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);

		// and here we have it! a DER encoded PKCS#8 encrypted key!
		byte[] encryptedPkcs8 = encinfo.getEncoded();
	}

	public static byte[] encrypt(String text, PublicKey key) {
		byte[] chiffre = null;

		try {
			Cipher cipher = Cipher.getInstance(Algorithm);
			cipher.init(cipher.ENCRYPT_MODE, key);
			chiffre = cipher.doFinal(text.getBytes());
		}
		catch (Exception e) {

		}

		return chiffre;
	}

	public static String decrypt(byte[] text, PrivateKey key, String password) {
		byte[] decryptedText = null;
    	try {
      		// get an RSA cipher object and print the provider
      		final Cipher cipher = Cipher.getInstance(ALGORITHM);

      		// decrypt the text using the private key
      		cipher.init(Cipher.DECRYPT_MODE, key);
      		decryptedText = cipher.doFinal(text);

    	} catch (Exception ex) {
      		ex.printStackTrace();
    	}

    	return new String(decryptedText);
	}
}