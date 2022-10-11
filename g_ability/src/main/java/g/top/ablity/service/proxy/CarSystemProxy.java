package g.top.ablity.service.proxy;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;

/**
 * Created by wanghaiguang on 2022/9/30 下午5:41
 */
public class CarSystemProxy {
    Car car;

    public Object getProxy() {
        if (car == null) {
            car = new Car();
        }
        return Proxy
                .newProxyInstance(getClass().getClassLoader(), car.getClass().getInterfaces(), new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("find proxy.... ");
                        Object invoke = method.invoke(car, args);
                        return invoke;
                    }
                });
    }
}