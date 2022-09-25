package g.top.simchat;

import g.top.simchat.core.Constants;
import g.top.simchat.core.SimpleWebChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wanghaiguang on 2022/9/21 下午8:04
 */
public class ChatStartApp {
    private static final Logger logger = LoggerFactory.getLogger(ChatStartApp.class);
    public static void main(String[] args) {
        final SimpleWebChatServer server = new SimpleWebChatServer(Constants.DEFAULT_PORT);
        server.init();
        server.start();
        //注册进程钩子，在JVM进程关闭前释放资源
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                server.shutdown();
                logger.warn(">>>>>>>>>> jvm shutdown");
                System.exit(0);
            }
        });
    }
}
