package com.gzhuoj.judgeserver.judge;

import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import com.gzhuoj.judgeserver.remote.DTO.ContestRemoteService;
import com.gzhuoj.judgeserver.remote.DTO.resp.ProblemRespDTO;
import common.enums.SubmissionLanguage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JudgeStrategy {
    private final LanguageConfigLoader languageConfigLoader;

    private final ContestRemoteService contestRemoteService;

    public Map<String, Object> judge(ProblemRespDTO problemRespDTO, SubmitDO submitDO) {
        HashMap<String, Object> result = new HashMap<>();
        String userFileId = null;
        try{
            LanguageConfig languageConfig = languageConfigLoader
//                    .getLanguageConfigByName(SubmissionLanguage.getLangById(submitDO.getLanguage()));
                    .getLanguageConfigByName("C++ 17 With O2");
            if(languageConfig.getCompileCommand() != null){
                userFileId = Compiler.compiler(languageConfig,
                        contestRemoteService.getCode(submitDO.getSubmitId()).getData(),
                        SubmissionLanguage.getLangById(submitDO.getLanguage())
                );
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
