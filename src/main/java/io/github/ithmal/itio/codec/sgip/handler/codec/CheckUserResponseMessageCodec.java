package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.CheckUserResponse;
import io.github.ithmal.itio.codec.sgip.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class CheckUserResponseMessageCodec implements IMessageCodec<CheckUserResponse> {

    @Override
    public CheckUserResponse decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        CheckUserResponse msg = new CheckUserResponse(sequenceId);
        msg.setResult(byteBuf.readByte());
        msg.setStatus(byteBuf.readByte());
        msg.setReserve(StringUtils.readString(byteBuf, 8));
        return msg;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, CheckUserResponse msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(msg.getResult());
        byteBuf.writeByte(msg.getStatus());
        byteBuf.writeBytes(StringUtils.toBytes(msg.getReserve(), 8));
    }

    @Override
    public int getBodyLength(ChannelHandlerContext ctx, CheckUserResponse msg) {
        return 10;
    }
}
