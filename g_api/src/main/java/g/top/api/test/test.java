package g.top.api.test;

import g.top.data.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Created by wanghaiguang on 2022/9/7 下午5:02
 */
@RestController
public class test {
    @Autowired
    private tta ta;

    @RequestMapping("/test815")
    public Object test() throws Exception {
        return ta.test();
    }

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(9000);

        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;
        while (true) {
            int readL = 0;
            while (readL < messageLength) {
                long read = socketChannel.read(byteBuffers);
                readL += read;

                System.out.println("read: --- " + read);

            }

            Arrays.asList(byteBuffers).
                    stream().
                    map(buffer -> "position:" + buffer.position() + "limit: " + buffer.limit() + "cap: " + buffer.capacity())
                    .forEach(System.out::println);

            for (ByteBuffer byteBuffer : Arrays.asList(byteBuffers)) {
                byteBuffer.flip();
            }

            int w = 0;
            while (w < messageLength) {
                long write = socketChannel.write(byteBuffers);
                w += write;

            }

            byteBuffers[0].clear();
            byteBuffers[1].clear();
            System.out.println("r : " + readL + "w:" + w);


        }


    }
}
