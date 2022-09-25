package g.top.ablity.service.chaintest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 获取容器工具
 */
@Component
public final class SpringContext implements ApplicationContextAware {

    private static ApplicationContext ctx;

    /**
     * 通过spring配置文件中配置的bean id取得bean对象
     *
     * @param name the name of the bean to retrieve
     * @return spring bean对象
     */
    public static Object getBean(String name) {
        Assert.notNull(ctx, "ApplicationContext is null");
        return ctx.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        Assert.notNull(ctx, "ApplicationContext is null");
        return ctx.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) {
        Assert.notNull(ctx, "ApplicationContext is null");
        return ctx.getBean(requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationcontext) throws BeansException {
        ctx = applicationcontext;
    }
}
