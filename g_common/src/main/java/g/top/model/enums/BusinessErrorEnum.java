package g.top.model.enums;

/**
 * 异常枚举示例
 */
public enum BusinessErrorEnum {
    INVALID_LOGIN_TOKEN(401, "user limit"),
    UNKNOWN_ERR(500, "服务器未知异常"),
    SAVE_FAILED(3001,"保存失败"),
    UPDATE_FAILED(3002,"更新失败"),
    DELETE_FAILED(3003,"删除失败"),
    NO_REWARD_SCOPE(3004,"发奖scope不存在"),
    NO_ACTIVITY_REWARD_SCOPE(3005,"活动未配置发奖scope"),
    EXIST_ACTIVITY_CODE(3006,"该活动code已经存在"),
    OUTER_PORT_REQUEST_FAILED(3007,"外部接口请求失败"),
    EXIST_PERIOD_CODE(3009,"该周期code已存在"),
    PARAM_ERROR(3008,"参数有误"),
    INVENTORY_REDUCTION(3010,"减库存失败"),
    GET_PAGE_UUID_FAILED(3011,"获取pageUuid失败"),
    EXIST_AUTO_COIN_OR_RED_PACKET_CASH(3012,"该活动已存在汽车币奖品或现金奖品配置"),
    ULP_INFO_NOT_FOUND(4001,"页面信息未找到");

    private int id;
    private String desc;

    BusinessErrorEnum(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
