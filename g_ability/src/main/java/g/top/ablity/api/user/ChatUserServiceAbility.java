package g.top.ablity.api.user;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

/**
 * Created by wanghaiguang on 2022/9/20 上午10:36
 */
@RestController("")
public class ChatUserServiceAbility{

    @GetMapping("/ability/user/info")
    @HystrixCommand(fallbackMethod = "getUserInfoFallBack",
            commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                    value = "1500")} )
    public String getUserInfo(@RequestParam Integer userId) {
        int a = 3/0;
        return LocalTime.now() + "——测试：" + userId;
    }

    public String getUserInfoFallBack(Integer userId) {
        return "error fallback";
    }
}
