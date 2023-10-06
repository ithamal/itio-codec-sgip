package io.github.ithmal.itio.codec.sgip;

import io.github.ithaml.itio.server.ItioServer;
import io.github.ithmal.itio.codec.sgip.base.MsgContent;
import io.github.ithmal.itio.codec.sgip.base.MsgFormat;
import io.github.ithmal.itio.codec.sgip.handler.SgipMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author: ken.lin
 * @since: 2023-10-01 10:34
 */
public class SgipServerTests {

    @Test
    public void testListen() throws InterruptedException {
        int port = 8801;
        //
        ItioServer server = new ItioServer();
        server.registerCodecHandler(ch -> new SgipMessageCodec());
        // 连接请求
        server.registerBizHandler(ch -> new SimpleChannelInboundHandler<BindRequest>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, BindRequest msg) throws Exception {
                BindResponse response = new BindResponse(msg.getSequenceId());
                response.setResult((short) 0);
                ctx.writeAndFlush(response);
            }
        });
        // 接受消息
        server.registerBizHandler(ch -> new SimpleChannelInboundHandler<SubmitRequest>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, SubmitRequest msg) throws Exception {
                System.out.println("接收到消息:" + msg);
                SubmitResponse response = new SubmitResponse(msg.getSequenceId());
                ctx.writeAndFlush(response);
                // 报告
                if (msg.getReportFlag() == 1) {
                    ReportRequest reportRequest = new ReportRequest(1);
                    reportRequest.setSubmitSequenceNumber(msg.getSequenceId());
                    reportRequest.setReportType((short) 0);
                    reportRequest.setUserNumber(msg.getUserNumbers()[0]);
                    reportRequest.setState((short) 2);
                    reportRequest.setErrorCode((short) 10);
                    ctx.writeAndFlush(reportRequest);
                }
                // 上行
                if (msg.getReportFlag() == 1) {
                    DeliverRequest deliverRequest = new DeliverRequest(2);
                    deliverRequest.setSpNumber(msg.getSpNumber());
                    deliverRequest.setUserNumber(msg.getUserNumbers()[0]);
                    deliverRequest.setTpPid((short) 0);
                    deliverRequest.setTpPid((short) 0);
                    deliverRequest.setMsgContent(MsgContent.fromText("回复短信X", MsgFormat.UCS2));
                    ctx.writeAndFlush(deliverRequest);
                }
            }

        });
        server.listen(port);
        System.out.println("已监听端口");
        TimeUnit.SECONDS.sleep(1800);
    }
}
