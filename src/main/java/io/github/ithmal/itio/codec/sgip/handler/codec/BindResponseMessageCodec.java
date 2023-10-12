package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.BindResponse;
import io.github.ithmal.itio.codec.sgip.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class BindResponseMessageCodec implements IMessageCodec<BindResponse> {

    @Override
    public BindResponse decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        BindResponse msg = new BindResponse(sequenceId);
        msg.setResult(byteBuf.readByte());
        msg.setReserve(StringUtils.readString(byteBuf, 8));
        return msg;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, BindResponse msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(msg.getResult());
        byteBuf.writeBytes(StringUtils.toBytes(msg.getReserve(), 8));
    }

    @Override
    public int getBodyLength(ChannelHandlerContext ctx, BindResponse msg) {
        return 9;
    }
}
