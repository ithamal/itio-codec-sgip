package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.base.MsgContent;
import io.github.ithmal.itio.codec.sgip.base.MsgFormat;
import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.DeliverRequest;
import io.github.ithmal.itio.codec.sgip.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class DeliverRequestMessageCodec implements IMessageCodec<DeliverRequest> {

    @Override
    public DeliverRequest decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        DeliverRequest msg = new DeliverRequest(sequenceId);
        msg.setUserNumber(StringUtils.readString(byteBuf, 21));
        msg.setSpNumber(StringUtils.readString(byteBuf, 21));
        msg.setTpPid(byteBuf.readByte());
        msg.setTpUdhi(byteBuf.readByte());
        MsgFormat msgFormat = MsgFormat.of(byteBuf.readByte());
        msg.setMsgContent(MsgContent.read(byteBuf, msgFormat, msg.getTpUdhi()));
        msg.setReserve(StringUtils.readString(byteBuf, 8));
        return msg;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, DeliverRequest msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(StringUtils.toBytes(msg.getUserNumber(), 21));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getSpNumber(), 21));
        byteBuf.writeByte(msg.getTpPid());
        byteBuf.writeByte(msg.getTpUdhi());
        byteBuf.writeByte(msg.getMsgContent().getFormat().getId());
        msg.getMsgContent().write(byteBuf);
        byteBuf.writeBytes(StringUtils.toBytes(msg.getReserve(), 8));
    }
}
