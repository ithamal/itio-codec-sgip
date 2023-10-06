package io.github.ithmal.itio.codec.sgip.handler;

import io.github.ithmal.itio.codec.sgip.base.SgipMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-01 08:27
 */
public interface IMessageCodec<T extends SgipMessage> {

    /**
     * 解码
     */
    T decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception;

    /**
     * 编码
     */
    void encode(ChannelHandlerContext ctx, T msg, ByteBuf byteBuf) throws Exception;
}
