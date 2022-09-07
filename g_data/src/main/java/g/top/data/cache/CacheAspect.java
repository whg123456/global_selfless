package g.top.data.cache;

import com.google.common.base.Strings;
import g.top.domain.ExpiryMap;
import g.top.utils.JacksonHelper;
import g.top.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeUnit;

@Aspect
@Slf4j
@Component
public class CacheAspect {

    private ExpiryMap<String, String> memoryCache = MemoryCache.getMemoryCache();


    @Around("@annotation(cache)")
    public Object process(ProceedingJoinPoint proceedingJoinPoint, Cache cache) throws Throwable {
        String key = this.getKey(proceedingJoinPoint, cache);
        String cacheValue = memoryCache.get(key);
        Method method = this.getMethod(proceedingJoinPoint);


        Object result;
        try {
            if (method.getGenericReturnType() instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
                Class<?> aClass = Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName());
                result =
                        JacksonHelper.deserialize(cacheValue, method.getReturnType(), aClass);
            } else {
                result = JacksonHelper.deserialize(cacheValue, method.getReturnType(), null);
            }
        }catch (Exception ex){
            log.error("JacksonHelper.deserialize ex",ex);
            result = null;
        }
        if (result == null) {
            result = proceedingJoinPoint.proceed();
            if(result != null){
                String jsonValue = JacksonHelper.serialize(result);
                if (cache.expireTime() == CachedConfigurer.ExpireTime.NONE) {
                    memoryCache.put(key, jsonValue);
                } else {
                    memoryCache.put(key, jsonValue, cache.expireTime().getTTL());
                }
            }
            return result;
        }
        return result;
    }

    private String getKey(ProceedingJoinPoint proceedingJoinPoint, Cache cache) {
        Method method = this.getMethod(proceedingJoinPoint);

        String argStr = "";
        Object[] args = proceedingJoinPoint.getArgs();
        if (args != null) {
            for (Object item : args) {
                argStr += ":" + item.toString();
            }
        }

        String baseKey = cache.key();
        if (Strings.isNullOrEmpty(baseKey)) {
            baseKey = StringHelper.fmt("global:service:{0}:{1}",
                    proceedingJoinPoint.getSignature().getDeclaringTypeName(),
                    method.getName());
        }

        return baseKey + argStr;
    }

    private Method getMethod(ProceedingJoinPoint proceedingJoinPoint) {
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return methodSignature.getMethod();
    }

    private Cache getAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(Cache.class);
    }
}
