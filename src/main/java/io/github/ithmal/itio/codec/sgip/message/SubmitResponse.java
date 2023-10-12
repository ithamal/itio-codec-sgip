package io.github.ithmal.itio.codec.sgip.message;


import io.github.ithmal.itio.codec.sgip.base.Command;
import io.github.ithmal.itio.codec.sgip.base.SgipMessage;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: ken.lin
 * @since: 2023-10-06 16:32
 */
@Getter
@Setter
public class SubmitResponse extends SgipMessage {

    /**
     * Submit命令是否成功接收。
     * 0：接收成功
     * 其它：错误码
     */
    private short result;

    /**
     * 保留，扩展用
     */
    private String reserve;

    public SubmitResponse(long sequenceId) {
        super(Command.SUBMIT_RESPONSE, sequenceId);
    }

    @Override
    public String toString() {
        return "SubmitResponse{" +
                "result=" + result +
                ", reserve='" + reserve + '\'' +
                ", sequenceId=" + sequenceId +
                '}';
    }
}

