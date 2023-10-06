package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.BindRequest;
import io.github.ithmal.itio.codec.sgip.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class BindRequestMessageCodec implements IMessageCodec<BindRequest> {

    @Override
    public BindRequest decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        BindRequest msg = new BindRequest(sequenceId);
        msg.setLoginType(byteBuf.readByte());
        msg.setUserName(StringUtils.readString(byteBuf, 16));
        msg.setPassword(StringUtils.readString(byteBuf, 16));
        msg.setReserve(StringUtils.readString(byteBuf, 8));
        return msg;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, BindRequest msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(msg.getLoginType());
        byteBuf.writeBytes(StringUtils.toBytes(msg.getUserName(), 16));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getPassword(), 16));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getReserve(), 8));
    }
}
