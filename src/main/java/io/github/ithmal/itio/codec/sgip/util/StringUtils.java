package io.github.ithmal.itio.codec.sgip.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author: ken.lin
 * @since: 2023-10-01 12:11
 */
public class StringUtils {

    public static byte[] toBytes(String str, int length, Charset charset){
        return ensureLength(str,length).getBytes(charset);
    }

    public static byte[] toBytes(String str, int length){
        return ensureLength(str,length).getBytes(StandardCharsets.US_ASCII);
    }


    public static String readString(ByteBuf byteBuf, int length, Charset charset){
        CharSequence charSequence = byteBuf.readCharSequence(length, charset);
        return charSequence.toString().trim();
    }


    public static String readString(ByteBuf byteBuf, int length){
        return readString(byteBuf, length, StandardCharsets.US_ASCII);
    }

    private static String ensureLength(String str, int length) {
        if (str == null) {
            return new String(new char[length]);
        }
        if (str.length() == length) {
            return str;
        }
        char[] chars = new char[length];
        for (int i = 0; i < length && i < str.length(); i++) {
            chars[i] = str.charAt(i);
        }
        return new String(chars);
    }

}
