package g.top.ablity.api.user;

import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

/**
 * Created by wanghaiguang on 2022/9/20 上午10:36
 */
@RestController("")
public class ChatUserServiceAbility{

    @GetMapping("/ability/user/info")
    public String getUserInfo(@RequestParam Integer userId) {

        return LocalTime.now() + "——测试：" + userId;
    }
}
