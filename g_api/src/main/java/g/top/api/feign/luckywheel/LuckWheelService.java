package g.top.api.feign.luckywheel;

import g.top.api.conf.FeignLogConfig;
import g.top.model.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wanghaiguang on 2022/9/21 下午5:48
 */
@FeignClient(contextId = "luckWheelService",
        value = ServiceNameConstants.ABILITY_SERVICE,
        configuration = FeignLogConfig.class
)
@Component
public interface LuckWheelService {

    @GetMapping("/ability/lucky/info")
    String getPrizeInfo(@RequestParam Integer prizeId);
}
