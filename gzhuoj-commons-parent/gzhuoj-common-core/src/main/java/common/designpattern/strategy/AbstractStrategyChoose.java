package common.designpattern.strategy;

import common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.STRATEGY_EXECUTION_ERROR;
import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.STRATEGY_MARK_NULL_ERROR;

@RequiredArgsConstructor
@Slf4j
public class AbstractStrategyChoose implements ApplicationListener<ApplicationReadyEvent> {
    private final ApplicationContext applicationContext;

    private final Map<String, AbstractExecuteStrategy> abstractExecuteStrategyMap = new HashMap<>();

    /**
     * 根据 mark 查询具体策略
     *
     * @param mark 策略唯一标识
     */
    public AbstractExecuteStrategy choose(String mark) {
        return Optional.ofNullable(abstractExecuteStrategyMap.get(mark))
                .orElseThrow(() -> new ServiceException(STRATEGY_MARK_NULL_ERROR));
    }

    /**
     * 选择策略并执行
     */
    public <REQUEST1, REQUEST2> void chooseAndExecute(String mark, REQUEST1 requestParam1, REQUEST2 requestParam2) {
        try {
            AbstractExecuteStrategy executeStrategy = choose(mark);
            executeStrategy.execute(requestParam1, requestParam2);
        } catch (ServiceException e) {
            // 日志记录
            log.error("策略选择失败: {}", e.getMessage());
            // 自定义处理逻辑，如回滚等
            throw e;
        } catch (Exception e) {
            log.error("策略执行时发生未知错误: {}", e.getMessage());
            throw new ServiceException(STRATEGY_EXECUTION_ERROR);
        }
    }
    /**
     * 选择策略并执行且返回结果
     */
    public <REQUEST1, REQUEST2, RESPONSE> RESPONSE chooseAndExecuteResp(String mark, REQUEST1 requestParam1, REQUEST2 requestParam2) {
        try {
            AbstractExecuteStrategy executeStrategy = choose(mark);
            return (RESPONSE) executeStrategy.executeResp(requestParam1, requestParam2);
        } catch (ServiceException e) {
            // 日志记录
            log.error("策略选择失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("策略执行时发生未知错误: {}", e.getMessage());
            throw new ServiceException(STRATEGY_EXECUTION_ERROR);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Map<String, AbstractExecuteStrategy> strategyMap = applicationContext.getBeansOfType(AbstractExecuteStrategy.class);
        strategyMap.forEach((beanName, bean) -> {
            String mark = bean.mark();
            if (abstractExecuteStrategyMap.containsKey(mark)) {
                // 捕获并处理重复加载策略的异常
                log.warn("策略 {} 已存在，跳过重复加载。", mark);
            } else {
                abstractExecuteStrategyMap.put(mark, bean);
            }
        });
    }
}
