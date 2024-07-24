package com.gzhuoj.usr.service;

import com.gzhuoj.usr.dto.req.JudgeUploadCaseReqDTO;

import java.util.List;

public interface JudgeService {
    List<String> uploadJudgeCase(JudgeUploadCaseReqDTO requestParam);
}
