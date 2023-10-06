package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.base.MsgContent;
import io.github.ithmal.itio.codec.sgip.base.MsgFormat;
import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.SubmitRequest;
import io.github.ithmal.itio.codec.sgip.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class SubmitRequestMessageCodec implements IMessageCodec<SubmitRequest> {

    @Override
    public SubmitRequest decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        SubmitRequest msg = new SubmitRequest(sequenceId);
        msg.setSpNumber(StringUtils.readString(byteBuf, 21));
        msg.setChargeNumber(StringUtils.readString(byteBuf, 21));
        byte userCount = byteBuf.readByte();
        String[] userNumbers = new String[userCount];
        for (byte i = 0; i < userCount; i++) {
            userNumbers[i] = StringUtils.readString(byteBuf, 21);
        }
        msg.setUserNumbers(userNumbers);
        msg.setCorpId(StringUtils.readString(byteBuf, 5));
        msg.setServiceType(StringUtils.readString(byteBuf, 10));
        msg.setFeeType(byteBuf.readByte());
        msg.setFeeValue(StringUtils.readString(byteBuf, 6));
        msg.setGivenValue(StringUtils.readString(byteBuf, 6));
        msg.setAgentFlag(byteBuf.readByte());
        msg.setMoreLateToMtFlag(byteBuf.readByte());
        msg.setPriority(byteBuf.readByte());
        msg.setExpireTime(StringUtils.readString(byteBuf, 16));
        msg.setScheduleTime(StringUtils.readString(byteBuf, 16));
        msg.setReportFlag(byteBuf.readByte());
        msg.setTpPid(byteBuf.readByte());
        msg.setTpUdhi(byteBuf.readByte());
        MsgFormat msgFormat = MsgFormat.of(byteBuf.readByte());
        msg.setMsgType(byteBuf.readByte());
        msg.setMsgContent(MsgContent.read(byteBuf, msgFormat, msg.getTpUdhi()));
        msg.setReserve(StringUtils.readString(byteBuf, 8));
        return msg;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, SubmitRequest msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(StringUtils.toBytes(msg.getSpNumber(), 21));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getChargeNumber(), 21));
        byteBuf.writeByte(msg.getUserNumbers().length);
        for (String userNumber : msg.getUserNumbers()) {
            byteBuf.writeBytes(StringUtils.toBytes(userNumber, 21));
        }
        byteBuf.writeBytes(StringUtils.toBytes(msg.getCorpId(), 5));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getServiceType(), 10));
        byteBuf.writeByte(msg.getFeeType());
        byteBuf.writeBytes(StringUtils.toBytes(msg.getFeeValue(), 6));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getGivenValue(), 6));
        byteBuf.writeByte(msg.getAgentFlag());
        byteBuf.writeByte(msg.getMoreLateToMtFlag());
        byteBuf.writeByte(msg.getPriority());
        byteBuf.writeBytes(StringUtils.toBytes(msg.getExpireTime(), 16));
        byteBuf.writeBytes(StringUtils.toBytes(msg.getScheduleTime(), 16));
        byteBuf.writeByte(msg.getReportFlag());
        byteBuf.writeByte(msg.getTpPid());
        byteBuf.writeByte(msg.getTpUdhi());
        byteBuf.writeByte(msg.getMsgContent().getFormat().getId());
        byteBuf.writeByte(msg.getMsgType());
        msg.getMsgContent().write(byteBuf);
        byteBuf.writeBytes(StringUtils.toBytes(msg.getReserve(), 8));
    }
}
