package com.gzhuoj.contest.service.goJudge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gzhuoj.contest.config.GoUrlProperties;
import com.gzhuoj.contest.dto.req.goJudge.goJudgeStatusReqDTO;
import com.gzhuoj.contest.dto.req.goJudge.goJudgeSubmitReqDTO;
import com.gzhuoj.contest.dto.resp.goJudge.goJudgeStatusRespDTO;
import com.gzhuoj.contest.dto.resp.goJudge.goJudgeSubmitRespDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

public interface goJudgeService {
    goJudgeSubmitRespDTO Submit(goJudgeSubmitReqDTO reqDTO);


    goJudgeStatusRespDTO Status(goJudgeStatusReqDTO reqDTO);
}
