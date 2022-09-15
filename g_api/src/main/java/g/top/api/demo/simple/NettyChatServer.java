package g.top.api.demo.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by wanghaiguang on 2022/9/15 下午4:50
 */
public class NettyChatServer {

    private int port;

    public NettyChatServer(int port) {
        this.port = port;
    }

    private void start() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(8);

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast("encoder", new StringEncoder());
//                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast(new ServerMessagehandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            channelFuture.addListener(future -> {
                if (future.isDone()) {
                    System.out.println("server start success...");
                }
            });

            channelFuture.channel().closeFuture().sync();
            channelFuture.addListener(future -> {
                if (future.isCancellable()) {
                    System.out.println("server stop success...");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }


    public static void main(String[] args) {
        NettyChatServer nettyChatServer = new NettyChatServer(9000);
        nettyChatServer.start();
    }
}
