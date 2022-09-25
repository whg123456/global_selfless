package g.top.simchat.core;

import g.top.simchat.handler.MessageHandler;
import g.top.simchat.handler.UserAuthHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaiguang on 2022/9/25 下午2:12
 */
public class SimpleWebChatServer extends BaseServer {
    private ScheduledExecutorService executorService;
    public SimpleWebChatServer(int port) {
        this.port = port;
        // 创建一个定长线程池
        executorService = Executors.newScheduledThreadPool(2);
    }
    @Override
    public void start() {
        b.group(bossGroup, workGroup);// 设置初始化的主从"线程池"
        b.channel(NioServerSocketChannel.class);//3.接收进来的连接，由于是服务端，故而是NioServerSocketChannel
        b.option(ChannelOption.SO_KEEPALIVE, true);//4提供给NioServerSocketChannel用来接收进来的连接
        b.option(ChannelOption.TCP_NODELAY, true);
        b.option(ChannelOption.SO_BACKLOG, 1024);//5设置Channel实现的配置参数
        b.localAddress(new InetSocketAddress(port));
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {//6帮助使用者配置一个新的 Channel
                ch.pipeline().addLast(defLoopGroup,
                        new HttpServerCodec(),   //请求解码器
                        new HttpObjectAggregator(65536),//将多个消息转换成单一的消息对象
                        new ChunkedWriteHandler(),  //支持异步发送大的码流，一般用于发送文件流
                        new IdleStateHandler(60, 0, 0), //检测链路是否读空闲
                        new UserAuthHandler(), //处理握手和认证
                        new MessageHandler()    //处理消息的发送
                );
            }
        });
        try {
            // 绑定端口，开始接收进来的连接
            cf = b.bind().sync();
            InetSocketAddress addr = (InetSocketAddress) cf.channel().localAddress();
            logger.info("WebSocketServer start success, port is:{}", addr.getPort());
            // 定时扫描所有的Channel，关闭失效的Channel
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    logger.info("scanNotActiveChannel --------");
                    UserInfoManager.scanNotActiveChannel();
                }
            }, 3, 60, TimeUnit.SECONDS);
            // 定时向所有客户端发送Ping消息
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    UserInfoManager.broadCastPing();
                }
            }, 3, 50, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            logger.error("WebSocketServer start fail,", e);
        }
    }
    @Override
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
        super.shutdown();
    }
}
