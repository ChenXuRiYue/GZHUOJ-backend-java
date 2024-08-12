package com.gzhuoj.contest.service.judge;

import com.gzhuoj.contest.dto.req.Judge.RegContestJudgeSubmitReqDTO;
import com.gzhuoj.contest.model.entity.SubmitDO;

public interface JudgeService {
    boolean submit(RegContestJudgeSubmitReqDTO requestParam);

    /**
     * 根据submitId查询SubmitDO
     * @param submitId 提交Id
     * @return submit对象
     */
    SubmitDO getSubmitDO(Integer submitId);
}
