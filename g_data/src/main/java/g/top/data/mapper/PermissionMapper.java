package g.top.data.mapper;

import g.top.model.dao.security.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by wanghaiguang on 2022/10/8 上午11:43
 */
@Mapper
@Repository
public interface PermissionMapper{

    @Select("select count(*) from sys_permission")
    int selectCount();
}
