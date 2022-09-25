package g.top.ablity.service.chaintest;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wanghaiguang on 2022/9/22 下午3:00
 */
public class ChainFactory {
    public static AbstractBizService createBizServiceChain(List<String> comFilter) {
        List<BizFilterEnum> bizFilterEnums = comFilter.stream().map(BizFilterEnum::findByName)
                .sorted(Comparator.comparing(BizFilterEnum::getType))
                .collect(Collectors.toList());

        AbstractBizService head = null;
        AbstractBizService current = head;

        for (BizFilterEnum bizFilterEnum : bizFilterEnums) {
            AbstractBizService next = (AbstractBizService) SpringContext.getBean(bizFilterEnum.getName() + "Service");

            if (head == null) {
                head = next;
                current = next;
                continue;
            }
            current.setNextBizService(next);
            current = next;

        }


        return head;
    }

}
