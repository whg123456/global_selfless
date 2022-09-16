package g.top.api.demo.simple;

import com.google.common.base.Charsets;
import g.top.api.demo.base.Clienthander;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * Created by wanghaiguang on 2022/9/15 下午7:07
 */
public class NettyChatClient {
    private String ip;
    private int port;

    public NettyChatClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void start() {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encoder", new StringEncoder(Charsets.UTF_8))
                                    .addLast("decoder", new StringDecoder(Charsets.UTF_8))
                                    .addLast(new ClientMessagehandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(this.ip, this.port).sync();

            channelFuture.addListener(future -> {
                if (future.isDone()) {
                    System.out.println("client start success...");
                }
            });

            Channel channel = channelFuture.channel();

            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String msg = sc.nextLine();
                channel.writeAndFlush(msg);
            }

            channelFuture.channel().closeFuture().sync();

            channelFuture.addListener(future -> {
                if (future.isCancellable()) {
                    System.out.println("client stop success...");

                }
            });
            sc.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        NettyChatClient nettyChatClient = new NettyChatClient("81.70.142.244", 18000);
        nettyChatClient.start();
    }
}
