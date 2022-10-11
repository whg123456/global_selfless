package g.top.ablity.service.proxy;

/**
 * Created by wanghaiguang on 2022/9/30 下午5:41
 */
public class Car implements CarInterfaces {
    @Override
    public void run() {
        System.out.println("Car run()");
    }

    @Override
    public void playMusic() {
        System.out.println("Car playMusic()");
    }

    @Override
    public void stop() {
        System.out.println("stop()");
    }
}
