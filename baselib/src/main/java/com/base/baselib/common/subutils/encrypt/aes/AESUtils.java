package com.base.baselib.common.subutils.encrypt.aes;


import com.base.baselib.common.subutils.encrypt.rsa.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * author:zft
 * date:2017/12/21 0021.
 * <p>
 * AES 对称加密
 */
public class AESUtils {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
    private static final String SHA1PRNG = "SHA1PRNG";//// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 1、创建密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));    // 2、初始化为加密模式的密码器
            byte[] byteContent = content.getBytes("utf-8");
            byte[] result = cipher.doFinal(byteContent);//               //3、加密
            return Base64.encode(result);//通过Base64转码返回
        } catch (Exception ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));

            //执行操作
            byte[] result = cipher.doFinal(Base64.decode(content));

            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg ;
        SecureRandom sr;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                sr = SecureRandom.getInstance(SHA1PRNG, "Crypto");
            } else {
                sr = SecureRandom.getInstance(SHA1PRNG);
            }
            sr.setSeed(password.getBytes());
            //注意：android 和java 两个环境结果不一致。因为他们的默认随机数序列不一致，所以解决他的办法是，不要使用默认的创建方法。
//          kg.init(128, new SecureRandom(password.getBytes()));
            kg.init(128, sr);////256 bits or 128 bits,192bits
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AESUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String s = "hello,您好";

        System.out.println("s:" + s);

        String s1 = AESUtils.encrypt(s, "1234");
        System.out.println("s1:" + s1);

        System.out.println("s2:" + AESUtils.decrypt(s1, "1234"));


    }

}
