package g.top.model.enums;


/**
 * @description: 自定义异常
 * @author: WangBoWen
 * @date: 2021-11-23
 **/
public class BusinessException extends RuntimeException {
    private int code;

    public int getCode() {
        return code;
    }

    public BusinessException(int code, String message){
        super(message);

        this.code = code;
    }
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);

        this.code = code;
    }

    public static BusinessException fromEnum(BusinessErrorEnum errorEnum) {
        return fromEnum(errorEnum, null);
    }

    public static BusinessException fromEnum(BusinessErrorEnum errorEnum, Throwable cause) {
        return new BusinessException(errorEnum.getId(), errorEnum.getDesc(), cause);
    }
}
