package com.base.baselib.common.subutils.encrypt.des;


import com.base.baselib.common.subutils.ConvertUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * author:zft
 * date:2017/11/21 0021.
 * des加解密，3des加解密
 */

public class DesUtils {
    //   1、加密
    public static byte[] encrypt(byte[] data, String key) {
        byte[] res = (byte[]) null;
        byte[] bytK1;
        byte[] bytK2;
        if (key.length() == 48) {
            bytK1 = ConvertUtils.hexStringToBytes(key.substring(0, 16));
            bytK2 = ConvertUtils.hexStringToBytes(key.substring(16, 32));
            byte[] bytK3 = ConvertUtils.hexStringToBytes(key.substring(32, 48));
            res = getDesEncrypt(getDesDecrypt(getDesEncrypt(data, bytK1), bytK2), bytK3);
        } else if (key.length() == 32) {
            bytK1 = ConvertUtils.hexStringToBytes(key.substring(0, 16));
            bytK2 = ConvertUtils.hexStringToBytes(key.substring(16, 32));
            res = getDesEncrypt(getDesDecrypt(getDesEncrypt(data, bytK1), bytK2), bytK1);
        } else if (key.length() == 16) {
            res = getDesEncrypt(data, ConvertUtils.hexStringToBytes(key));
        } else {
            System.out.println("key的格式不对");
        }

        return res;
    }

    //   2、解密
    public static byte[] decrypt(byte[] data, String key) {
        byte[] res = (byte[]) null;
        byte[] bytK1;
        byte[] bytK2;
        if (key.length() == 48) {
            bytK1 = ConvertUtils.hexStringToBytes(key.substring(0, 16));
            bytK2 = ConvertUtils.hexStringToBytes(key.substring(16, 32));
            byte[] bytK3 = ConvertUtils.hexStringToBytes(key.substring(32, 48));
            res = getDesDecrypt(getDesEncrypt(getDesDecrypt(data, bytK3), bytK2), bytK1);
        } else if (key.length() == 32) {
            bytK1 = ConvertUtils.hexStringToBytes(key.substring(0, 16));
            bytK2 = ConvertUtils.hexStringToBytes(key.substring(16, 32));
            res = getDesDecrypt(getDesEncrypt(getDesDecrypt(data, bytK1), bytK2), bytK1);
        } else if (key.length() == 16) {
            res = getDesDecrypt(data, ConvertUtils.hexStringToBytes(key));
        } else {
            System.out.println("key的格式不对");
        }

        return res;
    }

    //    des
    public static byte[] getDesEncrypt(byte[] data, byte[] key) {
        return getEncrypt(data, key, "DES/ECB/NoPadding");
    }


    public static byte[] getDesDecrypt(byte[] byteS, byte[] key) {
        return getDecrypt(byteS, key, "DES/ECB/NoPadding");
    }

    //    3des
    public static byte[] getTripleDESEncrypt(byte[] data, byte[] key) {
        return getEncrypt(data, key, "DESede/ECB/NoPadding");
    }

    public static byte[] getTripleDESDecrypt(byte[] byteS, byte[] key) {
        return getDecrypt(byteS, key, "DES/ECB/NoPadding");
    }

    public static byte[] getEncrypt(byte[] data, byte[] key, String transformation) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(transformation);            //1、创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));    //2、初始为加密模式的密码器
            byteFina = cipher.doFinal(data);                        //3、加密
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            cipher = null;
        }

        return byteFina;
    }

    /***
     *
     * @param byteS
     * @param key
     * @param transformation  转换的名称:例如 DES/ECB/NoPadding;DES/CBC/PKCS5Padding  ;创建 Cipher 对象，应用程序调用 Cipher 的 getInstance 方法并将所请求转换 的名称传递给它
     * @return
     */
    public static byte[] getDecrypt(byte[] byteS, byte[] key, String transformation) {
        byte[] byteFina = (byte[]) null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(transformation);            //1、创建密码器
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));    //2、初始为解密模式的密码器
            byteFina = cipher.doFinal(byteS);                       //3、解密
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /***
     * 获取密钥对象
     * @param key
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static SecretKey getSecretKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        DESKeySpec desKeySpec = new DESKeySpec(key);        //创建一个 DESKeySpec 对象，使用key中的前 8 个字节作为 DES 密钥的密钥内容。
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");      //返回转换指定算法的秘密密钥的 SecretKeyFactory 对象
        return keyFactory.generateSecret(desKeySpec);
    }

}
