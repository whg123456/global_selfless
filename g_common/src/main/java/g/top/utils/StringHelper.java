package g.top.utils;


import com.google.common.base.Charsets;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 */
public class StringHelper {

    /**
     * 字符串合并方法，返回一个合并后的字符串,像.net里的format函数
     *
     * @param str
     * @param args
     * @return
     */
    public static String fmt(String str, Object... args) {
        if (str == null || "".equals(str) || args.length == 0) {
            return str;
        }

        String result = str.intern();
        Pattern p = Pattern.compile("\\{(\\d+)\\}");
        java.util.regex.Matcher m = p.matcher(str);

        while (m.find()) {
            int index = Integer.parseInt(m.group(1));
            if (index < args.length) {
                result = result.replace(m.group(), args[index] == null ? "" : args[index].toString());
            }
        }
        return result;
    }

    public final static String md5(String data) {
        Hasher hasher = Hashing.md5().newHasher();
        hasher.putString(data, Charsets.UTF_8);
        return hasher.hash().toString().toUpperCase();
    }
    public static String createSign(Map<String, Object> paramsMap, String appkey, String className) {
        SortedMap<String, Object> p = new TreeMap<String, Object>(paramsMap);
        List<String> keys = new ArrayList<String>(p.keySet());
        StringBuilder sb = new StringBuilder();
        sb.append(appkey);
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = p.get(key);
            if (!key.equals("sign"))
                sb.append(key + value.toString());
        }
        sb.append(appkey);
        String mark = String.valueOf(System.currentTimeMillis()) + String.valueOf((int) (Math.random() * 1000));
//        logger.error(String.format("[" + mark + "]接口[" + className + "]签名前字符串信息:[%s]", sb.toString()));
        MessageDigest md;
        StringBuffer buf = new StringBuffer("");
        try {
            md = MessageDigest.getInstance("MD5");
            try {
                md.update(sb.toString().getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
//        logger.error(String.format("[" + mark + "]接口[" + className + "]主软件服务器签名:[%s]", buf.toString().toUpperCase()));
        return buf.toString().toUpperCase();
    }

}
