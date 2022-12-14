package g.top.api.demo.base;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by wanghaiguang on 2022/9/15 上午11:21
 * 自定义handler 必须继承netty规范
 */
public class ServerHander extends ChannelInboundHandlerAdapter {

    @Override
    //读取事件回调
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println("客户端".concat(
                ctx.channel().remoteAddress().toString()
                ).concat(
                "发送消息："
                ).concat(byteBuf.toString(Charsets.UTF_8)));
    }

    @Override
    //数据读取完毕回调
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("消息收到", Charsets.UTF_8));
    }

    @Override
    //异常回调
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
