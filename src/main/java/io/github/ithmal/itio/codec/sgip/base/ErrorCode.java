package io.github.ithmal.itio.codec.sgip.base;

import lombok.RequiredArgsConstructor;

/**
 * @author: ken.lin
 * @since: 2023-10-06 19:19
 */
@RequiredArgsConstructor
public class ErrorCode {

    private final int code;

    private final String message;

    public static final ErrorCode OK = new ErrorCode(0, "无错误，命令正确接收");
    public static final ErrorCode LOGIN_INCORRECT = new ErrorCode(1, "非法登录，如登录名、口令出错、登录名与口令不符等");
    public static final ErrorCode LOGIN_REPEAT = new ErrorCode(2, "重复登录，如在同一TCP/IP连接中连续两次以上请求登录");
    public static final ErrorCode LOGIN_MANY = new ErrorCode(3, "连接过多，指单个节点要求同时建立的连接数过多");
    public static final ErrorCode LOGIN_TYPE_ERR = new ErrorCode(4, "登录类型错，指bind命令中的logintype字段出错");
    public static final ErrorCode ARG_FORMAT_ERR = new ErrorCode(5, "参数格式错，指命令中参数值与参数类型不符或与协议规定的范围不符");
    public static final ErrorCode ILLEGAL_USER_NUM = new ErrorCode(6, "非法手机号码，协议中所有手机号码字段出现非86130号码或手机号码前未加“86”时都应报错");
    public static final ErrorCode MSG_ID_ERR = new ErrorCode(7, "消息ID错");
    public static final ErrorCode MSG_LEN_ERR = new ErrorCode(8, "信息长度错");
    public static final ErrorCode ILLEGAL_SEQ_NUM = new ErrorCode(9, "非法序列号，包括序列号重复、序列号格式错误等");
    public static final ErrorCode ILLEGAL_OP_GNS = new ErrorCode(10, "非法操作GNS");
    public static final ErrorCode NODE_BUSY = new ErrorCode(11, "节点忙，指本节点存储队列满或其他原因，暂时不能提供服务的情况");
    public static final ErrorCode TERMINAL_FAIL = new ErrorCode(12, "终端的原因导致接收失败(关机、信号不好等)");
    public static final ErrorCode UNREACHABLE_DST = new ErrorCode(21, "目的地址不可达，指路由表存在路由且消息路由正确但被路由的节点暂时不能提供服务的情况");
    public static final ErrorCode ROUTER_ERR = new ErrorCode(22, "路由错，指路由表存在路由但消息路由出错的情况，如转错SMG等");
    public static final ErrorCode ROUTER_ABSENT = new ErrorCode(23, "路由不存在，指消息路由的节点在路由表中不存在");
    public static final ErrorCode CHARGE_NUM_ERR = new ErrorCode(24, "计费号码无效，鉴权不成功时反馈的错误信息");
    public static final ErrorCode USER_NON_COMM = new ErrorCode(25, "用户不能通信（如不在服务区、未开机等情况）");
    public static final ErrorCode PHONE_OUT_OF_MEMORY = new ErrorCode(26, "手机内存不足");
    public static final ErrorCode PHONE_NOT_SUPPORT_SMS = new ErrorCode(27, "手机不支持短消息");
    public static final ErrorCode PHONE_RECEIVE_ERR = new ErrorCode(28, "手机接收短消息出现错误");
    public static final ErrorCode UNKNOWN_USER = new ErrorCode(29, "不知道的用户");
    public static final ErrorCode FUNC_NOT_SUPPORT = new ErrorCode(30, "不提供此功能");
    public static final ErrorCode ILLEGAL_DEVICE = new ErrorCode(31, "非法设备");
    public static final ErrorCode SYS_FAIL = new ErrorCode(32, "系统失败");
    public static final ErrorCode CENTER_QUEUE_FULL = new ErrorCode(33, "短信中心队列满");

    public static final ErrorCode OTHER_GW_REJECT = new ErrorCode(52, "被异网网关拒绝（发送量过大）");

    public static final ErrorCode SEND_TOO_FAST = new ErrorCode(61, "发送过快");

    public static final ErrorCode BLACK_LIST = new ErrorCode(97, "黑名单");
}
