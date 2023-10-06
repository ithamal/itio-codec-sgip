## itio-codec-sgip
适配Netty框架的SGIP协议


### 客户端
```java
String host = "127.0.0.1";
int port = 8801;
String userName = "301001";
String password = "2ymsc7";
String sourceId = "106908887002";
//
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
    }
});
client.connect(host, port);
System.out.println("已打开连接");
// 请求
BindRequest bindRequest = new BindRequest(System.currentTimeMillis());
bindRequest.setLoginType((short) 1);
bindRequest.setUserName(userName);
bindRequest.setPassword(password);
client.writeAndFlush(bindRequest);
System.out.println("已请求");
BindResponse bindResponse = client.waitForResponse(BindResponse.class);
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
submitRequest.setMsgContent(MsgContent.fromText("【测试签名】测试信息", MsgFormat.UCS2));
//        submitRequest.setContent(MsgContent.fromText("【测试签名】移动CMPP短信测试{time}移动CMPP短信测试{time}移动CMPP短信测试{time}移动CMPP短信测试{time}移动CMPP短信测试{time}移动CMPP短信测试{time}移动CMPP短信测试{time}", MsgFormat.UCS2));
// 长短信分割处理
for (SubmitRequest subSubmitRequest : LongSmsUtils.split(submitRequest)) {
    client.writeAndFlush(subSubmitRequest);
    System.out.println("提交请求");
    SubmitResponse submitResponse = client.waitForResponse(SubmitResponse.class);
    System.out.println("提交响应");
    System.out.println(submitResponse);
}
TimeUnit.SECONDS.sleep(30);
client.disconnect();
System.out.println("已断开连接");
```
### 服务器端
```java
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
```
