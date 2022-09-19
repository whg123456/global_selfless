package g.top.model.dao.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by on 2022/6/24 下午3:20
 * @author wanghaiguang
 */
@Data
public class User {
    private Integer id;

    private Integer chatUserId;

    private String userName;

    private Integer status;

    private List<Integer> friendIds;

    private Integer isDel;

    private LocalDateTime ctime;

    private LocalDateTime utime;
}
