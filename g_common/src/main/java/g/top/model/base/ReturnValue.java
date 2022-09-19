package g.top.model.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import g.top.model.enums.BusinessErrorEnum;

/**
 *返回体封装
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"returnCode", "message", "result"})
public class ReturnValue<T> extends AbstractDTOBase {

    private int returnCode;

    private String message;

    private T result;


    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static <T> ReturnValue<T> buildSuccessResult(T obj) {
        ReturnValue<T> rt = new ReturnValue<>();
        rt.setReturnCode(0);
        rt.setResult(obj);
        rt.setMessage("");
        return rt;
    }

    public static ReturnValue buildErrorResult(int returnCode, String param) {
        ReturnValue rt = new ReturnValue();
        rt.setReturnCode(returnCode);
        rt.setMessage(param);
        rt.setResult(null);
        return rt;
    }

    public static ReturnValue buildErrorResult(BusinessErrorEnum errorEnum) {
        ReturnValue rt = new ReturnValue();
        rt.setReturnCode(errorEnum.getId());
        rt.setMessage(errorEnum.getDesc());
        rt.setResult(null);
        return rt;
    }
}