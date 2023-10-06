package io.github.ithmal.itio.codec.sgip.util;

import io.netty.buffer.ByteBuf;

/**
 * @author: ken.lin
 * @date: 2023/2/4
 */
public class ByteBufUtils {

    public static byte[] toArray(ByteBuf buf, int length) {
        byte[] result = new byte[length];
        buf.readBytes(result);
        return result;
    }

    public static long readInt96(ByteBuf byteBuf) {
        byte[] bytes = new byte[12];
        byteBuf.readBytes(bytes);
        return BytesUtils.toInt96(bytes);
    }
}
