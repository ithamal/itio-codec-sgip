package io.github.ithmal.itio.codec.sgip.handler.codec;

import io.github.ithmal.itio.codec.sgip.handler.IMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.ReportResponse;
import io.github.ithmal.itio.codec.sgip.util.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:39
 */
public class ReportResponseMessageCodec implements IMessageCodec<ReportResponse> {

    @Override
    public ReportResponse decode(ChannelHandlerContext ctx, long sequenceId, ByteBuf byteBuf) throws Exception {
        ReportResponse msg = new ReportResponse(sequenceId);
        msg.setResult(byteBuf.readByte());
        msg.setReserve(StringUtils.readString(byteBuf, 8));
        return msg;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, ReportResponse msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(msg.getResult());
        byteBuf.writeBytes(StringUtils.toBytes(msg.getReserve(), 8));
    }

    @Override
    public int getBodyLength(ChannelHandlerContext ctx, ReportResponse msg) {
        return 9;
    }
}
