package com.gzhuoj.judgeserver.judge;

import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import com.gzhuoj.judgeserver.remote.DTO.resp.ProblemRespDTO;
import common.enums.SubmissionLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 判题上下文
 */
@Component
@RequiredArgsConstructor
public class JudgeContext {
    private final LanguageConfigLoader languageConfigLoader;

    private final JudgeStrategy judgeStrategy;

    public SubmitDO judge(ProblemRespDTO problemRespDTO, SubmitDO submitDO) {
        LanguageConfig languageConfig = languageConfigLoader
                .getLanguageConfigByName(SubmissionLanguage.getLangById(submitDO.getLanguage()));
        // 对于非C的语言给两倍空间和时间
        if(languageConfig.getSrcName() != null &&
                !(languageConfig.getSrcName().endsWith(".c") || languageConfig.getSrcName().endsWith(".cpp"))) {
            problemRespDTO.setTimeLimit(problemRespDTO.getTimeLimit() * 2);
            problemRespDTO.setMemoryLimit(problemRespDTO.getMemoryLimit() * 2);
        }

        // judge结果用于判断错误信息
        Map<String, Object> judgeResult = judgeStrategy.judge(problemRespDTO, submitDO);
        return SubmitDO.builder()
                .submitId(submitDO.getSubmitId())
                .status((Integer) judgeResult.get("code"))
                .memory((Integer) judgeResult.get("maxMemory"))
                .execTime((Integer) judgeResult.get("maxTime"))
                .build();
    }
}
