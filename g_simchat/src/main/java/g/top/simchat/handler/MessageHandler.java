package g.top.simchat.handler;
import com.alibaba.fastjson.JSONObject;
import g.top.model.dto.chat.UserInfo;
import g.top.simchat.core.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * Created by wanghaiguang on 2022/9/25 下午2:07
 */
/**
 * 1.SimpleChannelInboundHandler实现了ChannelInboundHandler接口,
 *       提供了许多事件处理的接口方法，然后你可以覆盖这些方法
 */
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame)
            throws Exception {
        UserInfo userInfo = UserInfoManager.getUserInfo(ctx.channel());
        if (userInfo != null && userInfo.isAuth()) {
            TemMessage temMessage = JSONObject.parseObject(frame.text(), TemMessage.class);
            logger.info("MessageHandler ---> " + temMessage.toString());

            Integer code = temMessage.getCode();
            TemMessage.message mess = temMessage.getMess();
            Integer uid = mess.getUid();
            Integer targetUid = mess.getTargetUid();

            switch (code) {
                case ChatCode.SINGLE_CHAT:
                    //todo checkUser
                    String currentDateTime = DateTimeUtil.getCurrentDateTime();
                    UserInfoManager.p2pMess(mess, currentDateTime);
                case ChatCode.GROUP_CHAT:
                    break;
                default:
                    logger.warn("The code [{}] can't be auth!!!", code);
                    return;
            }

            // 广播返回用户发送的消息文本

//            UserInfoManager.broadcastMess(userInfo.getUserId(), userInfo.getNick(), mess.getMsg());
        }
    }



}

