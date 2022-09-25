package g.top.ablity.service.chaintest;

public enum BizFilterEnum {

    BIDPRICE(1, "a", "a的作用"),
    ESTIMATE(20, "b", "b的作用"),
    C(3, "c", "c的作用");

    private int type;

    private String name;

    private String desc;

    BizFilterEnum(int type, String name, String desc) {

        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static BizFilterEnum findByName(String name){
        for(BizFilterEnum bidCompFilterEnum : BizFilterEnum.values()){
            if(bidCompFilterEnum.getName().equals(name)){
                return bidCompFilterEnum;
            }
        }
        return null;
    }
}
