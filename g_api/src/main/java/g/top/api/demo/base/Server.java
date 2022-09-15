package g.top.api.demo.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * Created by wanghaiguang on 2022/9/15 上午10:54
 */
public class Server {
    public static void main(String[] args) throws InterruptedException {
        //处理连接线程组 可指定线程数量 默认核心数 * 2
        EventLoopGroup boss = new NioEventLoopGroup();
        //工作线程组 可指定线程数量 默认核心数 * 2
        EventLoopGroup worker = new NioEventLoopGroup();

        //启动配置类
        ServerBootstrap bootstrap = new ServerBootstrap();

        //设置连接组 和 工作组
        bootstrap.group(boss, worker)
                //设置通道实现类型
                .channel(NioServerSocketChannel.class)
                //线程队列连接个数
                .option(ChannelOption.SO_BACKLOG, 128)
                //保持活动连接状态
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //通道初始化
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //设置通道处理器
                        ch.pipeline().addLast(new ServerHander());
                    }
                });

        System.out.println("服务端 ready...");

        //绑定启动端口
        ChannelFuture channelFuture = bootstrap.bind(9000).sync();

        //对关闭通道监听
        channelFuture.channel().closeFuture().sync();
    }
}
