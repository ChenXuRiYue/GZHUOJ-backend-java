package com.gzhuoj.judgeserver.remote.DTO.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class SubmitRemoteDTO {
    /**
     * 提交编号
     */
    private Integer SubmitId;

    /**
     * 评测机
     */
    private String judger;

    /**
     * 评测状态
     */
    private Integer status;
}
