package io.github.ithmal.itio.codec.sgip.util;

import java.math.BigInteger;

/**
 * 公用的工具类
 *
 * @author liuyanning
 */
public final class BytesUtils {

    public static byte[] ensureLength(byte[] array, int minLength) {
        return ensureLength(array, minLength, 0);
    }

    /**
     * 保证byte数组的长度
     *
     * @param array     原数组
     * @param minLength 最小长度
     * @param padding   扩展长度
     * @return
     */
    public static byte[] ensureLength(byte[] array, int minLength, int padding) {
        if (array.length == minLength) {
            return array;
        }
        return array.length > minLength ? copyOf(array, minLength) : copyOf(array, minLength + padding);
    }


    public static byte[] fromInt96(long number) {
        byte[] bytes = new byte[12];
        bytes[0] = (byte) ((number >> 88) & 0xFF);
        bytes[1] = (byte) ((number >> 80) & 0xFF);
        bytes[2] = (byte) ((number >> 72) & 0xFF);
        bytes[3] = (byte) ((number >> 64) & 0xFF);
        bytes[4] = (byte) ((number >> 56) & 0xFF);
        bytes[5] = (byte) ((number >> 48) & 0xFF);
        bytes[6] = (byte) ((number >> 40) & 0xFF);
        bytes[7] = (byte) ((number >> 32) & 0xFF);
        bytes[8] = (byte) ((number >> 24) & 0xFF);
        bytes[9] = (byte) ((number >> 16) & 0xFF);
        bytes[10] = (byte) ((number >> 8) & 0xFF);
        bytes[11] = (byte) (number & 0xFF);
        return bytes;
    }

    public static long toInt96(byte[] bytes) {
        return new BigInteger(bytes).longValue();
    }

    /**
     * original扩展为length长度的数组，右侧默认0
     *
     * @param original
     * @param length
     * @return
     */
    private static byte[] copyOf(byte[] original, int length) {
        byte copy[] = new byte[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }

    public static void main(String[] args) {
        System.out.println(toInt96(fromInt96(Long.MAX_VALUE)) == Long.MAX_VALUE);
    }
}
