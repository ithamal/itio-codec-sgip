package io.github.ithmal.itio.codec.sgip.handler;

import io.github.ithmal.itio.codec.sgip.base.Command;
import io.github.ithmal.itio.codec.sgip.base.SgipMessage;
import io.github.ithmal.itio.codec.sgip.handler.codec.*;
import io.github.ithmal.itio.codec.sgip.util.ByteBufUtils;
import io.github.ithmal.itio.codec.sgip.util.BytesUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 编解码器
 * @author: ken.lin
 * @date: 2023/2/4
 **/
@ChannelHandler.Sharable
public class SgipMessageCodec extends ChannelDuplexHandler {

    private final ConcurrentHashMap<Command, IMessageCodec<? extends SgipMessage>> codecMap = new ConcurrentHashMap<>(16);

    private static final Logger logger = LoggerFactory.getLogger(SgipMessageCodec.class);

    private final int HEAD_LENGTH = 4 + 4 + 12;

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            super.channelRead(ctx, msg);
            return;
        }
        try {
            ByteBuf in = (ByteBuf) msg;
            List<SgipMessage> out = new ArrayList<>(3);
            decode(ctx, in, out);
            for (SgipMessage message : out) {
                super.channelRead(ctx, message);
            }
        } catch (Throwable cause) {
            exceptionCaught(ctx, cause);
        } finally {
            if (!ReferenceCountUtil.release(msg)) {
                ((ByteBuf) msg).release();
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof SgipMessage)) {
            super.write(ctx, msg, promise);
            return;
        }
        try {
            ByteBuf out = encode(ctx, (SgipMessage) msg);
            if (out != null) {
                super.write(ctx, out, promise);
            }
        } catch (Throwable cause) {
            exceptionCaught(ctx, cause);
        }
    }


    protected ByteBuf encode(ChannelHandlerContext ctx, SgipMessage msg) throws Exception {
        ByteBuf out = null;
        try {
            Command command = msg.getCommand();
            IMessageCodec<SgipMessage> codec = (IMessageCodec<SgipMessage>) codecMap.get(command);
            if (codec == null) {
                logger.error("can not find codec for command: {}", command);
                out.release();
                out = null;
                return null;
            }
            long sequenceId = msg.getSequenceId();
            int commandId = command.getId();
            int bodyLength = codec.getBodyLength(ctx, msg);
            int totalLength = bodyLength + HEAD_LENGTH;
            out = ctx.alloc().buffer(totalLength);
            out.writeInt(totalLength);
            out.writeInt(commandId);
            out.writeBytes(BytesUtils.fromInt96(sequenceId));
            int beforeReadIndex = out.readableBytes();
            codec.encode(ctx, msg, out);
            int afterReadIndex = out.readableBytes();
            if (afterReadIndex - beforeReadIndex != bodyLength) {
                String message = "data length except for codec: " + msg.getClass()
                        + ", except:" + bodyLength + ",actual:" + (afterReadIndex - afterReadIndex);
                throw new Exception(message);
            }
            return out;
        } catch (Throwable e) {
            if (out != null) {
                out.release();
            }
            exceptionCaught(ctx, e);
            return null;
        }
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<SgipMessage> out) throws Exception {
        for (; ; ) {
            SgipMessage msg = decode(ctx, in);
            if (msg == null) {
                break;
            } else {
                out.add(msg);
            }
        }
    }

    protected SgipMessage decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.readableBytes() < HEAD_LENGTH) {
            return null;
        }
        int totalLength = in.readInt();
        int commandId = in.readInt();
        long sequenceId = ByteBufUtils.readInt96(in);
        int bodyLength = totalLength - HEAD_LENGTH;
        if (in.readableBytes() < bodyLength) {
            // 可能是ChannelOption.SO_RCVBUF、ChannelOption.RCVBUF_ALLOCATOR设置过小
            String message = String.format("readable bytes insufficiently： %s, %s, expect: %s, actual: %s",
                    ctx.channel(), commandId, bodyLength, in.readableBytes());
            throw new Exception(message);
        }
        Command command = Command.of(commandId);
        if (command == null) {
            logger.error("not supported command id： {}", commandId);
            in.readBytes(bodyLength).release();
            return null;
        }
        IMessageCodec<SgipMessage> codec = (IMessageCodec<SgipMessage>) codecMap.get(command);
        if (codec == null) {
            logger.error("can not find codec for commandId: {}", commandId);
            in.readBytes(bodyLength).release();
            return null;
        }
        int beforeReadIndex = in.readerIndex();
        SgipMessage out = codec.decode(ctx, sequenceId, in);
        int afterReadIndex = in.readerIndex();
        if (afterReadIndex - beforeReadIndex != bodyLength) {
            String message = "data length except for codec: " + out.getClass()
                    + ", except:" + bodyLength + ",actual:" + (afterReadIndex - beforeReadIndex);
            throw new Exception(message);
        }
        return out;
    }
}
