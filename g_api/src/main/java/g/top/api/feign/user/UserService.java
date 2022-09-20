package g.top.api.feign.user;

import g.top.model.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wanghaiguang on 2022/9/19 下午2:45
 */
@FeignClient(contextId = "userService", value = ServiceNameConstants.ABILITY_SERVICE)
public interface UserService {

    @GetMapping("/ability/user/info")
    String getUserInfo(@RequestParam Integer userId) ;

}
