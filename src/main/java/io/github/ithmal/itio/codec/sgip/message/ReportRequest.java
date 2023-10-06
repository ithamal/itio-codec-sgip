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
public class ReportRequest extends SgipMessage {

    /**
     * 该命令所涉及的Submit或deliver命令的序列号
     */
    private long submitSequenceNumber;

    /**
     * Report命令类型
     * 0：对先前一条Submit命令的状态报告
     * 1：对先前一条前转Deliver命令的状态报告
     */
    private short reportType;

    /**
     * 接收短消息的手机号，手机号码前加“86”国别标志
     */
    private String userNumber;

    /**
     * 该命令所涉及的短消息的当前执行状态
     * 0：发送成功
     * 1：等待发送
     * 2：发送失败
     */
    private short state;

    /**
     * 当State=2时为错误码值，否则为0
     */
    private short errorCode;

    /**
     * 保留，扩展用
     */
    private String reserve;

    public ReportRequest(long sequenceId) {
        super(Command.REPORT_REQUEST, sequenceId);
    }

    @Override
    public int getLength() {
        return 44;
    }

    @Override
    public String toString() {
        return "ReportRequest{" +
                "submitSequenceNumber=" + submitSequenceNumber +
                ", reportType=" + reportType +
                ", userNumber='" + userNumber + '\'' +
                ", state=" + state +
                ", errorCode=" + errorCode +
                ", reserve='" + reserve + '\'' +
                '}';
    }
}

