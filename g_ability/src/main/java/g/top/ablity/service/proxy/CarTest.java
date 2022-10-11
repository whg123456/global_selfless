package g.top.ablity.service.proxy;

/**
 * Created by wanghaiguang on 2022/9/30 下午5:42
 */
public class CarTest {
    public static void main(String[] args) {
        CarSystemProxy carSystemProxy = new CarSystemProxy();
        CarInterfaces proxy = (CarInterfaces) carSystemProxy.getProxy();
        proxy.run();
        proxy.playMusic();
        proxy.stop();
    }
}
