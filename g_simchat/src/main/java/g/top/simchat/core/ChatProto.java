package g.top.simchat.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import g.top.model.base.CommonDTO;
import g.top.utils.JacksonHelper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghaiguang on 2022/9/25 下午2:11
 */
@Data
public class ChatProto {
    public static final int PING_PROTO = 1 << 8 | 220; //ping消息
    public static final int PONG_PROTO = 2 << 8 | 220; //pong消息
    public static final int SYST_PROTO = 3 << 8 | 220; //系统消息
    public static final int EROR_PROTO = 4 << 8 | 220; //错误消息
    public static final int AUTH_PROTO = 5 << 8 | 220; //认证消息
    public static final int MESS_PROTO = 6 << 8 | 220; //普通消息

    private int version = 1;
    private int uri;
    private CommonDTO body;

    public ChatProto(int head, CommonDTO body) {
        this.uri = head;
        this.body = body;
    }
    public static String buildPingProto() {
        return buildProto(PING_PROTO, null);
    }
    public static String buildPongProto() {
        return buildProto(PONG_PROTO, null);
    }

    public static String buildProto(int uri, CommonDTO body) {
        ChatProto chatProto = new ChatProto(uri, body);
        return JSON.toJSONString(chatProto);
    }
}
