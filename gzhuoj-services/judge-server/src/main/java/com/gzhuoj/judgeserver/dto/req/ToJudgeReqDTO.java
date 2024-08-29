package com.gzhuoj.judgeserver.dto.req;

import com.gzhuacm.sdk.contest.model.dto.SubmitDTO;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import lombok.Data;

@Data
public class ToJudgeReqDTO {
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
