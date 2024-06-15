package org.research.kadda.nuclide.service;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class EncryptPwd {

	private final static String ENCODING1 = "UTF-8";
	private final static String ENCODING2 = "Cp1252";

	private Cipher ecipher;
	private Cipher dcipher;
	private boolean newSystem = true;

	public EncryptPwd(String passPhrase) {
		byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56, (byte) 0x34, (byte) 0xE3,
				(byte) 0x03 };
		int iterationCount = 19;
		try {
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String encrypt(char[] str) {
		try {
			if (newSystem) {
				byte[] utf8 = new String(str).getBytes("UTF-8");
				byte[] enc = ecipher.doFinal(utf8);
				String res = byteToHex(enc);
				if (!new String(str).equals(new String(decrypt(res))))
					throw new RuntimeException("decryption mismatch ");
				return res;
			} else {
				byte[] utf8 = new String(str).getBytes(ENCODING1);
				byte[] enc = ecipher.doFinal(utf8);
				String res = new String(enc, ENCODING2);
				return res;
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot encrypt", e);
		}
	}

	public String decrypt(String str) {
		try {
			if (newSystem) {
				byte[] utf8 = dcipher.doFinal(hexToByte(str));
				return new String(utf8, "UTF-8");
			} else {
				byte[] dec = str.getBytes(ENCODING2);
				byte[] utf8 = dcipher.doFinal(dec);
				return new String(utf8, ENCODING1);
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot decrypt ", e);
		}
	}

	public static String byteToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	public static byte[] hexToByte(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

}