package g.top.api;

import g.top.api.demo.simple.NettyChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by wanghaiguang on 2022/9/7 下午5:04
 */
@SpringBootApplication
public class StartApp {

    public static void main(String[] args) {

        SpringApplication.run(StartApp.class, args);
    }
}
