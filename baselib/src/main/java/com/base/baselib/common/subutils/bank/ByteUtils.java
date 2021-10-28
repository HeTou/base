package com.base.baselib.common.subutils.bank;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * author:zft
 * date:2017/12/4 0004.
 */

public class ByteUtils {
    public static final short UNSIGNED_MAX_VALUE = 255;

    public static short toUnsigned(byte b) {
        return (short)(b < 0?256 + b:b);
    }

    public static String toHexAscii(byte b) {
        StringWriter sw = new StringWriter(2);
        addHexAscii(b, sw);
        return sw.toString();
    }

    public static String toHexAscii(byte[] bytes) {
        int len = bytes.length;
        StringWriter sw = new StringWriter(len * 2);

        for(int i = 0; i < len; ++i) {
            addHexAscii(bytes[i], sw);
        }

        return sw.toString();
    }

    public static byte[] fromHexAscii(String s) throws NumberFormatException {
        try {
            int len = s.length();
            if(len % 2 != 0) {
                throw new NumberFormatException("Hex ascii must be exactly two digits per byte.");
            } else {
                int out_len = len / 2;
                byte[] out = new byte[out_len];
                int i = 0;

                int val;
                for(StringReader sr = new StringReader(s); i < out_len; out[i++] = (byte)val) {
                    val = 16 * fromHexDigit(sr.read()) + fromHexDigit(sr.read());
                }

                return out;
            }
        } catch (IOException var7) {
            throw new InternalError("IOException reading from StringReader?!?!");
        }
    }

    static void addHexAscii(byte b, StringWriter sw) {
        short ub = toUnsigned(b);
        int h1 = ub / 16;
        int h2 = ub % 16;
        sw.write(toHexDigit(h1));
        sw.write(toHexDigit(h2));
    }

    private static int fromHexDigit(int c) throws NumberFormatException {
        if(c >= 48 && c < 58) {
            return c - 48;
        } else if(c >= 65 && c < 71) {
            return c - 55;
        } else if(c >= 97 && c < 103) {
            return c - 87;
        } else {
            throw new NumberFormatException(39 + c + "' is not a valid hexadecimal digit.");
        }
    }

    private static char toHexDigit(int h) {
        char out;
        if(h <= 9) {
            out = (char)(h + 48);
        } else {
            out = (char)(h + 55);
        }

        return out;
    }

    private ByteUtils() {
    }
}
