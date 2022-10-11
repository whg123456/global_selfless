package g.top.model.dto.chat;

import io.netty.channel.Channel;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wanghaiguang on 2022/9/25 下午2:06
 */
@Data
public class UserInfo {
//    {
//        id: 0,
//                url: "https://img1.baidu.com/it/u=592570905,1313515675&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500",
//            username: "花间一壶酒",
//            info: "在吗",
//            timer: "2022/8/9",
//    }
    private static AtomicInteger uidGener = new AtomicInteger(1000);
    private boolean isAuth = false; // 是否认证
    private long time = 0;  // 登录时间
    private int userId;     // UID
    private String nick;    // 昵称
    private String addr;    // 地址
    private Channel channel;// 通道




    //其他get和set方法直接生成就行
    public void setUserId() {
        this.userId = uidGener.incrementAndGet();
    }

    @Data
    public static class TemUser{
        private String url = "https://img1.baidu.com/it/u=592570905,1313515675&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500";
        private String username;
        private String info;
        private String timer;
        private Integer id;
    }

    public TemUser getTemUser() {
        TemUser temUser = new TemUser();
        temUser.setUsername(this.nick);
        temUser.setInfo("ceshi");
        temUser.setTimer("2022/8/9");
        return temUser;
    }
}