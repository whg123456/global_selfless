package g.top.admin.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wanghaiguang on 2022/7/18 上午10:55
 */
@Controller
public class htmltestController {

    @RequestMapping("/index")
    public String teste() {

        return "index";
    }
}
