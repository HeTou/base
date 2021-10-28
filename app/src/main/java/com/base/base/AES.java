package com.base.base;

import android.os.Build;
import android.provider.SyncStateContract;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//AES CBCģʽ���ܹ�����
public class AES {

    /**
     * ����
     *
     * @param data ��Ҫ���ܵ�����
     * @param key  ��������
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, Constants.AES);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, Constants.AES);
            Cipher cipher = Cipher.getInstance(Constants.AES_CBC_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(key));
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ����
     *
     * @param data ����������
     * @param key  ������Կ
     * @return
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, Constants.AES);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, Constants.AES);
            Cipher cipher = Cipher.getInstance(Constants.AES_CBC_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(key));
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ����
     *
     * @param data ��Ҫ���ܵ�����
     * @param key  ��������
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String data, String key) {
        byte[] valueByte = encrypt(data.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(valueByte);
    }

    /**
     * ����
     *
     * @param data ���������� base64 �ַ���
     * @param key  ������Կ
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String data, String key) {
        byte[] originalData = Base64.getDecoder().decode(data.getBytes());
        byte[] valueByte = decrypt(originalData, key.getBytes(StandardCharsets.UTF_8));
        return new String(valueByte);
    }

//    /**
//     * ����һ������ַ�����Կ
//     *
//     * @return
//     * @throws NoSuchAlgorithmException
//     */
//    public static String generateRandomKey() {
//        return IdWorker.get32UUID().substring(0, 16);
//    }
}