package com.gzhuoj.judgeserver.dto.req;

import com.gzhuacm.sdk.judgeserver.model.dto.SubmitDTO;
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
