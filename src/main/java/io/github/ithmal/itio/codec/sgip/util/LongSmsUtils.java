package io.github.ithmal.itio.codec.sgip.util;

import io.github.ithmal.itio.codec.sgip.base.MsgContent;
import io.github.ithmal.itio.codec.sgip.base.MsgFormat;
import io.github.ithmal.itio.codec.sgip.base.UserDataHeader;
import io.github.ithmal.itio.codec.sgip.message.DeliverRequest;
import io.github.ithmal.itio.codec.sgip.message.SubmitRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author: ken.lin
 * @since: 2023-10-06 11:27
 */
public class LongSmsUtils {

    public static List<SubmitRequest> split(SubmitRequest msg) {
        MsgContent msgContent = msg.getMsgContent();
        if (msgContent.getMsgLength() <= msgContent.getLimitMsgLength()) {
            return Collections.singletonList(msg);
        }
        // 分割
        long timeNow = System.currentTimeMillis();
        int bodyRealLength = msgContent.getBody().length;
        int bodyLimitLength = msg.getMsgContent().getLimitMsgLength() - 6;
        short pkTotal = (short) Math.ceil((double) bodyRealLength / bodyLimitLength);
        List<SubmitRequest> out = new ArrayList<>(pkTotal);
        for (short i = 1; i <= pkTotal; i++) {
            int from = (i - 1) * bodyLimitLength;
            byte[] body = Arrays.copyOfRange(msgContent.getBody(), from, from + bodyLimitLength);
            short msgId = (short) (timeNow % 255);
            UserDataHeader udh = UserDataHeader.six(msgId, pkTotal, i);
            MsgContent subContent = new MsgContent(udh, msgContent.getFormat(), body);
            SubmitRequest subRequest = msg.copy();
            subRequest.setTpUdhi((short) 1);
            subRequest.setMsgContent(subContent);
            out.add(subRequest);
        }
        return out;
    }

    public static List<DeliverRequest> split(DeliverRequest msg) {
        MsgContent msgContent = msg.getMsgContent();
        if (msgContent == null || msgContent.getMsgLength() <= msgContent.getLimitMsgLength()) {
            return Collections.singletonList(msg);
        }
        // 分割
        long timeNow = System.currentTimeMillis();
        int bodyRealLength = msgContent.getBody().length;
        int bodyLimitLength = msg.getMsgContent().getLimitMsgLength() - 6;
        short pkTotal = (short) Math.ceil((double) bodyRealLength / bodyLimitLength);
        List<DeliverRequest> out = new ArrayList<>(pkTotal);
        for (short i = 1; i <= pkTotal; i++) {
            int from = (i - 1) * bodyLimitLength;
            byte[] body = Arrays.copyOfRange(msgContent.getBody(), from, from + bodyLimitLength);
            short msgId = (short) (timeNow % 255);
            UserDataHeader udh = UserDataHeader.six(msgId, pkTotal, i);
            MsgContent subContent = new MsgContent(udh, msgContent.getFormat(), body);
            DeliverRequest subRequest = msg.copy();
            subRequest.setTpUdhi((short) 1);
            subRequest.setMsgContent(subContent);
            out.add(subRequest);
        }
        return out;
    }

    public static MsgContent mergeSubmitContent(List<SubmitRequest> requests) throws IOException {
        MsgFormat msgFormat = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (SubmitRequest request : requests) {
            msgFormat = request.getMsgContent().getFormat();
            byte[] body = request.getMsgContent().getBody();
            out.write(body);
        }
        byte[] body = out.toByteArray();
        return new MsgContent(null, msgFormat, body);
    }

    public static MsgContent mergeDeliverContent(List<DeliverRequest> requests) throws IOException {
        MsgFormat msgFormat = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (DeliverRequest request : requests) {
            assert request.getMsgContent() != null;
            msgFormat = request.getMsgContent().getFormat();
            byte[] body = request.getMsgContent().getBody();
            out.write(body);
        }
        byte[] body = out.toByteArray();
        return new MsgContent(null, msgFormat, body);
    }
}
