package g.top.api.demo.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by wanghaiguang on 2022/9/26 下午7:50
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        //可以直接调用text 拿到文本信息帧中的信息
        System.out.println("msg:" + textWebSocketFrame.text());
        Channel channel = ctx.channel();
        //我们可以使用新建一个对象 将服务端需要返回的信息放入其中 返回即可
        TextWebSocketFrame resp = new TextWebSocketFrame("hello client from websocket server");
        channel.writeAndFlush(resp);
    }
}
