package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.CheckUserRequest;
import io.github.ithmal.itio.codec.sgip.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class CheckUserRequestMessageCodec implements IMessageCodec<CheckUserRequest> {

    @Override
    public CheckUserRequest decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        CheckUserRequest msg = new CheckUserRequest(sequenceId);
        msg.setUserName(StringUtils.readString(byteBuf, 16));
        msg.setPassword(StringUtils.readString(byteBuf, 16));
        msg.setUserNumber(StringUtils.readString(byteBuf, 21));
        msg.setReserve(StringUtils.readString(byteBuf, 8));
        return msg;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, CheckUserRequest msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(StringUtils.toBytes(msg.getUserName(), 16));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getPassword(), 16));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getUserNumber(), 21));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getReserve(), 8));
    }
}
