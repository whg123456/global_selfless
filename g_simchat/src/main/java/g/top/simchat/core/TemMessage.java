package g.top.simchat.core;

import lombok.Data;

/**
 * Created by wanghaiguang on 2022/9/27 下午4:46
 * 请求消息体
 */
@Data
public class TemMessage {

    private Integer code;

    private message mess;

    @Data
    public static class message{
        private String nick;
        private Boolean isGroup;
        private Integer uid;
        private Integer targetUid;
        private String msg;
    }
}
