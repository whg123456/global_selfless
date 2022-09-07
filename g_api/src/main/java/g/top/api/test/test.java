package g.top.api.test;

import g.top.data.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wanghaiguang on 2022/9/7 下午5:02
 */
@RestController
public class test {
    @Autowired
    private tta ta;

    @RequestMapping("/test815")
    public Object test() throws Exception {
        return ta.test();
    }
}
