package io.github.ithmal.itio.codec.sgip.message;


import io.github.ithmal.itio.codec.sgip.base.Command;
import io.github.ithmal.itio.codec.sgip.base.MsgContent;
import io.github.ithmal.itio.codec.sgip.base.SgipMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:32
 */
@Getter
@Setter
public class SubmitRequest extends SgipMessage {

    /**
     * SP的接入号码
     */
    private String spNumber;

    /**
     * 付费号码，手机号码前加“86”国别标志；当且仅当群发且对用户收费时为空；如果为空，则该条短消息产生的费用由UserNumber代表的用户支付；
     * 如果为全零字符串“000000000000000000000”，表示该条短消息产生的费用由SP支付。
     */
    private String chargeNumber;

    /**
     * 接收该短消息的手机号，该字段重复UserCount指定的次数，手机号码前加“86”国别标志
     */
    private String[] userNumbers;

    /**
     * 企业代码，取值范围0-99999
     */
    private String corpId;

    /**
     * 业务代码，由SP定义
     */
    private String serviceType;

    /**
     * 计费类型
     */
    private short feeType;

    /**
     * 取值范围0-99999，该条短消息的收费值，单位为分，由SP定义
     * 对于包月制收费的用户，该值为月租费的值
     */
    private String feeValue;

    /**
     * 取值范围0-99999，赠送用户的话费，单位为分，由SP定义，特指由SP向用户发送广告时的赠送话费
     */
    private String givenValue;

    /**
     * 代收费标志，0：应收；1：实收
     */
    private short agentFlag;

    /**
     * 引起MT消息的原因
     * 0-MO点播引起的第一条MT消息；
     * 1-MO点播引起的非第一条MT消息；
     * 2-非MO点播引起的MT消息；
     * 3-系统反馈引起的MT消息。
     */
    private short moreLateToMtFlag;

    /**
     * 优先级0-9从低到高，默认为0
     */
    private short priority;

    /**
     * 短消息寿命的终止时间，如果为空，表示使用短消息中心的缺省值。时间内容为16个字符，格式为”yymmddhhmmsstnnp” ，其中“tnnp”取固定值“032+”，即默认系统为北京时间
     */
    private String expireTime;

    /**
     * 短消息定时发送的时间，如果为空，表示立刻发送该短消息。时间内容为16个字符，格式为“yymmddhhmmsstnnp” ，其中“tnnp”取固定值“032+”，即默认系统为北京时间
     */
    private String scheduleTime;

    /**
     * 状态报告标记
     * 0-该条消息只有最后出错时要返回状态报告
     * 1-该条消息无论最后是否成功都要返回状态报告
     * 2-该条消息不需要返回状态报告
     * 3-该条消息仅携带包月计费信息，不下发给用户，要返回状态报告
     * 其它-保留
     * 缺省设置为0
     */
    private short reportFlag;

    /**
     * GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9
     */
    private short tpPid;

    /**
     * GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐
     */
    private short tpUdhi;

    /**
     * 信息类型：
     * 0-短消息信息
     * 其它：待定
     */
    private short msgType;

    /**
     * 短信内容
     */
    private MsgContent msgContent;

    /**
     * 保留，扩展用
     */
    private String reserve;

    public SubmitRequest(long sequenceId) {
        super(Command.SUBMIT_REQUEST, sequenceId);
    }

    public SubmitRequest copy() {
        SubmitRequest newObj = new SubmitRequest(getSequenceId());
        newObj.spNumber = spNumber;
        newObj.chargeNumber = chargeNumber;
        newObj.userNumbers = userNumbers;
        newObj.corpId = corpId;
        newObj.serviceType = serviceType;
        newObj.feeType = feeType;
        newObj.feeValue = feeValue;
        newObj.givenValue = givenValue;
        newObj.agentFlag = agentFlag;
        newObj.moreLateToMtFlag = moreLateToMtFlag;
        newObj.priority = priority;
        newObj.expireTime = expireTime;
        newObj.scheduleTime = scheduleTime;
        newObj.reportFlag = reportFlag;
        newObj.tpPid = tpPid;
        newObj.tpUdhi = tpUdhi;
        newObj.msgType = msgType;
        newObj.msgContent = msgContent;
        newObj.reserve = reserve;
        return newObj;
    }

    @Override
    public String toString() {
        return "SubmitRequest{" +
                "spNumber='" + spNumber + '\'' +
                ", chargeNumber='" + chargeNumber + '\'' +
                ", userNumbers=" + Arrays.toString(userNumbers) +
                ", corpId='" + corpId + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", feeType=" + feeType +
                ", feeValue='" + feeValue + '\'' +
                ", givenValue='" + givenValue + '\'' +
                ", agentFlag=" + agentFlag +
                ", moreLateToMtFlag=" + moreLateToMtFlag +
                ", priority=" + priority +
                ", expireTime='" + expireTime + '\'' +
                ", scheduleTime='" + scheduleTime + '\'' +
                ", reportFlag=" + reportFlag +
                ", tpPid=" + tpPid +
                ", tpUdhi=" + tpUdhi +
                ", msgType=" + msgType +
                ", msgContent=" + msgContent +
                ", reserve='" + reserve + '\'' +
                ", sequenceId=" + sequenceId +
                '}';
    }
}

