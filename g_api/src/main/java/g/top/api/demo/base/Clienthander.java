package g.top.api.demo.base;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by wanghaiguang on 2022/9/15 上午11:40
 */
public class Clienthander extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String msg = "hello";
        System.out.println("client send >>>");
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg , Charsets.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("服务端".concat(
                ctx.channel().remoteAddress().toString()
        ).concat(
                "回复消息："
        ).concat(byteBuf.toString(Charsets.UTF_8)));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
