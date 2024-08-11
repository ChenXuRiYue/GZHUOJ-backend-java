package com.gzhuoj.contest.model.pojo;

import com.gzhuoj.contest.model.entity.SubmitDO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 评测机评测对象
 */
@Data
public class ToJudgeDTO {

    /**
     * 评测对象实体
     */
    private SubmitDO submitDO;

    /**
     *  调用判题机的ip
     */
    private String judgeServerIp;

    /**
     *  调用判题机的port
     */
    private Integer judgeServerPort;
}
