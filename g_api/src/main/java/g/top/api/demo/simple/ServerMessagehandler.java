package g.top.api.demo.simple;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.SocketAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanghaiguang on 2022/9/15 下午5:37
 */
public class ServerMessagehandler extends ChannelInboundHandlerAdapter {
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

//    私聊
//    private static Map<String, Channel> users = new HashMap<>();



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();
        channels.add(channel);

        SocketAddress address = channel.remoteAddress();
        String msg = getNowTime() + "\n[用户]" + address + "上线";
        String onLinePeoples = "\n当前在线人数 : " + channels.size();



        System.out.println(msg);
        channels.writeAndFlush(Unpooled.copiedBuffer(msg, Charsets.UTF_8));
        channels.writeAndFlush(Unpooled.copiedBuffer(onLinePeoples, Charsets.UTF_8));
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();


        for (Channel ch : channels) {
            String content;
            if (ch.remoteAddress().equals(address)) {
                content = getNowTime() + "\n [自己]" + address + " : ";
            } else {
                content = getNowTime() + "\n [用户]" + address + " : ";
            }
            ByteBuf byteBuf = (ByteBuf) msg;
            String sendMsg = content + byteBuf.toString(Charsets.UTF_8);
            System.out.println(sendMsg);
            ch.writeAndFlush(Unpooled.copiedBuffer(sendMsg, Charsets.UTF_8)).isSuccess();

        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        String msg = getNowTime() + "\n [用户]" + address + "下线";
        String onLinePeoples = "当前在线人数 : " + channels.size();


        System.out.println(msg);
        channels.writeAndFlush(Unpooled.copiedBuffer(msg, Charsets.UTF_8));
        channels.writeAndFlush(Unpooled.copiedBuffer(onLinePeoples, Charsets.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private String getNowTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
