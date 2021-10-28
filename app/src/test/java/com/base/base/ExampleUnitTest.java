package com.base.base;

import androidx.core.app.RemoteInput;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    private static final String SALTED_STR = "Salted__";
    private static final byte[] SALTED_MAGIC_UTF8 = SALTED_STR.getBytes(StandardCharsets.UTF_8);

    @Test
    public void shanghai() throws Exception {
        String encrypted1 = "U2FsdGVkX1/0p2yZMDKfNKBQT0ZRk9xO16FlcqzK4z4yRTgxnCM2PLH2DQMuEg1YTAL+637HBP1o0Lrf7NdVkEKw5gB918Y9svnf4zeW/XGg105LHSQc7dwArXNg93/h3GF6h0iLMDb37DARfOGblwZADNgVqmuPwzJdG7or/dZCeY6RtTUDS+1scC6g2cluDa87VRvfuAPUtqcU6tm9rNmmW+sYw0ZCePfA5vAAHZLA4kyKkQmHq3i1oMx46pB1D27lg+TJS33FbiWIszhQVA==";
        String base64Key = "Mk1hUzJPZDYydFpxYVgyTA==";

        byte[] decode = Base64.getDecoder().decode(base64Key);
        System.out.println("明文密钥：" + new String(decode));


//        String key = "2MaS2Od62tZqaX2L";

        String result = decrypt(encrypted1, base64Key);
        System.out.println("PPT上二维码密文例子解密：" + result);

        ///----------------------------------------------------------------------------------------------

//        String plaintext = "v1|4204081530|penny|shu|ISC|assistant|pennys@infosalons.com.cn|86 21 51341111|CHINA";
//
//        String encrypted = encrypt(plaintext, key);
//        System.out.println("密文：" + encrypted);
//
//        String decrypt = decrypt(encrypted, base64Key);
//        System.out.println("解密后明文：" + decrypt);

    }


    public static String encrypt(String data, String secret) throws Exception {
        byte[] cipherData = data.getBytes(StandardCharsets.UTF_8);
        byte[] saltData = (new SecureRandom()).generateSeed(8);

        final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8));


        SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
        IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);


        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCBC.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] decryptedData = aesCBC.doFinal(cipherData);
        byte[] decrypted = array_concat(array_concat(SALTED_MAGIC_UTF8, saltData), decryptedData);
        return java.util.Base64.getEncoder().encodeToString(decrypted);
    }

    public static String decrypt(String ciphertext, String plantextKey) throws Exception {
//      base64解密  ->  密钥明文
        String base64Key = new String(Base64.getDecoder().decode(plantextKey));
//      密文的base64字节数组
        byte[] cipherData = Base64.getDecoder().decode(ciphertext);

        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

        final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, base64Key.getBytes(StandardCharsets.UTF_8));

        String s = new String(keyAndIV[0]);
        String s1 = new String(keyAndIV[1]);
        System.out.println(s+"\n"+s1);


        SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
        IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

        byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptedData = aesCBC.doFinal(encrypted);
        return new String(decryptedData, StandardCharsets.UTF_8);
    }


    /***
     *
     * @param keyLength 密钥的长度  32
     * @param ivLength 偏移向量的长度 16
     * @param iterations  迭代次数   1
     * @param salt
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[][] generateKeyAndIV(int keyLength, int ivLength, int iterations, byte[] salt, byte[] password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");

        /***散列（消息摘要）的长度*/
        int digestLength = md.getDigestLength();
        System.out.println("散列长度：" + digestLength);
        /*** 63/16*16 */
        int requiredLength = (keyLength + ivLength + digestLength - 1) / digestLength * digestLength;
        System.out.println(String.format("%d=(%d+%d+%d-1)/%d*%d", requiredLength, keyLength, ivLength, digestLength, digestLength, digestLength));

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