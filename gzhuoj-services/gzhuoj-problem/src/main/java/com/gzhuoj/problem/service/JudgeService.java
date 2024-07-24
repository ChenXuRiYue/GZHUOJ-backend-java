package com.gzhuoj.problem.service;

import com.gzhuoj.problem.dto.req.ListJudgeDataReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface JudgeService {
    void judgeDataManager(ListJudgeDataReqDTO requestParam);

    Boolean upload(Integer problemNum, List<MultipartFile> testCase);
}
