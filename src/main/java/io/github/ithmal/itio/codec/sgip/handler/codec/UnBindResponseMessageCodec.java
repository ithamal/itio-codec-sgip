package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.UnBindResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class UnBindResponseMessageCodec implements IMessageCodec<UnBindResponse> {


    @Override
    public UnBindResponse decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        return new UnBindResponse(sequenceId);
    }

    @Override
    public void encode(ChannelHandlerContext ctx, UnBindResponse msg, ByteBuf byteBuf) throws Exception {

    }

    @Override
    public int getBodyLength(ChannelHandlerContext ctx, UnBindResponse msg) {
        return 0;
    }
}
