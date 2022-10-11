package g.top.admin;

import g.top.data.mapper.PermissionMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by wanghaiguang on 2022/9/16 下午5:21
 */
@EnableDiscoveryClient
@ComponentScan(basePackages = {"g.top.data.mapper"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = {"g.top.api.feign"}) // 启用 OpenFeign
public class StartApp {
    @Resource
    private PermissionMapper permissionMapper;
    @PostConstruct
    public void test(){
        System.out.println(permissionMapper.selectCount());
    }
    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }
}
