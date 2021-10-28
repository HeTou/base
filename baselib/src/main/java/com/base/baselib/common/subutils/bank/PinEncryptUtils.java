package com.base.baselib.common.subutils.bank;



import com.base.baselib.common.subutils.ConvertUtils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * x9.8输入PIN标准
 *
 * @author zouhuaqiu
 * @date 2014-08-21
 */
public class PinEncryptUtils {
    public static final String Algorithm3DES = "DESede";
    public static final String AlgorithmDES = "DES";

    private byte[] masterKey;
    private byte[] workingKey;
    private String card_no;

    /**
     * 设置主密钥
     *
     * @param masterKey
     * @return 本方法对象
     */
    public PinEncryptUtils setMasterKey(byte[] masterKey) {
        this.masterKey = masterKey;
        return this;
    }

    /**
     * 工作密钥
     *
     * @param workingKey
     * @return 本方法对象
     */
    public PinEncryptUtils setWorkingKey(byte[] workingKey) {
        this.workingKey = workingKey;
        return this;
    }

    /**
     * 卡号
     *
     * @param card_no
     * @return 本方法对象
     */
    public PinEncryptUtils setCardNo(String card_no) {

        this.card_no = card_no;
        return this;
    }

    /**
     * 模拟输入PIN
     * 流程：
     * 1、获取pin 加密串
     * 2、获取cardNo 加密串
     * 3、3des解密，获取pin 明文
     * 4、pin明文和cardNo加密串 进行异或
     * 5、3des加密，pin明文作为key,对 异或结果进行加密
     *
     * @param PIN 银行卡密码
     * @return pin
     */
    public byte[] inputPIN(String PIN) {

        String pin_str = builtPINStr(PIN);
        String card_str = builtCardStr(card_no);

        byte[] real_workingKey = decryptMode(workingKey, masterKey, Algorithm3DES);     //获取pin 明文
        System.out.println("pin明文:" + ConvertUtils.bytesToHexString(real_workingKey) + "\r\ncard_str:" + card_str);
        String xor = ConvertUtils.XOR(pin_str, card_str);
        System.out.println("xor:" + xor);
        byte[] ex = ConvertUtils.hexStringToBytes(xor);    //进行异或
        return encryptMode(ex, real_workingKey, Algorithm3DES);                        // 使用pin 进行加密

    }

    /**
     * 解密加密串
     *
     * @param ciphertext
     * @return 解密结果
     */
    public String decrypt(byte[] ciphertext) {

        byte[] real_workingKey = decryptMode(workingKey, masterKey, Algorithm3DES);
        byte[] src = decryptMode(ciphertext, real_workingKey, Algorithm3DES);
        String card_str = builtCardStr(card_no);
        byte[] tmp = ConvertUtils.hexStringToBytes(ConvertUtils.XOR(ConvertUtils.bytesToHexString(src), card_str));
        String str = ConvertUtils.bytesToHexString(tmp);
        int length = Integer.valueOf(str.substring(0, 2));
        String pin = str.substring(2, length + 2);
        return pin;
    }

    /**
     * 生成PIN的加密串
     *
     * @param PIN 2位长度+ pin  然后F 补全 16位
     * @return 如：pin = 123456  结果： 06123456FFFFFFFF
     */
    private static String builtPINStr(String PIN) {

        int length = PIN.length();
        String len = String.valueOf(length);
        len = (len.length() == 1) ? ("0" + len) : len;
        StringBuffer sb = new StringBuffer();
        sb.append(len);
        sb.append(PIN);
        for (; sb.length() < 16; ) {
            sb.append("F");

        }
        System.out.println("pin:\t" + sb);
        return sb.toString();

    }

    /**
     * 生成卡号的加密串;
     * 取pan银联标准：从卡号的倒数第二位开始往前输12位，前补4个0
     *
     * @param card_no
     * @return 如card_no=6224242000000021  结果：0000424200000002
     */
    private static String builtCardStr(String card_no) {

        StringBuffer sb = new StringBuffer();
        sb.append("0000");
        StringBuffer card_buff = new StringBuffer(card_no);
        card_buff = card_buff.reverse();        //1200000002424226
        card_buff = new StringBuffer(card_buff.substring(1, 13)).reverse();     //200000002424 ->424200000002
        char[] ch = card_buff.toString().toCharArray();
        for (int i = 0; sb.length() < 16; i++) {
            sb.append(ch[i]);
        }
        System.out.println("cardNo:\t" + sb.toString());
        return sb.toString();

    }

    /**
     * 3des  加密
     *
     * @param src       加密数据
     * @param key       密钥
     * @param Algorithm 加密模式
     * @return 3des加密结果
     */

    public static byte[] encryptMode(byte[] src, byte[] key, String Algorithm) {
        try {
            SecretKey deskey = new SecretKeySpec(Algorithm.equals(Algorithm3DES) ? build3DesKey(key) : build3DesKey(key), Algorithm); //
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");//
            cipher.init(Cipher.ENCRYPT_MODE, deskey); //
            return cipher.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 3des 解密
     *
     * @param workKey
     * @return 获取pin 明文
     */
    public static byte[] decryptMode(byte[] workKey, byte[] mainKey, String Algorithm) {
        try {
            SecretKey deskey = new SecretKeySpec(build3DesKey(mainKey), Algorithm);
            Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(workKey);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 生成3des加密串，16字节长的会自动补成24字节
     *
     * @param temp
     * @return 生成3des加密串，16字节长的会自动补成24字节
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(byte[] temp) throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        System.arraycopy(temp, 0, key, 0, temp.length);
        if (temp.length == 16) {
            for (int i = 0; i < 8; i++) {
                key[16 + i] = temp[i];
            }
        }
        return key;
    }


    /**
     * DES加密数据非填充方式
     *
     * @param hexKey
     * @param hexData
     * @param mode
     * @return
     * @throws Exception
     */
    public static String decEncNoPaddingDES(String hexKey, String hexData,
                                            int mode) throws Exception {
        SecretKey desKey = new SecretKeySpec(ConvertUtils.hexStringToBytes(hexKey), AlgorithmDES);

        Cipher cp = Cipher.getInstance("DES/ECB/NoPadding");
        cp.init(mode, desKey);
        byte[] bytes = cp.doFinal(ConvertUtils.hexStringToBytes(hexData));

        return ConvertUtils.bytesToHexString(bytes);
    }

    /**
     * 用key生成加密
     *
     * @param hexKey
     * @param hexData
     * @return
     * @throws Exception
     */

    public static String encrypt(String hexKey, String hexData)
            throws Exception {
        SecretKey desKey = new SecretKeySpec(build3DesKey(ConvertUtils.hexStringToBytes(hexKey)), AlgorithmDES);

        Cipher cp = Cipher.getInstance("DES");
        cp.init(Cipher.ENCRYPT_MODE, desKey);
        byte[] bytes = cp.doFinal(ConvertUtils.hexStringToBytes(hexData));

        return ConvertUtils.bytesToHexString(bytes);
    }

    /**
     * 3Des加密非填充
     *
     * @param hexKey
     * @param hexData
     * @return
     * @throws Exception
     */
    public static String encryptDesSede(String hexKey, String hexData)
            throws Exception {
        SecretKey desKey = new SecretKeySpec(build3DesKey(ConvertUtils.hexStringToBytes(hexKey)), Algorithm3DES);

        Cipher cp = Cipher.getInstance("DESede/ECB/NoPadding");
        cp.init(Cipher.ENCRYPT_MODE, desKey);
        byte[] bytes = cp.doFinal(ConvertUtils.hexStringToBytes(hexData));

        return ConvertUtils.bytesToHexString(bytes);
    }


}
