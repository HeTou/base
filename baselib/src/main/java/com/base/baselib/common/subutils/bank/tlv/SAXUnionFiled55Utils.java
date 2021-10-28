package com.base.baselib.common.subutils.bank.tlv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 *  TLV 解析工具
 */
public class SAXUnionFiled55Utils {
    public static List<TLV> saxUnionField55_2List(String hexfiled55) {

        if (null == hexfiled55) {
            throw new IllegalArgumentException("55域的值不能为空!");
        }

        return builderTLV(hexfiled55);
    }

    /***
     * 解析 tlv 字符串
     * @param hexString  tlv字符串
     * @return
     */
    private static List<TLV> builderTLV(String hexString) {
        List<TLV> tlvs = new ArrayList<TLV>();

        int position = 0;
        while (position != hexString.length()) {
            String _hexTag = getTLVTag(hexString, position);        //获取Tag
            position += _hexTag.length();

            LPositon l_position = getUnionLAndPosition(hexString, position);
            int _vl = l_position.get_vL();

            position = l_position.get_position();

            String _value = hexString.substring(position, position + _vl * 2);

            position = position + _value.length();

            //System.out.println(position+" "+_hexTag+" "+_vl +" "+_value);
            tlvs.add(new TLV(_hexTag, _vl, _value));
        }
        return tlvs;
    }

    public static Map<String, TLV> saxUnionField55_2Map(String hexfiled55) {

        if (null == hexfiled55) {
            throw new IllegalArgumentException("55域的值不能为空!");
        }

        return builderKeyAndTLV(hexfiled55);
    }

    /***
     * 解析 TLV  hexString
     * @param hexString
     * @return 返回键值对 Map
     */
    public static Map<String, TLV> builderKeyAndTLV(String hexString) {

        Map<String, TLV> tlvs = new HashMap<String, TLV>();

        int position = 0;
        while (position != hexString.length()) {
            String _hexTag = getTLVTag(hexString, position);
            position += _hexTag.length();
            LPositon l_position = getUnionLAndPosition(hexString, position);
            int _vl = l_position.get_vL();
            position = l_position.get_position();
            String _value = hexString.substring(position, position + _vl * 2);
            position = position + _value.length();
            tlvs.put(_hexTag, new TLV(_hexTag, _vl, _value));
        }
        return tlvs;
    }

    /**
     * @param hexString
     * @param position
     * @return
     */
    private static LPositon getUnionLAndPosition(String hexString, int position) {
        String firstByteString = hexString.substring(position, position + 2);   //一个字节的长度标识
        int i = Integer.parseInt(firstByteString, 16);                   //tagValue的长度
        String hexLength = "";

        if (((i >>> 7) & 1) == 0) {
            hexLength = hexString.substring(position, position + 2);
            position = position + 2;

        } else {
            int _L_Len = i & 127;
            position = position + 2;
            hexLength = hexString.substring(position, position + _L_Len * 2);
            position = position + _L_Len * 2;

        }
        return new LPositon(Integer.parseInt(hexLength, 16), position);

    }

    /***
     * 获取 tlv 的tag
     * @param hexString
     * @param position
     * @return
     */
    private static String getTLVTag(String hexString, int position) {
        String firstByte = hexString.substring(position, position + 2);
        int i = Integer.parseInt(firstByte, 16); //输出16进制的int
        if ((i & 0x1f) == 0x1f) {     //判断 tag 是否是9F开头， 是则 占 4个长度， 反之  2个长度
            return hexString.substring(position, position + 4);            // 9F        10011111
        } else {                                                           // 1F        00011111
            return hexString.substring(position, position + 2);            // result    00011111     =  1F
        }

    }

    static class LPositon {
        private int _vL;
        private int _position;

        public LPositon(int _vL, int position) {
            this._vL = _vL;
            this._position = position;
        }

        public int get_vL() {
            return _vL;
        }

        public void set_vL(int _vL) {
            this._vL = _vL;
        }

        public int get_position() {
            return _position;
        }

        public void set_position(int _position) {
            this._position = _position;
        }

    }

    public static void main(String[] args) {
        String s = "9F2608054BD0DDE9B7182E9F2701809F101307010103A0A800010A0100000487998A02B2A59F3704EFABC99A9F360201DA9505080004E0009A031709079C01009F02060000000003145F2A02015682027C009F1A0201569F03060000000000009F3303E0E1C89F34034203009F3501229F1E0831323334353637388408A0000003330101029F090200309F4104000000154F08A0000003330101025F280201569B02E800500B50424F43204352454449549F120B50424F43204352454449549F4E0F0000000000000000000000000000009F7B060000001000009F77060000001000008F010B5A0862242420000000215F34010057116224242000000021D29022015699031007";
        Map<String, TLV> saxUnionField55_2Map = saxUnionField55_2Map(s);
        System.out.println(saxUnionField55_2Map.toString());
    }

}

