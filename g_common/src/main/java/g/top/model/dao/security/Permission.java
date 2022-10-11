package g.top.model.dao.security;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wanghaiguang on 2022/10/8 上午11:41
 */
@Data
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer Id;

    /**
     * 权限code
     */
    private String permissionCode;

    /**
     * 权限名
     */
    private String permissionName;

}
