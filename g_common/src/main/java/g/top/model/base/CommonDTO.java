package g.top.model.base;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wanghaiguang on 2022/6/29 下午3:40
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommonDTO<T> {

    private Integer code;

    private String msg;

    private T data;

    public static final int SUCCESS = 0;
    public static final String SUCCESS_MSG = "success";
    public static final String FAIL_MSG = "fail";

    public static final int ERROR = 1;
    public static final String FAIL_EMPTY_STR = StringUtils.EMPTY;

    public static <T> CommonDTO<T> success(T obj) {
        return CommonDTO.<T>builder().code(SUCCESS).msg(SUCCESS_MSG).data(obj).build();
    }

    public static <T> CommonDTO<T> error(T obj) {
        return CommonDTO.<T>builder().code(ERROR).msg(FAIL_MSG).data(obj).build();
    }

    public static <T> CommonDTO<T> success() {
        return CommonDTO.<T>builder().code(SUCCESS).msg(SUCCESS_MSG).data(null).build();
    }

    public static CommonDTO error(String errMsg) {
        return CommonDTO.builder().code(ERROR).msg(errMsg).data(FAIL_EMPTY_STR).build();
    }

    public static CommonDTO error(Integer code, String errMsg) {
        if (null == code) {
            return CommonDTO.builder().code(ERROR).msg(errMsg).data(FAIL_EMPTY_STR).build();
        }
        return CommonDTO.builder().code(code).msg(errMsg).data(FAIL_EMPTY_STR).build();
    }
}
