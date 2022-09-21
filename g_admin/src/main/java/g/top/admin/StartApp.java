package g.top.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by wanghaiguang on 2022/9/16 下午5:21
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"g.top.api.feign"}) // 启用 OpenFeign
public class StartApp {

    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }
}
