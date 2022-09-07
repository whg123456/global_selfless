//package g.top.utils;
//
//import com.google.common.base.Charsets;
//import com.google.common.base.Preconditions;
//import lombok.NonNull;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.connection.ReturnType;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.time.Duration;
//import java.util.UUID;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
///**
// * Created by wanghaiguang on 2022/6/20 下午7:19
// */
//@Slf4j
//@Component
//public class RedisLockManager {
//
//    private final static byte[] UNLOCK_LUA_SCRIPT =
//            ("if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
//                    "then return redis.call(\"del\",KEYS[1]) else return 0 end").getBytes(Charsets.UTF_8);
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    /**
//     * 在redis锁中执行，完成后自动释放
//     * @param lockKey 锁的key
//     * @param lockSeconds 加锁多少秒
//     * @param execFunc 加锁成功后执行的方法
//     * @param lockFailedCallback 加锁失败回调
//     * @param errorCallback 异常回调
//     */
//    public <T> T execWithinLock(String lockKey, int lockSeconds,
//                                @NonNull Supplier<T> execFunc,
//                                @NonNull Supplier<T> lockFailedCallback,
//                                @NonNull Function<Exception, T> errorCallback) {
//        Preconditions.checkArgument(StringUtils.isNotBlank(lockKey), "lock key must not be blank");
//        Preconditions.checkArgument(lockSeconds > 0, "lock seconds must be great than 0");
//
//        String lockedToken = "";
//        try {
//            lockedToken = tryLock(lockKey, lockSeconds);
//            if (StringUtils.isBlank(lockedToken)) {
//                return lockFailedCallback.get();
//            }
//
//            // exec func
//            return execFunc.get();
//
//        } catch (Exception ex) {
//            return errorCallback.apply(ex);
//        } finally {
//            if (StringUtils.isNotBlank(lockedToken)) {
//                releaseLock(lockKey, lockedToken);
//            }
//        }
//    }
//
//    private String tryLock(String lockKey, int lockSeconds) {
//        return tryLock(lockKey, Duration.ofSeconds(lockSeconds));
//    }
//
//    private String tryLock(String lockKey, Duration lockDuration) {
//        String uuid = UUID.randomUUID().toString();
//        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, lockDuration);
//        return locked != null && locked ? uuid : null;
//    }
//
//    private boolean releaseLock(String key, String value) {
//        try{
//            RedisCallback<Boolean> callback = (connection) -> {
//                return connection.eval(UNLOCK_LUA_SCRIPT, ReturnType.BOOLEAN ,1, key.getBytes(Charsets.UTF_8), value.getBytes(Charsets.UTF_8));
//            };
//            return redisTemplate.execute(callback);
//        } catch (Exception e) {
//            log.warn("redis releaseLock error,", e);
//        }
//        return false;
//    }
//}
//
