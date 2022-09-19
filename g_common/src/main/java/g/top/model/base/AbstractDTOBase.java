package g.top.model.base;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * 抽象类
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseStrategy.class)
public abstract class AbstractDTOBase {

}
