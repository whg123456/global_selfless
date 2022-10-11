package g.top.simchat.handler;

/**
 * Created by wanghaiguang on 2022/9/25 下午2:09
 */

import com.alibaba.fastjson.JSONObject;
import g.top.model.dto.chat.UserInfo;
import g.top.simchat.core.*;
import g.top.utils.JacksonHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1.SimpleChannelInboundHandler实现了ChannelInboundHandler接口,
 *      提供了许多事件处理的接口方法，然后你可以覆盖这些方法
 */
public class UserAuthHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(UserAuthHandler.class);
    private WebSocketServerHandshaker handshaker;

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
//        System.out.println(JacksonHelper.serialize(msg));
        return super.acceptInboundMessage(msg);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        UserInfoManager.removeChannel(ctx.channel());
        UserInfoManager.broadCastInfo(ChatCode.USER_LIST, UserInfoManager.getTemUsers());
        super.channelUnregistered(ctx);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocket(ctx, (WebSocketFrame) msg);
        }
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent evnet = (IdleStateEvent) evt;
            // 判断Channel是否读空闲, 读空闲时移除Channel
            if (evnet.state().equals(IdleState.READER_IDLE)) {
                final String remoteAddress = NettyUtil.parseChannelRemoteAddr(ctx.channel());
                logger.warn("NETTY SERVER PIPELINE: IDLE exception [{}]", remoteAddress);
                UserInfoManager.removeChannel(ctx.channel());
                UserInfoManager.broadCastInfo(ChatCode.SYS_USER_COUNT,UserInfoManager.getAuthUserCount());
            }
        }
        ctx.fireUserEventTriggered(evt);
    }
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess() || !"websocket".equals(request.headers().get("Upgrade"))) {
            logger.warn("protobuf don't support websocket");
            ctx.channel().close();
            return;
        }
        WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(
                Constants.WEBSOCKET_URL, null, true);
        handshaker = handshakerFactory.newHandshaker(request);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            // 动态加入websocket的编解码处理 握手响应
            handshaker.handshake(ctx.channel(), request);
//            UserInfo userInfo = new UserInfo();
//            userInfo.setAddr(NettyUtil.parseChannelRemoteAddr(ctx.channel()));
            // 存储已经连接的Channel
            UserInfoManager.addChannel(ctx.channel());
        }
    }
    private void handleWebSocket(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路命令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            UserInfoManager.removeChannel(ctx.channel());
            return;
        }
        // 判断是否Ping消息
        if (frame instanceof PingWebSocketFrame) {
            logger.info("ping message:{}", frame.content().retain());
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 判断是否Pong消息
        if (frame instanceof PongWebSocketFrame) {
            logger.info("pong message:{}", frame.content().retain());
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 本程序目前只支持文本消息
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(frame.getClass().getName() + " frame type not supported");
        }
        String message = ((TextWebSocketFrame) frame).text();
        TemMessage temMessage = JSONObject.parseObject(message, TemMessage.class);
        int code = temMessage.getCode();
        Channel channel = ctx.channel();
        switch (code) {
            case ChatCode.PING_CODE:
            case ChatCode.PONG_CODE:
                UserInfoManager.updateUserTime(channel);
                logger.info("receive pong message, address: {}", NettyUtil.parseChannelRemoteAddr(channel));
                return;
            case ChatCode.AUTH_CODE:
                boolean isSuccess = UserInfoManager.saveUser(channel, temMessage.getMess().getNick(), temMessage.getMess().getUid());
                if (isSuccess) {
                    UserInfoManager.broadCastInfo(ChatCode.SYS_USER_COUNT,UserInfoManager.getAuthUserCount());

                    //在线用户列表
                    UserInfoManager.broadCastInfo(ChatCode.USER_LIST, UserInfoManager.getTemUsers());
                }

                return;
            case ChatCode.SYS_AUTH_STATE:
            case ChatCode.SYS_OTHER_INFO:
            case ChatCode.SYS_USER_COUNT:
            case ChatCode.SINGLE_CHAT:
            case ChatCode.GROUP_CHAT:
            case ChatCode.USER_LIST:
            case ChatCode.ONLINEUSER_LIST:
                break;
            default:
                logger.warn("The code [{}] can't be auth!!!", code);
                return;
        }
        //后续消息交给MessageHandler处理
        ctx.fireChannelRead(frame.retain());
    }



}
