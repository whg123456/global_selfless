package g.top.simchat.core;

/**
 * Created by wanghaiguang on 2022/9/25 下午2:11
 */
public class ChatCode {
    public static final int PING_CODE = 10015;
    public static final int PONG_CODE = 10016;
    public static final int AUTH_CODE = 10000;
    /**
     * 系统消息类型
     */
    public static final int SYS_USER_COUNT = 20001; // 在线用户数
    public static final int SYS_AUTH_STATE = 20002; // 认证结果
    public static final int SYS_OTHER_INFO = 20003; // 系统消息
    public static final int USER_LIST = 20004; // 用户列表
    public static final int SINGLE_CHAT = 20005; // 单聊
    public static final int GROUP_CHAT = 20006; // 群聊
    public static final int ONLINEUSER_LIST = 20007; // 在线用户
}
