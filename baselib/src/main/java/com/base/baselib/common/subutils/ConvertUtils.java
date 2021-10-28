package com.base.baselib.common.subutils;



import androidx.annotation.Nullable;

import com.base.baselib.common.utils.StringUtil;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * author:zft
 * date:2017/11/21 0021.
 * 转换工具类
 */

public class ConvertUtils {

    public static final String DEFAULT_CHARSET_NAME = "GBK";
    public static final String EMPTY = "";
    public static final Charset DEFAULT_CHARSET = Charset.forName("GBK");

    /***
     *  hexString转成byte数组 （数组长度为（hexString.length()/2））
     *  比较： "1A".getBytes()         = byte[]{49,65},
     *          hexStringToBytes("1A") = byte[]{26}          //数据量小
     *  注意hexString只能是1 ~ F 组成
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (isEmpty(hexString)) {     //如果为空就返回 空byte数组
            return new byte[0];
        } else {
            hexString = hexString.toUpperCase();                    //     如:  1A       两个最大的数为 FF  也就是 255  也就是1个byte
            int length = hexString.length() / 2;                    //      1  属于高4位，A 属于 低4位
            char[] hexChars = hexString.toCharArray();              //     0001 0000
            byte[] d = new byte[length];                            //     0000 1010
            //                                                             0001 1010   =   26
            for (int i = 0; i < length; ++i) {                      //
                int pos = i << 1;                                   //
                d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }

            return d;
        }
    }

    /**
     * byte 数组转成 hexString
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src != null && src.length > 0) {                         //   如 byte[]{26}  =  1A
            for (int i = 0; i < src.length; ++i) {                   //   直接 数字转成 16进制显示即可，个位数前面补0
                String hv = Integer.toHexString(src[i] & 255);    //
                if (hv.length() < 2) {                               //
                    stringBuilder.append(0);                         //
                }                                                    //
                stringBuilder.append(hv);                            //
            }
            return stringBuilder.toString();
        } else {
            return null;
        }
    }



    /*
    *
    * BCD代码。Binary-Coded Decimal‎，简称BCD，称BCD码或二-十进制代码，亦称二进码十进数。是一种二进制的数字编码形式，用二进制编码的十进制代码
    *
    *       ||================================================||
    *       ||                                                ||
    *       ||    十进制数	8421BCD码 	2421BCD码	余3码     ||
    *       ||        0		  0000		  0000		0011      ||
    *       ||        1		  0001		  0001		0100      ||
    *       ||        2		  0010		  0010		0101      ||
    *       ||        3		  0011		  0011		0110      ||
    *       ||        4		  0100		  0100		0111      ||
    *       ||        5		  0101		  1011		1000      ||
    *       ||        6		  0110		  1100		1001      ||
    *       ||        7		  0111		  1101		1010      ||
    *       ||        8		  1000	      1110		1011      ||
    *       ||        9		  1001		  1111		1100      ||
    *       ||                                                ||
    *       ||================================================||
    *||
    * */

    /**
     * bcd 指的是纯数字的转化，方法中的if()else 只为了兼容了hexString2bytes（个人猜想）
     * string bcd 转换  如果 string 是1~A  ,即字母是大写的话，转换的值是跟hexString2bytes的结果是相同的
     *
     * @param s
     * @return
     */
    public static byte[] str2bcdOne(String s) {
        if (s.length() % 2 != 0) {
            s = s + "0";                //长度不为双数,后补0,成双。
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = s.toCharArray();                //转换char数组
        int high = 0;     //高4位
        int low = 0;      //低4位
        for (int i = 0; i < cs.length; i += 2) {
            if ((cs[i] >= '0') && (cs[i] <= '9'))       //在'0' ~ '9' 之间
                high = cs[i] - '0';
            else {
                high = cs[i] - '0' - 7;
            }

            if ((cs[(i + 1)] >= '0') && (cs[(i + 1)] <= '9'))
                low = cs[(i + 1)] - '0';
            else {
                low = cs[(i + 1)] - '0' - 7;
            }
            baos.write(high << 4 | low);
        }
        return baos.toByteArray();
    }

    public static byte[] str2bcdTwo(String s) {
        if (s.length() % 2 != 0) {
            s = "0" + s;                 //长度不为双数,前补0,成双。
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = s.toCharArray();
        int high = 0;
        int low = 0;
        for (int i = 0; i < cs.length; i += 2) {
            if ((cs[i] >= '0') && (cs[i] <= '9'))
                high = cs[i] - '0';
            else {
                high = cs[i] - '0' - 7;
            }

            if ((cs[(i + 1)] >= '0') && (cs[(i + 1)] <= '9'))
                low = cs[(i + 1)] - '0';
            else {
                low = cs[(i + 1)] - '0' - 7;
            }
            baos.write(high << 4 | low);
        }
        return baos.toByteArray();
    }

    /***
     * bcd数组 转成 string
     * @param b
     * @return
     */
    public static String bcd2Str(byte[] b) {
        StringBuffer sb = new StringBuffer();                                                       //
//                                                                                                      ascII 码中  48 -> '0'        + 48 是为了 显示 将 2 显示为 '2' ;
        for (int i = 0; i < b.length; ++i) {                                                        //  1、BCD码00110010，拆开得到：0011和0010
            int h = (b[i] & 255) >> 4 > 9 ? ((b[i] & 255) >> 4) + 55 : ((b[i] & 255) >> 4) + 48;    //  2、分别变成非压缩BCD码：00000011和00000010，得到十进制数字：3和2
            sb.append((char) h);                                                                    //  3、看ASC表可以得出BCD码0--9转换为ASC码为：48---58，所以对应ASC码值为：51和50
            int l = (b[i] & 15) > 9 ? (b[i] & 15) + 55 : (b[i] & 15) + 48;                          //
            sb.append((char) l);                                                                    //
        }                                                                                           //

        return sb.toString();
    }

    /***
     * 合并两个byte 数组 成    a1+a2
     * @param a1    前数组
     * @param a2    后数组
     * @return
     */
    public static byte[] joinBytes(byte[] a1, byte[] a2) {
        byte[] result = new byte[a1.length + a2.length];
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }


    /***
     * public static void fill(byte[] a, int fromIndex, int toIndex, byte val)
     * 分配指定的字节值指定的字节数组指定范围中的每个元素。要填充的范围从索引fromIndex(包括)到索引toIndex(不包括)。(如果fromIndex== toIndex，则填充范围为空。)
     *
     * public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
     * 用指定数组 指定起始索引 、长度覆盖 另一个数组
     * src -- 源阵列。
     * srcPos -- 源阵列中的起始位置。 （复制的起始索引）
     * dest -- 目标数组。 （被覆盖的数组）
     * destPos -- 是在目标数据的起始位置。 （被覆盖的起始索引）
     * length -- 此是要被复制的数组元素的数量。 （复制的长度）
     */
    /***
     * @param src
     * @param fillByte
     * @param destLen
     * @param flag
     * @return
     */
    public static byte[] fillByte(byte[] src, byte fillByte, int destLen, String flag) {
        byte[] returnByteArray = new byte[destLen];
        if ("R".equals(flag)) {
            System.arraycopy(src, 0, returnByteArray, 0, src.length);
            Arrays.fill(returnByteArray, src.length, destLen, fillByte);
        } else if ("L".equals(flag)) {
            System.arraycopy(src, 0, returnByteArray, returnByteArray.length - src.length, src.length);
            Arrays.fill(returnByteArray, 0, destLen - src.length, fillByte);
        }

        return returnByteArray;
    }


    /**
     * 两个数据进行异或操作
     *
     * @param hexSrcData1 :32CB95B36D89477C
     * @param hexSrcData2 :3030000000000000
     * @return
     */
    public static String XOR(String hexSrcData1, String hexSrcData2) {
        if (hexSrcData1.length() != hexSrcData2.length()) {
            throw new IllegalArgumentException("异或的两个数据长度不相等，请检查数据!");
        }
        byte[] bytes1 = hexStringToBytes(hexSrcData1);
        byte[] bytes2 = hexStringToBytes(hexSrcData2);

        ByteBuffer buffer = ByteBuffer.allocate(bytes2.length);     //创建一个容量为bytes2.length字节的ByteBuffer,如果发现创建的缓冲区容量太小,唯一的选择就是重新创建一个大小合适的缓冲区.

        for (int i = 0; i < bytes2.length; i++) {
            byte temp = (byte) ((int) bytes1[i] ^ (int) bytes2[i]);
            buffer.put(temp);
        }

        return bytesToHexString(buffer.array());
    }

    public static String xor(String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        byte[] buf1 = s1.getBytes();
        byte[] buf2 = s2.getBytes();

        for(int i = 0; i < s1.length(); ++i) {
            sb.append(buf1[i] ^ buf2[i]);
        }

        return sb.toString();
    }

    public static byte[] xor(byte[] s1, byte[] s2, byte defaultByte) {
        int maxLen = s1.length > s2.length?s1.length:s2.length;
        byte[] tmp = new byte[maxLen];

        for(int i = 0; i < maxLen; ++i) {
            tmp[i] = (byte)((i < s1.length?s1[i]:defaultByte) ^ (i < s2.length?s2[i]:defaultByte));
        }

        return tmp;
    }

    public static byte[] stringToBytes(String str, Charset[] charsets) {
        return StringUtil.isEmpty(str) ? new byte[0] : (charsets != null && charsets.length == 1 ? str.getBytes(charsets[0]) : str.getBytes(DEFAULT_CHARSET));
    }

    public static String bytesToString(byte[] data, Charset[] charsets) {
        return data != null && data.length != 0 ? (charsets != null && charsets.length == 1 ? new String(data, charsets[0]) : new String(data, DEFAULT_CHARSET)) : "";
    }

    public static String stringToHexString(String strPart) {
        StringBuffer hexStringBuffer = new StringBuffer("");

        for (int i = 0; i < strPart.length(); ++i) {
            int ch = strPart.charAt(i);
            String strHex = fill(Integer.toHexString(ch), "0", Integer.valueOf(2), "L");
            hexStringBuffer.append(strHex);
        }

        return hexStringBuffer.toString();
    }


    //   _____________以下是一些处理方法____________________________________________________________
    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }


    public static String fill(String source, String fillStr, Integer destLen, String fillPos) {
        if (fillStr == null || "".equals(fillStr)) {
            fillStr = " ";
        }

        StringBuffer dest = new StringBuffer(source);

        for (int i = stringToBytes(source, new Charset[0]).length; i < destLen.intValue(); i += stringToBytes(fillStr, new Charset[0]).length) {
            if ("L".equals(fillPos)) {
                dest.insert(0, fillStr);
            } else {
                dest.append(fillStr);
            }
        }

        byte[] returnBytes = stringToBytes(dest.toString(), new Charset[0]);
        return bytesToString("R".equals(fillPos) ? Arrays.copyOfRange(returnBytes, 0, destLen.intValue()) : Arrays.copyOfRange(returnBytes, returnBytes.length - destLen.intValue(), returnBytes.length), new Charset[0]);
    }


}
