package g.top.api.test;

import g.top.data.cache.Cache;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by wanghaiguang on 2022/9/7 下午5:54
 */
@Service
public class tta {

    @Cache
    public String test() {
        return "hello";
    }
}
