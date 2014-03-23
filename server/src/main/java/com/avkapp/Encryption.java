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

	public static String SHA256(String input) {
		String out = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(input.getBytes("UTF-8"));
			out = new String(md.digest());
			md.reset();
		}
		catch (Exception e) {

		}
		
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(out);
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

	
}