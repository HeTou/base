package com.base.base;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * AESTest2
 *
 * @author Jaince
 * @date 2021/06/09
 */
public class AESTest2 {

	private static final String SALTED_STR = "Salted__";
	private static final byte[] SALTED_MAGIC_UTF8 = SALTED_STR.getBytes(StandardCharsets.UTF_8);

	public static void main(String[] args) throws Exception {
		String encrypted1 = "U2FsdGVkX19EHVis+ZwukHL+UOKimtljMpY1HBPagHUX/NBkTUFdsZM2U5VEBcidZcDnM/WL/lRcKbNo0UvxW/JBnWdfQ1xiCRsGJFn6eB17D5H+KLbgW9wHyC9EtCv4HvRYmNigFBlNAx/USg6Huw==";
		String base64Key = "Mk1hUzJPZDYydFpxYVgyTA==";
		String key = "2MaS2Od62tZqaX2L";

		String result = decrypt(encrypted1, key);
		System.out.println("PPT上二维码密文例子解密：" + result);

		///----------------------------------------------------------------------------------------------

		String plaintext = "v1|4204081530|penny|shu|ISC|assistant|pennys@infosalons.com.cn|86 21 51341111|CHINA";

		String encrypted = encrypt(plaintext, key);
		System.out.println("密文：" + encrypted);

		String decrypt = decrypt(encrypted, key);
		System.out.println("解密后明文：" + decrypt);

		//-----邮件PHP例子---------------

		String key2 = "S3cwQjQ2R0ljbHdna1ZUcQ==";
		String plaintext2 = "INCOSKOREA21|999000|abcded|ab|Eabcdee|CTO|abcd|010-1111-1234|KOR";

		String encrypted2 = encrypt(plaintext2, key2);
		System.out.println("密文2：" + encrypted2);

		String decrypt2 = decrypt(encrypted2, key2);
		System.out.println("解密后明文2：" + decrypt2);

	}

	public static String encrypt(String data, String secret) throws Exception {
		byte[] cipherData = data.getBytes(StandardCharsets.UTF_8);
		byte[] saltData = (new SecureRandom()).generateSeed(8);

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
		SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
		IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

		Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aesCBC.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] decryptedData = aesCBC.doFinal(cipherData);
		byte[] decrypted = array_concat(array_concat(SALTED_MAGIC_UTF8, saltData), decryptedData);
		return Base64.getEncoder().encodeToString(decrypted);
	}

	public static String decrypt(String data, String secret) throws Exception {
		byte[] cipherData = Base64.getDecoder().decode(data);
		byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
		SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
		IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

		byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
		Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] decryptedData = aesCBC.doFinal(encrypted);
		return new String(decryptedData, StandardCharsets.UTF_8);
	}

	
	public static byte[][] generateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password, MessageDigest md) {

		int digestLength = md.getDigestLength();
		int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
		byte[] generatedData = new byte[requiredLength];
		int generatedLength = 0;

		try {
			md.reset();

			// Repeat process until sufficient data has been generated
			while (generatedLength < keyLength + ivLength) {

				// Digest data (last digest if available, password data, salt if available)
				if (generatedLength > 0)
					md.update(generatedData, generatedLength - digestLength, digestLength);
				md.update(password);
				if (salt != null)
					md.update(salt, 0, 8);
				md.digest(generatedData, generatedLength, digestLength);

				// additional rounds
				for (int i = 1; i < iterations; i++) {
					md.update(generatedData, generatedLength, digestLength);
					md.digest(generatedData, generatedLength, digestLength);
				}

				generatedLength += digestLength;
			}

			// Copy key and IV into separate byte arrays
			byte[][] result = new byte[2][];
			result[0] = Arrays.copyOfRange(generatedData, 0, keyLength);
			if (ivLength > 0)
				result[1] = Arrays.copyOfRange(generatedData, keyLength, keyLength + ivLength);

			return result;

		} catch (DigestException e) {
			throw new RuntimeException(e);
		} finally {
			// Clean out temporary data
			Arrays.fill(generatedData, (byte) 0);
		}
	}

	private static byte[] array_concat(final byte[] a, final byte[] b) {
		final byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}


}
