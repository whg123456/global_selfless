package g.top.data.cache;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cache {
    String key() default "";

    CachedConfigurer.ExpireTime expireTime() default CachedConfigurer.ExpireTime.NONE;
}
