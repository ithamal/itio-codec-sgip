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
public class BindRequest extends SgipMessage {

    /**
     * 登录类型。
     * 1：SP向SMG建立的连接，用于发送命令
     * 2：SMG向SP建立的连接，用于发送命令
     * 3：SMG之间建立的连接，用于转发命令
     * 4：SMG向GNS建立的连接，用于路由表的检索和维护
     * 5：GNS向SMG建立的连接，用于路由表的更新
     * 6：主备GNS之间建立的连接，用于主备路由表的一致性
     * 11：SP与SMG以及SMG之间建立的测试连接，用于跟踪测试
     * 其它：保留
     */
    private short loginType;

    /**
     * 服务器端给客户端分配的登录名
     */
    private String userName;

    /**
     * 服务器端和Login Name对应的密码
     */
    private String password;

    /**
     * 保留，扩展用
     */
    private String reserve;

    public BindRequest(long sequenceId) {
        super(Command.BIND_REQUEST, sequenceId);
    }

    @Override
    public int getLength() {
        return 41;
    }

    @Override
    public String toString() {
        return "BindRequest{" +
                "loginType=" + loginType +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", reserve='" + reserve + '\'' +
                '}';
    }
}

