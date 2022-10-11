package g.top.admin.controller.luckywheel;

import g.top.api.feign.luckywheel.LuckWheelService;
import g.top.api.feign.user.UserService;
import g.top.data.mapper.PermissionMapper;
import g.top.model.dao.security.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by wanghaiguang on 2022/9/21 下午5:54
 */
@RestController
public class LuckyTestController {
    @Resource
    LuckWheelService luckWheelService;

    @Resource
    private PermissionMapper permissionMapper;

    @RequestMapping("/lucky/rpc")
    public String teste() {

        return luckWheelService.getPrizeInfo(9);
    }

//    @RequestMapping("/data")
//    public String testdae() {
//        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
//        System.out.println(permissionMapper.selectCount(queryWrapper));
//        return null;
//    }
}
