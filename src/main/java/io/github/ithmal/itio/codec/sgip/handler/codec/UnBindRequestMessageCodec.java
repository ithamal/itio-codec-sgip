package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.UnBindRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class UnBindRequestMessageCodec implements IMessageCodec<UnBindRequest> {


    @Override
    public UnBindRequest decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        return new UnBindRequest(sequenceId);
    }

    @Override
    public void encode(ChannelHandlerContext ctx, UnBindRequest msg, ByteBuf byteBuf) throws Exception {

    }

    @Override
    public int getBodyLength(ChannelHandlerContext ctx, UnBindRequest msg) {
        return 0;
    }
}
