package g.top.ablity.api.luckywheel;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

/**
 * Created by wanghaiguang on 2022/9/21 下午5:46
 */
@RestController
public class LuckyWheelController {
    @GetMapping("/ability/lucky/info")
    public String getPrizeInfo(@RequestParam Integer prizeId) {
        return LocalTime.now() + "——测试：" + prizeId;
    }

}
