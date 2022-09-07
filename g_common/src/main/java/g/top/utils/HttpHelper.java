package g.top.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class HttpHelper {

    static ConcurrentHashMap<Long, HttpHelper> map = new ConcurrentHashMap<>();
    OkHttpClient okHttpClient;

    /**
     * 构建OkHttpClient
     *
     * @param millisecond
     * @return
     */
    public static HttpHelper getInstance(long millisecond) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        if (log.isDebugEnabled()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        HttpHelper httpHelper = map.computeIfAbsent(millisecond, k -> new HttpHelper(new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(millisecond, TimeUnit.MILLISECONDS)
                .readTimeout(millisecond, TimeUnit.MILLISECONDS).build()));

        return httpHelper;
    }

    public static HttpHelper getInstance() {
        return getInstance(5000);
    }

    private HttpHelper(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public HttpResult httpGet(String url) {
        return httpGet(url, null, null);
    }

    public HttpResult httpGet(String url, Map<String, Object> fields) {
        return httpGet(url, fields, null);
    }

    public HttpResult httpGet(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Request.Builder requestBuilder = new Request.Builder();

        if (fields != null && fields.size() > 0) {
            String query = String.join("&", fields.entrySet().stream().map(p -> p.getKey() + "=" + (p.getValue() == null ? "" : p.getValue().toString())).collect(Collectors.toList()));
            if (URI.create(url).getQuery() != null && URI.create(url).getQuery().trim() != "")
                url += "&" + query;
            else
                url += "?" + query;
        }

        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }

        Request request = requestBuilder
                .url(url)
                .build();
        log.debug("request url:{}", url);
        return call(request);
    }

    public <T, K> T httpGet(String url, Map<String, Object> fields, Map<String, Object> headers, Class<T> parametrized, Class<K> parameterClass) {
        HttpResult httpGet = httpGet(url, fields, headers);
        if (httpGet != null) {
            return getResultFormJson(httpGet, parametrized, parameterClass);
        }
        return null;
    }

    public <T> T httpGet(String url, Map<String, Object> fields, Map<String, Object> headers, TypeReference<T> clazz) {
        HttpResult httpGet = httpGet(url, fields, headers);
        if (httpGet != null) {
            return getResultFormJson(httpGet, clazz);
        }
        return null;
    }

    public <T, K> T getResultFormJson(HttpResult httpResult, Class<T> parametrized, Class<K> parameterClass) {
        if (httpResult.getStatusCode() == 200 && httpResult.getBody() != null) {
            if (parameterClass == null) {
                return JacksonHelper.deserialize(httpResult.getBody(), parametrized);
            }
            return JacksonHelper.deserialize(httpResult.getBody(), parametrized, parameterClass);
        }
        return null;
    }

    public <T> T getResultFormJson(HttpResult httpResult, Class<T> clazz) {
        if (httpResult.getStatusCode() == 200 && httpResult.getBody() != null) {
            return (T) JacksonHelper.deserialize(httpResult.getBody(), clazz);
        }
        return null;
    }

    public <T> T getResultFormJson(HttpResult httpResult, TypeReference<T> clazz) {
        if (httpResult.getStatusCode() == 200 && httpResult.getBody() != null) {
            return (T) JacksonHelper.deserialize(httpResult.getBody(), clazz);
        }
        return null;
    }

    public <T, K> T httpPost(String url, Map<String, Object> fields, Map<String, Object> headers, Class<T> parametrized, Class<K> parameterClass) {
        HttpResult httpPost = httpPost(url, fields, headers);
        if (httpPost != null) {
            return getResultFormJson(httpPost, parametrized, parameterClass);
        }
        return null;
    }


    private HttpResult call(Request request) {
        String url = request.url().toString();

        Stopwatch stopwatch = Stopwatch.createStarted();
        Call call = okHttpClient.newCall(request);
        HttpResult build = HttpResult.build(0, null);
        try {
            Response response = call.execute();
            build = HttpResult.build(response.code(), response.body().string());

            return build;
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        } finally {
            stopwatch.stop();
            long duration = stopwatch.elapsed(TimeUnit.MILLISECONDS);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("request url:").append(url).append("\t");
            stringBuilder.append("costs:").append(duration).append("ms").append("\t");
            // 请求耗时大于100毫秒的记录日志

            if (build.getStatusCode() != 200) {
                stringBuilder.append("ResponseCode:").append(build.getStatusCode()).append("\n").append("ResponseBody:").append(build.getBody());
                log.error(stringBuilder.toString());
            } else if (duration > 1000) {
                log.warn(stringBuilder.toString());
            } else if (duration > 300) {
                log.info(stringBuilder.toString());
            } else if (duration > 100) {
                log.debug(stringBuilder.toString());
            } else {
                log.trace(stringBuilder.toString());
            }

        }
        return build;
    }

    public HttpResult httpPost(String url, Map<String, Object> fields) {
        return httpPost(url, fields, null);
    }

    public HttpResult httpPostJson(String url, String body) {
        return httpPostJson(url, body, null);
    }

    public <T, K> T httpPostJson(String url, String requestBody, Map<String, Object> headers, Class<T> parametrized, Class<K> parameterClass) {
        HttpResult httpPost = httpPostJson(url, requestBody, headers);
        if (httpPost != null) {
            return getResultFormJson(httpPost, parametrized, parameterClass);
        }
        return null;
    }

    public HttpResult httpPostJson(String url, String body, Map<String, Object> headers) {

        Request.Builder requestBuilder = new Request.Builder();

        RequestBody reqBody = RequestBody.create(MediaType.parse("application/json"), body);

        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }

        Request request = requestBuilder
                .url(url)
                .post(reqBody)
                .build();
        log.debug("request url:{}", url);
        return call(request);
    }

    public HttpResult httpPost(String url, Map<String, Object> fields, Map<String, Object> headers) {
        Request.Builder requestBuilder = new Request.Builder();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (fields != null) {
            fields.entrySet().forEach(p -> bodyBuilder.add(p.getKey(), (p.getValue() == null ? "" : p.getValue().toString())));
        }
        if (headers != null) {
            headers.entrySet().forEach(p -> {
                if (p.getKey() != null && p.getValue() != null)
                    requestBuilder.header(p.getKey(), p.getValue().toString());
            });
        }
        Request request = requestBuilder
                .url(url)
                .post(bodyBuilder.build())
                .build();

        return call(request);
    }

    public HttpResult httpPostUTF8(String url, Map<String, Object> fields) {
        StringBuffer sb = new StringBuffer();
        //设置表单参数
        for (String key : fields.keySet()) {
            sb.append(key + "=" + fields.get(key) + "&");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), sb.toString());
        //创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        return call(request);
    }

    public static class HttpResult {
        private int httpStatusCode;
        private String body;

        private HttpResult(int httpStatusCode, String body) {
            this.httpStatusCode = httpStatusCode;
            this.body = body;
        }

        public int getStatusCode() {
            return httpStatusCode;
        }

        public String getBody() {
            return body;
        }

        public static HttpResult build(int httpStatusCode, String body) {
            return new HttpResult(httpStatusCode, body);
        }
    }

    public String params(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, Object>> entrySet = params.entrySet();

            for (Map.Entry<String, Object> entry : entrySet) {
                sb.append(entry.getKey());
                sb.append("=");
                try {
                    String value = entry.getValue() == null ? "" : entry.getValue().toString();
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
