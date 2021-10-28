package com.base.base;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;

/**
 * AESTest
 *
 * @author Jaince
 * @date 2021/06/08
 */
public class AESTest {

	public static final String SALTED = "Salted__";


	public static void main(String[] args) {
		String key = "S3cwQjQ2R0ljbHdna1ZUcQ==";
		String data = "INCOSKOREA21|999000|abcded|ab|Eabcdee|CTO|abcd|010-1111-1234|KOR";
		String encrypted = encrypt(data, key);

		System.out.println("密文：" + encrypted);

		String decrypted = decrypt(encrypted, key);
		System.out.println("解码后明文：" + decrypted);

		//------------------------------------------------------------以下旧列子失败
		String encrypted1 = "U2FsdGVkX19EHVis+ZwukHL+UOKimtljMpY1HBPagHUX/NBkTUFdsZM2U5VEBcidZcDnM/WL/lRcKbNo0UvxW/JBnWdfQ1xiCRsGJFn6eB17D5H+KLbgW9wHyC9EtCv4HvRYmNigFBlNAx/USg6Huw==";
		String key1 = "Mk1hUzJPZDYydFpxYVgyTA==";

		String decrypted1 = decrypt(encrypted1, key1);
		System.out.println("解码后明文2：" + decrypted1);

	}

	public static String decrypt(String base64, String passphrase) {
		passphrase = Base64.decodeStr(passphrase);
		String[] decode = decode(base64);
		String[] evpkdf = evpkdf(passphrase, decode[1]);

//		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, evpkdf[0].getBytes(StandardCharsets.UTF_8), evpkdf[1].getBytes(StandardCharsets.UTF_8));
//		return aes.decryptStr(decode[0]);

		return AES.decrypt(decode[0], passphrase);
	}

	public static String[] decode(String base64) {
		String data = Base64.decodeStr(base64);
		if (!SALTED.equals(data.substring(0, 8))) {
			throw new IllegalArgumentException("非法参数");
		}
		String salt = data.substring(8, 8);
		String ct = data.substring(16);
		return new String[]{ct, salt};
	}

	public static String[] evpkdf(String passphrase, String salt) {
		String salted = "";
		String dx = "";
		while (salted.length() < 48) {
			dx = SecureUtil.md5(dx + passphrase + salt);
			salted += dx;
		}
		String key = salted.substring(0, 32);
		String iv = salted.substring(32, 48);
		return new String[]{key, iv};
	}

	public static String encode(String ct, String salt) {
		return Base64.encode(SALTED + salt + ct);
	}

	public static String encrypt(String data, String passphrase) {
		passphrase = Base64.decodeStr(passphrase);
		String salt = RandomUtil.randomString(8);
		String[] evpkdf = evpkdf(passphrase, salt);

//		AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, evpkdf[0].getBytes(StandardCharsets.UTF_8), evpkdf[1].getBytes(StandardCharsets.UTF_8));
//		String ct = aes.encryptBase64(data);
		String ct = AES.encrypt(data, passphrase);
		return encode(ct, salt);
	}

}
