package g.top.admin.controller.user;

import g.top.api.feign.user.UserService;
import g.top.model.constant.ServiceNameConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by wanghaiguang on 2022/9/20 下午7:29
 */
@RestController
public class ChatUserController {
    @Resource
    UserService userService;

    @RequestMapping("/rpc")
    public String teste() {

        System.out.println(userService.getUserInfo(9));
        return userService.getUserInfo(9);
    }
}
