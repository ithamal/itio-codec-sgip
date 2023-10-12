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
public class CheckUserResponse extends SgipMessage {

    /**
     * 鉴权结果
     * 0：鉴权成功
     * 其它：错误码
     */
    private short result;

    /**
     * 用户状态
     * 0：注销；1：欠费停机；2：正常
     */
    private short status;

    /**
     * 保留，扩展用
     */
    private String reserve;


    public CheckUserResponse(long sequenceId) {
        super(Command.CHECK_USER_RESPONSE, sequenceId);
    }

    @Override
    public String toString() {
        return "CheckUserResponse{" +
                "result=" + result +
                ", status=" + status +
                ", reserve='" + reserve + '\'' +
                '}';
    }
}
