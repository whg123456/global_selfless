package g.top.api.conf;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wanghaiguang on 2022/9/21 下午4:38
 */
@Configuration
public class FeignLogConfig {
    @Bean
    public Logger.Level level(){
        return Logger.Level.BASIC;
    }
}
