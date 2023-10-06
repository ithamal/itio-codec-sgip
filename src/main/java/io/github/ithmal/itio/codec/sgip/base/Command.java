package io.github.ithmal.itio.codec.sgip.base;

/**
 * @author: ken.lin
 * @since: 2023-10-01 08:45
 */
public enum Command {


    /**
     * 连接请求
     */
    BIND_REQUEST(0x00000001),
    /**
     * 连接响应
     */
    BIND_RESPONSE(0x80000001),


    /**
     * 断开请求
     */
    UNBIND_REQUEST(0x00000002),
    /**
     * 断开响应
     */
    UNBIND_RESPONSE(0x80000002),
    /**
     * 提交短信请求
     */
    SUBMIT_REQUEST(0x00000003),
    /**
     * 提交短信响应
     */
    SUBMIT_RESPONSE(0x80000003),
    /**
     * 上行短信请求
     */
    DELIVER_REQUEST(0x00000004),
    /**
     * 上行短信响应
     */
    DELIVER_RESPONSE(0x80000004),
    /**
     * 报告请求
     */
    REPORT_REQUEST(0x00000005),
    /**
     * 报告响应
     */
    REPORT_RESPONSE(0x80000005),
    /**
     * 鉴权请求
     */
    CHECK_USER_REQUEST(0x00000010),
    /**
     * 鉴权响应
     */
    CHECK_USER_RESPONSE(0x80000010),
    ;

    private final int id;

    Command(int id) {
        this.id = id;
    }

    public static Command of(int commandId) {
        for (Command item : Command.values()) {
            if(item.id == commandId){
                return item;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }
}
