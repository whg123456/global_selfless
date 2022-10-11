package g.top.simchat.clientdemo;


import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.URI;
import java.util.Scanner;


/**
 * Created by wanghaiguang on 2022/9/27 下午3:47
 */
public class WebSocketNettyClient {
    public static void main(String[] args)  {

        EventLoopGroup group = new NioEventLoopGroup();
        final ClientHandler handler =new ClientHandler();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加一个http的编解码器
                            pipeline.addLast(new HttpClientCodec());
                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));

                            pipeline.addLast(handler );

                        }
                    });

            URI websocketURI = new URI("ws://localhost:9688/websocket");
            HttpHeaders httpHeaders = new DefaultHttpHeaders();
            //进行握手
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, (String)null, true,httpHeaders);
            final Channel channel=bootstrap.connect(websocketURI.getHost(),websocketURI.getPort()).sync().channel();
            handler.setHandshaker(handshaker);
            handshaker.handshake(channel);
            //阻塞等待是否握手成功
            handler.handshakeFuture().sync();
            System.out.println("握手成功");

            //发送消息
//            JSONObject clientJson = new JSONObject();
//            clientJson.put("cmd","test");
//            channel.writeAndFlush(new TextWebSocketFrame(clientJson.toString()));
            Scanner sc = new Scanner(System.in);
            while (sc.hasNextLine()) {
                String msg = sc.nextLine();
                channel.writeAndFlush(new TextWebSocketFrame(msg));
            }

            // 等待连接被关闭
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            group.shutdownGracefully();
        }

    }
}
