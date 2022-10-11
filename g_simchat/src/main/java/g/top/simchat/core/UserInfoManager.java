package g.top.simchat.core;

/**
 * Created by wanghaiguang on 2022/9/25 下午2:10
 */

import g.top.model.base.CommonDTO;
import g.top.model.dto.chat.UserInfo;
import g.top.utils.JacksonHelper;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Channel的管理器
 */
public class UserInfoManager {
    private static final Logger logger = LoggerFactory.getLogger(UserInfoManager.class);
    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private static ConcurrentMap<Channel, UserInfo> userInfos = new ConcurrentHashMap<>();
    private static AtomicInteger userCount = new AtomicInteger(0);




    public static void addChannel(Channel channel) {
        String remoteAddr = NettyUtil.parseChannelRemoteAddr(channel);
        if (!channel.isActive()) {
            logger.error("channel is not active, address: {}", remoteAddr);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAddr(remoteAddr);
        userInfo.setChannel(channel);
        userInfo.setTime(System.currentTimeMillis());
        userInfos.put(channel, userInfo);
    }



    public static boolean saveUser(Channel channel, String nick, Integer uid) {
        UserInfo userInfo = userInfos.get(channel);
        if (userInfo == null) {
            return false;
        }
        if (!channel.isActive()) {
            logger.error("channel is not active, address: {}, nick: {}", userInfo.getAddr(), nick);
            return false;
        }
        boolean flag = checkParamUid(uid);
        if (!flag) {
            sendInfo(channel,ChatCode.SYS_AUTH_STATE,false);
            channel.close();
            return false;
        }

        // 增加一个认证用户
        userCount.incrementAndGet();
        userInfo.setNick(nick);
        userInfo.setAuth(true);
        userInfo.setUserId(uid);
        userInfo.setTime(System.currentTimeMillis());
        return true;
    }
    /**
     * 从缓存中移除Channel，并且关闭Channel
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        try {
            logger.warn("channel will be remove, address is :{}", NettyUtil.parseChannelRemoteAddr(channel));
            rwLock.writeLock().lock();
            channel.close();
            UserInfo userInfo = userInfos.get(channel);
            if (userInfo != null) {
                UserInfo tmp = userInfos.remove(channel);
                if (tmp != null && tmp.isAuth()) {
                    // 减去一个认证用户
                    userCount.decrementAndGet();
                }
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 点对点
     */
    public static void p2pMess(TemMessage.message mess, String time) {
        //tem版 未持久化

        ConcurrentMap<Channel, UserInfo> userInfos = getUserInfos();
        UserInfo sourceUserInfo = userInfos.values().stream().filter(userInfo -> userInfo.getUserId() == mess.getUid()).findFirst().orElse(null);
        String nick = sourceUserInfo == null ? "" : sourceUserInfo.getNick();
        userInfos.forEach((k,v) -> {
            if (v.getUserId() == mess.getTargetUid()) {
                ChatMessageResp chatMessageResp = ChatMessageResp.builder()
                        .msg(mess.getMsg())
                        .sendTime(time)
                        .sourceImg("")
                        .sourceNick(nick)
                        .sourceUid(mess.getUid())
                        .targetUid(mess.getTargetUid())
                        .build();
                String proto = ChatProto.buildProto(ChatCode.SINGLE_CHAT, CommonDTO.success(chatMessageResp));
                k.writeAndFlush(new TextWebSocketFrame(proto));
            }
        });
    }
    /**
     * 广播系统消息
     */
    public static void broadCastInfo(int code, Object mess) {
        try {
            rwLock.readLock().lock();
            Set<Channel> keySet = userInfos.keySet();
            for (Channel ch : keySet) {
                UserInfo userInfo = userInfos.get(ch);
                if (userInfo == null || !userInfo.isAuth()) continue;
                ch.writeAndFlush(new TextWebSocketFrame(ChatProto.buildProto(code, CommonDTO.success(mess))));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }
    public static void broadCastPing() {
        try {
            rwLock.readLock().lock();
            logger.info("broadCastPing userCount: {}", userCount.intValue());
            Set<Channel> keySet = userInfos.keySet();
            for (Channel ch : keySet) {
                UserInfo userInfo = userInfos.get(ch);
                if (userInfo == null || !userInfo.isAuth()) continue;
                ch.writeAndFlush(new TextWebSocketFrame(ChatProto.buildPingProto()));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }
    /**
     * 发送系统消息
     * @param code
     * @param mess
     */
    public static void sendInfo(Channel channel, int code, Object mess) {
        channel.writeAndFlush(new TextWebSocketFrame(ChatProto.buildProto(code, CommonDTO.success(mess))));
    }

    //广播消息自己除外
    public static void boardCastSelfLessInfo(Channel channel, int code, Object mess) {
        try {
            rwLock.readLock().lock();
            Set<Channel> keySet = userInfos.keySet();
            for (Channel ch : keySet) {
                if (channel == ch) continue;
                UserInfo userInfo = userInfos.get(ch);
                if (userInfo == null || !userInfo.isAuth()) continue;
                ch.writeAndFlush(new TextWebSocketFrame(ChatProto.buildProto(code, CommonDTO.success(mess))));
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public static void sendPong(Channel channel) {
        channel.writeAndFlush(new TextWebSocketFrame(ChatProto.buildPongProto()));
    }
    /**
     * 扫描并关闭失效的Channel
     */
    public static void scanNotActiveChannel() {
        Set<Channel> keySet = userInfos.keySet();
        for (Channel ch : keySet) {
            UserInfo userInfo = userInfos.get(ch);
            if (userInfo == null) continue;
            if (!ch.isOpen() || !ch.isActive() || (!userInfo.isAuth() &&
                    (System.currentTimeMillis() - userInfo.getTime()) > 10000)) {
                removeChannel(ch);
            }
        }
    }

    public static List<UserInfo.TemUser> getTemUsers() {
        List<UserInfo.TemUser> result = new ArrayList<>();
        if (userInfos.size() == 0) {
            return null;
        }

        List<UserInfo> userInfos = UserInfoManager.userInfos.values().stream().collect(Collectors.toList());

        for (int i = 0; i < userInfos.size(); i++) {
            UserInfo userInfo = userInfos.get(i);
            UserInfo.TemUser temUser = userInfo.getTemUser();
            temUser.setId(userInfo.getUserId());
            result.add(temUser);
        }
        return result;
    }
    public static UserInfo getUserInfo(Channel channel) {
        return userInfos.get(channel);
    }
    public static ConcurrentMap<Channel, UserInfo> getUserInfos() {
        return userInfos;
    }
    public static int getAuthUserCount() {
        return userCount.get();
    }
    public static void updateUserTime(Channel channel) {
        UserInfo userInfo = getUserInfo(channel);
        if (userInfo != null) {
            userInfo.setTime(System.currentTimeMillis());
        }
    }


    private static boolean checkParamUid(Integer... uids) {

        for (Integer uid : uids) {
            if (uid > 100) {
                return false;
            }
        }
        return true;
    }

}
