package com.gzhuoj.usr.service.Impl;

import com.gzhuoj.usr.dto.req.JudgeUploadCaseReqDTO;
import com.gzhuoj.usr.service.JudgeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Override
    public List<String> uploadJudgeCase(JudgeUploadCaseReqDTO requestParam) {
        return List.of();
    }
}
