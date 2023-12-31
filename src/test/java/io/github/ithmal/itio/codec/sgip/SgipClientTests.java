package io.github.ithmal.itio.codec.sgip;

import io.github.ithaml.itio.client.ItioClient;
import io.github.ithmal.itio.codec.sgip.base.MsgContent;
import io.github.ithmal.itio.codec.sgip.base.MsgFormat;
import io.github.ithmal.itio.codec.sgip.handler.SgipMessageCodec;
import io.github.ithmal.itio.codec.sgip.message.BindRequest;
import io.github.ithmal.itio.codec.sgip.message.BindResponse;
import io.github.ithmal.itio.codec.sgip.message.SubmitRequest;
import io.github.ithmal.itio.codec.sgip.message.SubmitResponse;
import io.github.ithmal.itio.codec.sgip.sequence.SequenceManager;
import io.github.ithmal.itio.codec.sgip.util.LongSmsUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: ken.lin
 * @since: 2023-10-01 10:34
 */
public class SgipClientTests {

    @Test
    public void testConnect() throws InterruptedException {
//        String host = "123.249.84.242";
//        int port = 8801;
        String host = "127.0.0.1";
        int port = 8801;
        String userName = "301001";
        String password = "2ymsc7";
        String sourceId = "106908887002";
        //
        SequenceManager sequenceManager = new SequenceManager();
        ItioClient client = new ItioClient();
        client.registerCodecHandler(new SgipMessageCodec());
        client.registerBizHandler(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println(msg);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                cause.printStackTrace();
                ctx.close();
            }
        });
        client.connect(host, port);
        System.out.println("已打开连接");
        // 请求
        BindRequest bindRequest = new BindRequest(sequenceManager.nextValue());
        bindRequest.setLoginType((short) 1);
        bindRequest.setUserName(userName);
        bindRequest.setPassword(password);
        System.out.println("已请求");
        BindResponse bindResponse = client.writeWaitResponse(bindRequest, BindResponse.class);
        System.out.println("已响应");
        System.out.println(bindResponse);
        if (bindResponse.getResult() == 0) {
            System.out.println("连接成功");
        }
        SubmitRequest submitRequest = new SubmitRequest(System.currentTimeMillis());
        submitRequest.setSpNumber(sourceId);
        submitRequest.setChargeNumber(userName);
        submitRequest.setUserNumbers(new String[]{"8613924604900"});
        submitRequest.setReportFlag((short) 1);
        submitRequest.setCorpId(userName);
        submitRequest.setTpPid((short) 0);
        submitRequest.setTpUdhi((short) 0);
//        submitRequest.setMsgContent(MsgContent.fromText("【测试签名】测试信息", MsgFormat.UCS2));
        submitRequest.setMsgContent(MsgContent.fromText("【测试签名】移动CMPP短信测试{time}移动CMPP短信测试{time}移动CMPP短信测试{time}移动CMPP短信测试{time}移动CMPP短信测试{time" +
                "}移动CMPP短信测试{time}移动CMPP短信测试{time}", MsgFormat.UCS2));
        // 长短信分割处理
        List<SubmitRequest> submitRequests = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            long sequenceId = sequenceManager.nextValue();
            for (SubmitRequest request : LongSmsUtils.split(submitRequest)) {
                request.setSequenceId(sequenceId);
                submitRequests.add(request);
                System.out.println("提交请求:" + request);
            }
        }
        List<SubmitResponse> submitResponses = client.writeWaitResponses(submitRequests, SubmitResponse.class);
        for (SubmitResponse submitResponse : submitResponses) {
            System.out.println("提交响应: " + submitResponse);
        }
        TimeUnit.SECONDS.sleep(30);
        client.disconnect();
        System.out.println("已断开连接");
    }
}
