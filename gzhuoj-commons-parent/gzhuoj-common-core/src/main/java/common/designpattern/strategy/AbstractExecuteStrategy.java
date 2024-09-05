package common.designpattern.strategy;

public interface AbstractExecuteStrategy<REQUEST1, REQUEST2, RESPONSE> {
    /**
     * 执行策略标识
     */
    default String mark(){
        return null;
    }

    /**
     * 执行策略
     */
    default void execute(REQUEST1 requestParam1, REQUEST2 requestParam2){

    }

    /**
     * 执行策略并带有返回值
     */
    default RESPONSE executeResp(REQUEST1 requestParam1, REQUEST2 requestParam2){
        return null;
    }
}
