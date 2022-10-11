package g.top.api.demo.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by wanghaiguang on 2022/9/26 下午7:49
 */
public class WebSocketInitialzer extends ChannelInitializer<Channel> {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //增加编解码器 的另一种方式
        pipeline.addLast(new HttpServerCodec());
        //    块方式写的处理器 适合处理大数据
        pipeline.addLast(new ChunkedWriteHandler());
        //聚合
        pipeline.addLast(new HttpObjectAggregator(512 * 1024));
        /*
         * 这个时候 我们需要声明我们使用的是 websocket 协议
         * netty为websocket也准备了对应处理器  设置的是访问路径
         * 这个时候我们只需要访问 ws://127.0.0.1:7777/hello 就可以了
         * 这个handler是将http协议升级为websocket  并且使用 101 作为响应码
         * */
        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
        pipeline.addLast(new WebSocketHandler());
    }
}
