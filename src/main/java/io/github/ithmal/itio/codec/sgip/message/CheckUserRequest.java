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
public class CheckUserRequest extends SgipMessage {

    /**
     * 计费中心给SMG分配的用户名
     */
    private String userName;

    /**
     * 和用户名对应的密码
     */
    private String password;

    /**
     * 待鉴权的手机号码，手机号码前加“86”国别标志
     */
    private String userNumber;

    /**
     * 保留，扩展用
     */
    private String reserve;


    public CheckUserRequest(long sequenceId) {
        super(Command.CHECK_USER_REQUEST, sequenceId);
    }

    @Override
    public String toString() {
        return "CheckUserRequest{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", reserve='" + reserve + '\'' +
                '}';
    }
}

