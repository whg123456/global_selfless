package g.top.api.thrift.thriftdemo.service.impl;

import g.top.api.thrift.thriftdemo.service.TestService;
import org.apache.thrift.TException;

/**
 * Created by wanghaiguang on 2022/7/15 下午5:06
 */
public class TestServiceImpl implements TestService.Iface {
    @Override
    public String sayHello(String message) throws TException {
        System.out.println("收到" + message);
        return "test ---------" + message;
    }
}
