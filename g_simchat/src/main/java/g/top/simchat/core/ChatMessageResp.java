package g.top.simchat.core;

import g.top.model.base.ReturnValue;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Created by wanghaiguang on 2022/9/27 下午7:44
 * 聊天记录返回消息体
 */
@Data
@Builder
public class ChatMessageResp {
    private Integer sourceUid;
    private String sourceNick;
    private String sourceImg;
    private Integer targetUid;
    private String msg;
    private String sendTime;
}
