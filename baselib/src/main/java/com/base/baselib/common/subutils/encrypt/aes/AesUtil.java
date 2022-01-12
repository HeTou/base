package com.base.baselib.common.subutils.encrypt.aes;


import com.base.baselib.common.subutils.encrypt.rsa.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/***
 *  {@link BadPaddingException :pad block corrupted 密码错误}
 *  {@link NoSuchPaddingException :Padding PKCS5Padding1 unknown 没有PKCS5Padding1这种算法}
 *  {@link InvalidKeyException :无效key}
 *  {@link UnsupportedEncodingException :不支持编码，new String("",utf-8)}
 * author:zft
 * date:2017/12/21 0021.
 * <p>
 * AES 对称加密
 */
public class AesUtil {
    private static final String KEY_ALGORITHM = "AES";
    private static final String SHA1PRNG = "SHA1PRNG";//// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法

    //  填充模式
//    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法 //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private static final String CIPHER_CBC_ALGORITHM = "AES/CBC/NoPadding";

    //  AES-CBC-128偏移向量
    private final static String ivParameter = "KjQlb%1aV1Jrli79";         //iv偏移量

    private static final String CURRENT_ALGORITHM = CIPHER_CBC_ALGORITHM;       //当前配置的加密模式

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(CURRENT_ALGORITHM);// 1、创建密码器
        byte[] ivBytes = ivParameter.getBytes();

        IvParameterSpec iv = new IvParameterSpec(ivBytes);// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        if (CURRENT_ALGORITHM.contains("/EBC/")) {
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));    // 2、初始化为加密模式的密码器
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password), iv);       // 使用CBC模式，需要一个向量iv
        }

        byte[] byteContent = content.getBytes("utf-8");
        System.out.println("utf-8 byte数组：" + parseByte2HexStr(byteContent));
        /*****自动填充 start*/
        int blockSize = cipher.getBlockSize();
        System.out.println("最小字节长度：" + blockSize);
        int length = byteContent.length;
        //计算需填充长度
        if (length % blockSize != 0) {
            length = length + (blockSize - (length % blockSize));
        }
        byte[] plaintext = new byte[length];
        //填充
        System.arraycopy(byteContent, 0, plaintext, 0, byteContent.length);
        byteContent = plaintext;
        System.out.println("填充后 byte数组:" + parseByte2HexStr(byteContent));
        /*****自动填充 end*/
        byte[] result = cipher.doFinal(byteContent);//               // 3、加密
        System.out.println("加密后 byte 数组：" + parseByte2HexStr(result));
        return Base64.encode(result);//通过Base64转码返回
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        //实例化
        Cipher cipher = Cipher.getInstance(CURRENT_ALGORITHM);
        //使用密钥初始化，设置为解密模式
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        if (CURRENT_ALGORITHM.contains("/EBC/")) {
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));    // 2、初始化为加密模式的密码器
        } else {
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password), iv);       // 使用CBC模式，需要一个向量iv
        }
        //执行操作
        byte[] result = cipher.doFinal(Base64.decode(content));
        return new String(result, "utf-8");
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
//        KeyGenerator kg = null;
//        SecureRandom sr = null;
        try {
//            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
////            //AES 要求密钥长度为 128
////            if (android.os.Build.VERSION.SDK_INT >= 17) {
//                sr = SecureRandom.getInstance(SHA1PRNG, "Crypto");
////            } else {
////                sr = SecureRandom.getInstance(SHA1PRNG);
////            }
//            sr.setSeed(password.getBytes());
//            //注意：android 和java 两个环境结果不一致。因为他们的默认随机数序列不一致，所以解决他的办法是，不要使用默认的创建方法。
////          kg.init(128, new SecureRandom(password.getBytes()));
//            kg.init(128, sr);////256 bits or 128 bits,192bits
//            //生成一个密钥
//            SecretKey secretKey = kg.generateKey();
//            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
            return new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {

        try {
            String content = "dzy19930710";
            String key = "KjQlb%1aV1Jrli79";
            String encrypt = AesUtil.encrypt(content, key);
            String decrypt = null;
            decrypt = AesUtil.decrypt(encrypt, key);
            System.out.println("加密:" + encrypt);
            System.out.println("解密:" + decrypt);
            System.out.println(content.equals(decrypt.trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < buf.length; ++i) {
            String hex = Integer.toHexString(buf[i] & 255);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }

        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hexStr.length() / 2];

            for (int i = 0; i < hexStr.length() / 2; ++i) {
                int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
                int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
                result[i] = (byte) (high * 16 + low);
            }

            return result;
        }
    }


}
