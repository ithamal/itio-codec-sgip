package io.github.ithmal.itio.codec.sgip.handler;

import io.github.ithmal.itio.codec.sgip.base.Command;
import io.github.ithmal.itio.codec.sgip.base.SgipMessage;
import io.github.ithmal.itio.codec.sgip.handler.codec.*;
import io.github.ithmal.itio.codec.sgip.util.ByteBufUtils;
import io.github.ithmal.itio.codec.sgip.util.BytesUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 编解码器
 * @author: ken.lin
 * @date: 2023/2/4
 **/
@ChannelHandler.Sharable
public class SgipMessageCodec extends MessageToMessageCodec<ByteBuf, SgipMessage> {

    private final ConcurrentHashMap<Command, IMessageCodec<? extends SgipMessage>> codecMap = new ConcurrentHashMap<>(16);

    private static final Logger logger = LoggerFactory.getLogger(SgipMessageCodec.class);

    public SgipMessageCodec() {
        codecMap.put(Command.BIND_REQUEST, new BindRequestMessageCodec());
        codecMap.put(Command.BIND_RESPONSE, new BindResponseMessageCodec());
        codecMap.put(Command.UNBIND_REQUEST, new UnBindRequestMessageCodec());
        codecMap.put(Command.UNBIND_RESPONSE, new UnBindResponseMessageCodec());
        codecMap.put(Command.SUBMIT_REQUEST, new SubmitRequestMessageCodec());
        codecMap.put(Command.SUBMIT_RESPONSE, new SubmitResponseMessageCodec());
        codecMap.put(Command.DELIVER_REQUEST, new DeliverRequestMessageCodec());
        codecMap.put(Command.DELIVER_RESPONSE, new DeliverResponseMessageCodec());
        codecMap.put(Command.REPORT_REQUEST, new ReportRequestMessageCodec());
        codecMap.put(Command.REPORT_RESPONSE, new ReportResponseMessageCodec());
        codecMap.put(Command.CHECK_USER_REQUEST, new CheckUserRequestMessageCodec());
        codecMap.put(Command.CHECK_USER_RESPONSE, new CheckUserResponseMessageCodec());
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SgipMessage msg, List<Object> out) throws Exception {
        try {
            Command command = msg.getCommand();
            long sequenceId = msg.getSequenceId();
            int commandId = command.getId();
            //消息总长度
            int bodyLength = msg.getLength();
            int totalLength = bodyLength + (4 + 4 + 12);
            ByteBuf byteBuf = ctx.alloc().buffer(totalLength);
            byteBuf.writeInt(totalLength);
            byteBuf.writeInt(commandId);
            byteBuf.writeBytes(BytesUtils.fromInt96(sequenceId));
            IMessageCodec codec = codecMap.get(command);
            if (codec == null) {
                logger.error("Cann't find codec for commandId: {}", commandId);
                return;
            }
            int beforeByteCount = byteBuf.readableBytes();
            codec.encode(ctx, msg, byteBuf);
            int afterByteCount = byteBuf.readableBytes();
            if (afterByteCount - beforeByteCount != bodyLength) {
                String message = "data length except for codec: " + msg.getClass()
                        + ", except:" + bodyLength + ",actual:" + (afterByteCount - beforeByteCount);
                throw new Exception(message);
            }
            out.add(byteBuf);
        } catch (Throwable e) {
            exceptionCaught(ctx, e);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf bytebuf, List<Object> out) throws Exception {
        try {
            int totalLength = bytebuf.readInt();
            int commandId = bytebuf.readInt();
            long sequenceId = ByteBufUtils.readInt96(bytebuf);
            Command command = Command.of(commandId);
            if (command == null) {
                logger.error("Not supported command id： {}", commandId);
                return;
            }
            IMessageCodec codec = codecMap.get(command);
            if (codec == null) {
                logger.error("Cann't find codec for commandId: {}", commandId);
                return;
            }
            SgipMessage message = codec.decode(ctx, sequenceId, bytebuf);
            out.add(message);
        } catch (Throwable e) {
            exceptionCaught(ctx, e);
        }
    }
}
