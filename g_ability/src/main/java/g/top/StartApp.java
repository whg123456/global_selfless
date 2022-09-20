package g.top;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by wanghaiguang on 2022/9/20 上午11:00
 */
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = {"g.top.api.feign"})
@SpringBootApplication
public class StartApp {
    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }

}
