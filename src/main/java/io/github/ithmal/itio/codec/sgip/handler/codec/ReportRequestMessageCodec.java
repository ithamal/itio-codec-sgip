package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.ReportRequest;
import io.github.ithmal.itio.codec.sgip.util.ByteBufUtils;
import io.github.ithmal.itio.codec.sgip.util.BytesUtils;
import io.github.ithmal.itio.codec.sgip.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class ReportRequestMessageCodec implements IMessageCodec<ReportRequest> {

    @Override
    public ReportRequest decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        ReportRequest msg = new ReportRequest(sequenceId);
        msg.setSubmitSequenceNumber(ByteBufUtils.readInt96(byteBuf));
        msg.setReportType(byteBuf.readByte());
        msg.setUserNumber(StringUtils.readString(byteBuf, 21));
        msg.setState(byteBuf.readByte());
        msg.setErrorCode(byteBuf.readByte());
        msg.setReserve(StringUtils.readString(byteBuf, 8));
        return msg;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ReportRequest msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(BytesUtils.fromInt96(msg.getSubmitSequenceNumber()));
        byteBuf.writeByte(msg.getReportType());
        byteBuf.writeBytes(StringUtils.toBytes(msg.getUserNumber(), 21));
        byteBuf.writeByte(msg.getState());
        byteBuf.writeByte(msg.getErrorCode());
        byteBuf.writeBytes(StringUtils.toBytes(msg.getReserve(), 8));
    }

    @Override
    public int getBodyLength(ChannelHandlerContext ctx, ReportRequest msg) {
        return 44;
    }
}
