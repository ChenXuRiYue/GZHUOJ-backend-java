package com.gzhuoj.problem.service;

import com.gzhuoj.problem.dto.req.ListJudgeDataReqDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Pattern;

public interface JudgeService {
    void judgeDataManager(ListJudgeDataReqDTO requestParam);
}
