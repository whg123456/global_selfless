package g.top.utils;

import cn.hutool.core.lang.Assert;

import java.util.UUID;

/**
 * Created by wanghaiguang on 2022/6/30 上午11:15
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {
    private static final char UNDERLINE = '_';

    /**
     * 驼峰命名xxx_abc -> xxxAbc
     */
    public static String underlineToCamel(String name) {
        Assert.notNull(name, "The 'name' must not be null!");
        name = name.trim();
        int len = name.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(name.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * uuid生成没有 — 的编号
     *
     * @return
     */
    public static String createUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("\\-", "");
    }
}
