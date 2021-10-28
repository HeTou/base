package com.base.baselib.common.subutils.bank;

/**
 * author:zft
 * date:2017/12/4 0004.
 */

public class LongUtils {
    private LongUtils() {
    }

    public static long longFromByteArray(byte[] bytes, int offset) {
        long out = 0L;
        out |= (long)ByteUtils.toUnsigned(bytes[offset + 0]) << 56;
        out |= (long)ByteUtils.toUnsigned(bytes[offset + 1]) << 48;
        out |= (long)ByteUtils.toUnsigned(bytes[offset + 2]) << 40;
        out |= (long)ByteUtils.toUnsigned(bytes[offset + 3]) << 32;
        out |= (long)ByteUtils.toUnsigned(bytes[offset + 4]) << 24;
        out |= (long)ByteUtils.toUnsigned(bytes[offset + 5]) << 16;
        out |= (long)ByteUtils.toUnsigned(bytes[offset + 6]) << 8;
        out |= (long)ByteUtils.toUnsigned(bytes[offset + 7]) << 0;
        return out;
    }

    public static byte[] byteArrayFromLong(long l) {
        byte[] out = new byte[8];
        longIntoByteArray(l, 0, out);
        return out;
    }

    public static void longIntoByteArray(long l, int offset, byte[] bytes) {
        bytes[offset + 0] = (byte)((int)(l >>> 56 & 255L));
        bytes[offset + 1] = (byte)((int)(l >>> 48 & 255L));
        bytes[offset + 2] = (byte)((int)(l >>> 40 & 255L));
        bytes[offset + 3] = (byte)((int)(l >>> 32 & 255L));
        bytes[offset + 4] = (byte)((int)(l >>> 24 & 255L));
        bytes[offset + 5] = (byte)((int)(l >>> 16 & 255L));
        bytes[offset + 6] = (byte)((int)(l >>> 8 & 255L));
        bytes[offset + 7] = (byte)((int)(l >>> 0 & 255L));
    }

    public static int fullHashLong(long l) {
        return hashLong(l);
    }

    public static int hashLong(long l) {
        return (int)l ^ (int)(l >>> 32);
    }
}
