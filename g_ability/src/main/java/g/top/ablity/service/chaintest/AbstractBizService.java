package g.top.ablity.service.chaintest;

import com.alibaba.ttl.TransmittableThreadLocal;
import g.top.model.base.ReturnValue;

/**
 * Created by wanghaiguang on 2022/9/22 上午11:45
 */
public abstract class AbstractBizService {
    private TransmittableThreadLocal<AbstractBizService> nextBizService = new TransmittableThreadLocal();


    public void setNextBizService(AbstractBizService bizService) {
        this.nextBizService.set(bizService);
    }

    abstract public ReturnValue action(BizDto bizDto, BizContext bizContext) throws Exception;
}
