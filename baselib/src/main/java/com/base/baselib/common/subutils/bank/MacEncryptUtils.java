package com.base.baselib.common.subutils.bank;


import com.base.baselib.common.subutils.ConvertUtils;
import com.base.baselib.common.subutils.encrypt.des.DesUtils;
import com.base.baselib.common.utils.StringUtil;

/***
 * mac 计算工具
 */
public class MacEncryptUtils {
    public static String DESEncrypt(String input, int length, String key, int keylen) {
        if ((StringUtil.isEmpty(input)) || (StringUtil.isEmpty(key))) {
            return null;
        }
        if ((key.length() % 16 != 0) || (length % 16 != 0)) {
            return null;
        }
        if (input.length() < key.length()) {
            while (key.length() > input.length()) {
                input = input + input;
            }
            input = input.substring(0, key.length());
        } else if (input.length() > key.length()) {
            while (key.length() < input.length()) {
                key = key + key;
            }
            key = key.substring(0, input.length());
        }

        String tmpEncrypted = ConvertUtils.bytesToHexString(DesUtils.encrypt(ConvertUtils.hexStringToBytes(input), key)).toUpperCase();
        return tmpEncrypted.substring(0, length);
    }


    /***
     *  des解密码, 获取mac明文
     * @param macKey     mac密钥
     * @param macKeyLength    mac密钥长度
     * @param mainKey       主密钥
     * @return macKey明文
     */
    public static String DESDecrypt(String macKey, int macKeyLength, String mainKey) {
        if ((StringUtil.isEmpty(macKey)) || (StringUtil.isEmpty(mainKey))) {       //结果1： mac密钥或主密钥为空，则返回 null;
            return null;
        }
        if ((mainKey.length() % 16 != 0) || (macKey.length() % 16 != 0)) {          //结果2： mac密钥或主密钥的长度不为16的整数倍，则返回 null;
            return null;
        }
        if (macKey.length() < mainKey.length()) {                   //如果 macKey 长度 比 mainKey 短时, macKey = macKey + macKey;
            while (mainKey.length() > macKey.length()) {
                macKey = macKey + macKey;
            }
            macKey = macKey.substring(0, mainKey.length());         //截取mackey长度，使 macKey和mainKey的长度一致。
        } else if (macKey.length() > mainKey.length()) {           //如果 mainKey 长度 比 macKey 短时, mainKey = mainKey + mainKey;
            while (mainKey.length() < macKey.length()) {
                mainKey = mainKey + mainKey;
            }
            mainKey = mainKey.substring(0, macKey.length());       //截取mainKey长度，使 mainKey 和 macKey 的长度一致。
        }

        String tmpEncrypted = ConvertUtils.bytesToHexString(DesUtils.decrypt(ConvertUtils.hexStringToBytes(macKey), mainKey)).toUpperCase();
        return tmpEncrypted.substring(0, macKeyLength);
    }

    /***
     * 计算mac
     * 流程：
     * 1、 获取mac 密钥明文
     *
     * @param messMac       待加密数据
     * @param messMacLen    待加密数据的长度（messMac.length）
     * @param macCal        0
     * @param mainKey       主密钥
     * @param macKey        mac密钥
     * @return 返回mac 结果
     */
    public static String calcMAC(String messMac, int messMacLen, int macCal, String mainKey, String macKey) {
        if ((StringUtil.isEmpty(messMac)) || (StringUtil.isEmpty(macKey))) {  //结果1：如果待加密数据或mackey为空,则返回null
            return null;
        }
        String decrptedMacKey = macKey;
        if (!StringUtil.isEmpty(mainKey)) {    //如果主密钥不为空
            decrptedMacKey = DESDecrypt(macKey, macKey.length(), mainKey);      //获取mac密钥明文
        }
        if (macCal == 0) {
            byte[] tmpBytes = ConvertUtils.hexStringToBytes(messMac);
            return ConvertUtils.bytesToHexString(xorThenDesByGroup(tmpBytes, 8, decrptedMacKey)).toUpperCase();
        }
        if (1 == macCal) {
            byte[] tmpBytes = ConvertUtils.hexStringToBytes(messMac);
            String messXorMacStr = ConvertUtils.bytesToHexString(xorByGroup(tmpBytes, 8));
            return calcMAC(messXorMacStr, messXorMacStr.length(), 0, mainKey, macKey);
        }
        if (2 == macCal) {
            if (32 != macKey.length()) {
                return null;
            }
            String leftMacKey = decrptedMacKey.substring(0, 16);
            String rightMacKey = decrptedMacKey.substring(16, 32);
            byte[] tmpBytes = ConvertUtils.hexStringToBytes(messMac);
            String tmpResult = ConvertUtils.bytesToHexString(xorThenDesByGroup(tmpBytes, 8, leftMacKey)).toUpperCase();
            tmpResult = DESDecrypt(tmpResult, tmpResult.length(), rightMacKey);
            return DESEncrypt(tmpResult, tmpResult.length(), leftMacKey, leftMacKey.length());
        }
        return null;
    }

    public static String calcSpecMAC(String messMac, int messMacLen, int macCal, String key, String macKey) {
        if ((StringUtil.isEmpty(messMac)) || (StringUtil.isEmpty(macKey))) {
            return null;
        }
        String decrptedMacKey = macKey;
        if (!StringUtil.isEmpty(key)) {
            decrptedMacKey = DESDecrypt(macKey, macKey.length(), key);
        }
        if (macCal == 0) {
            byte[] tmpBytes = ConvertUtils.hexStringToBytes(messMac);
            return ConvertUtils.bytesToHexString(xorThenDesByGroup(tmpBytes, 8, decrptedMacKey)).toUpperCase();
        }
        if (1 == macCal) {
            byte[] tmpBytes = ConvertUtils.hexStringToBytes(messMac);
            String messXorMacStr = ConvertUtils.bytesToHexString(xorByGroup(tmpBytes, 8));
            messXorMacStr = ConvertUtils.stringToHexString(messXorMacStr);
            return calcSpecMAC(messXorMacStr, messXorMacStr.length(), 0, key, macKey);
        }
        if (2 == macCal) {
            if (32 != macKey.length()) {
                return null;
            }
            String leftMacKey = decrptedMacKey.substring(0, 16);
            String rightMacKey = decrptedMacKey.substring(16, 32);
            byte[] tmpBytes = ConvertUtils.hexStringToBytes(messMac);
            String tmpResult = ConvertUtils.bytesToHexString(xorThenDesByGroup(tmpBytes, 8, leftMacKey)).toUpperCase();
            tmpResult = DESDecrypt(tmpResult, tmpResult.length(), rightMacKey);
            return DESEncrypt(tmpResult, tmpResult.length(), leftMacKey, leftMacKey.length());
        }
        return null;
    }

    public static String XOR(String strOne, String strTwo) {
        return ConvertUtils.bytesToHexString(ConvertUtils.xor(ConvertUtils.hexStringToBytes(strOne), ConvertUtils.hexStringToBytes(strTwo), (byte) 0)).toUpperCase();
    }

    public static byte[] xorByGroup(byte[] tmpBytes, int groupLen) {
        int messMacBytesLen = (tmpBytes.length + groupLen - 1) / groupLen * groupLen;

        byte[] messMacBytes = new byte[messMacBytesLen];
        System.arraycopy(tmpBytes, 0, messMacBytes, 0, tmpBytes.length);
        int start = 0;
        long messXorMac = 0L;

        while (start < messMacBytes.length) {
            messXorMac ^= LongUtils.longFromByteArray(messMacBytes, start);
            start += groupLen;
        }
        return LongUtils.byteArrayFromLong(messXorMac);
    }

    /***
     *
     * MAC算法原理(以直联银联pos和POS中心通讯为例）。
     * a) 将欲发送给POS中心的消息中，从消息类型（MTI）到63域之间的部分构成MAC
     * ELEMEMENT BLOCK （MAB）。
     * b) 对MAB，按每8个字节做异或（不管信息中的字符格式），如果最后不满8个字
     * 节，则添加“0X00”。
     *
     * @param messMac     待加密数据
     * @param groupLen
     * @param macKey          mac密钥明文
     * @return     mac byte数组
     */
    public static byte[] xorThenDesByGroup(byte[] messMac, int groupLen, String macKey) {
        int messMacBytesLen = (messMac.length + groupLen - 1) / groupLen * groupLen;

        byte[] messMacBytes = new byte[messMacBytesLen];
        System.arraycopy(messMac, 0, messMacBytes, 0, messMac.length);
        int start = 0;
        long messXorMac = 0L;
        while (start < messMacBytes.length) {
            messXorMac ^= LongUtils.longFromByteArray(messMacBytes, start);
            String messXorMacStr = ConvertUtils.bytesToHexString(LongUtils.byteArrayFromLong(messXorMac));
            String desMessXorMacStr = DESEncrypt(messXorMacStr, messXorMacStr.length(), macKey, macKey.length());
            messXorMac = LongUtils.longFromByteArray(ConvertUtils.hexStringToBytes(desMessXorMacStr), 0);
            start += groupLen;
        }
        return LongUtils.byteArrayFromLong(messXorMac);
    }
}
