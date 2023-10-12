package io.github.ithmal.itio.codec.sgip.message;


import io.github.ithmal.itio.codec.sgip.base.Command;
import io.github.ithmal.itio.codec.sgip.base.MsgContent;
import io.github.ithmal.itio.codec.sgip.base.SgipMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:32
 */
@Getter
@Setter
public class DeliverRequest extends SgipMessage {

    /**
     * 发送短消息的用户手机号，手机号码前加“86”国别标志。
     */
    private String userNumber;

    /**
     * SP的接入号码
     */
    private String spNumber;

    /**
     * GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9
     */
    private short tpPid;

    /**
     * GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐
     */
    private short tpUdhi;

    /**
     * 短信内容
     */
    private MsgContent msgContent;

    /**
     * 保留，扩展用
     */
    private String reserve;

    public DeliverRequest(long sequenceId) {
        super(Command.DELIVER_REQUEST, sequenceId);
    }

    public DeliverRequest copy(){
        DeliverRequest newObj = new DeliverRequest(getSequenceId());
        newObj.userNumber = userNumber;
        newObj.spNumber = spNumber;
        newObj.tpPid = tpPid;
        newObj.tpUdhi = tpUdhi;
        newObj.msgContent = msgContent;
        newObj.reserve = reserve;
        return newObj;
    }

    @Override
    public String toString() {
        return "DeliverRequest{" +
                "userNumber='" + userNumber + '\'' +
                ", spNumber='" + spNumber + '\'' +
                ", tpPid=" + tpPid +
                ", tpUdhi=" + tpUdhi +
                ", msgContent=" + msgContent +
                ", reserve='" + reserve + '\'' +
                '}';
    }
}

