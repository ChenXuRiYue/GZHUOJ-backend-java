package com.gzhuacm.sdk.contest.model.dto;

import lombok.Data;

/**
 * 评测机评测对象
 */
@Data
public class ToJudgeDTO {

    /**
     * 评测对象实体
     */
    private SubmitDTO submitDTO;

    /**
     *  调用判题机的ip
     */
    private String judgeServerIp;

    /**
     *  调用判题机的port
     */
    private Integer judgeServerPort;
}
