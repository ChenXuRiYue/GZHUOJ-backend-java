package com.gzhuoj.contest.validator;

import com.gzhuoj.contest.dto.req.Judge.RegContestJudgeSubmitReqDTO;
import common.enums.SubmissionLanguage;
import common.exception.ClientException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static common.convention.errorcode.BaseErrorCode.SUBMISSION_LANGUAGE_NOT_SUPPORT;
import static common.convention.errorcode.BaseErrorCode.SUBMISSION_LENGTH_TOO_LONG;

@Component
public class JudgeValidator {
    private final static List<Integer> SUPPORT_LANGUAGE = Arrays.asList(
            SubmissionLanguage.C.getCode(),
            SubmissionLanguage.Go.getCode(),
            SubmissionLanguage.Java.getCode(),
            SubmissionLanguage.CPlusPlus.getCode(),
            SubmissionLanguage.Python.getCode()
    );

    /**
     * 评测参数校验
     */
    public void submitArgChecker(RegContestJudgeSubmitReqDTO requestParam){
        // 评测语言是否支持
        if(!SUPPORT_LANGUAGE.contains(requestParam.getLanguage())){
            throw new ClientException(SUBMISSION_LANGUAGE_NOT_SUPPORT);
        }
        //代码长度限制
        if(requestParam.getCode().length() > 65535){
            throw new ClientException(SUBMISSION_LENGTH_TOO_LONG);
        }
    }
}
