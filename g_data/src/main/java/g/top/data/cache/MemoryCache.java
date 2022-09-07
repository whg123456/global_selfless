package g.top.data.cache;

import g.top.domain.ExpiryMap;

import java.util.UUID;

/**
 * Created by wanghaiguang on 2022/9/7 下午5:38
 */
public class MemoryCache {

    private static volatile ExpiryMap<String, String> expiryMap;


    public static ExpiryMap getMemoryCache() {
        if (expiryMap == null) {
            synchronized (ExpiryMap.class) {
                if (expiryMap == null) {
                    expiryMap = new ExpiryMap();
                    return expiryMap;
                }
            }
        }
        return expiryMap;
    }

    private void MemoryCache() {}

}
