package com.gzhuoj.judgeserver.judge;

import cn.hutool.core.collection.CollUtil;
import com.gzhuacm.sdk.problem.api.ProblemApi;
import com.gzhuacm.sdk.problem.model.dto.ProblemJudgeResourcesRespDTO;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import common.enums.SubmissionLanguage;
import common.exception.ServiceException;
import common.toolkit.FileUtils;
import lombok.RequiredArgsConstructor;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.gzhuoj.judgeserver.constant.PathConstant.PROBLEM_BASE_PATH;
import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_PROBLEM_RESOURCES_NOT_FOUND_ERROR;

/**
 * 判题上下文
 */
@Component
@RequiredArgsConstructor
public class JudgeContext {
    private final ProblemApi problemRemoteService;
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
        // 目标文件夹
        Path targetDirectoryPath = Paths.get("").toAbsolutePath().resolve(PROBLEM_BASE_PATH + "/" + problemRespDTO.getAttach() + "/test_case");
        // 该题目文件夹如果为空
        if(!FileUtils.isDirectoryExists(targetDirectoryPath.toString())){
            loadProblemTestCase(targetDirectoryPath, submitDO.getProblemNum());
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

    /**
     * 加载单个题目的所有题目资源到本地
     * @param targetDirectoryPath 本地目标路径
     * @param problemNum 题目编号
     */
    private void loadProblemTestCase(Path targetDirectoryPath, Integer problemNum) {
        Result<List<ProblemJudgeResourcesRespDTO>> result = problemRemoteService.upload(problemNum);
        List<ProblemJudgeResourcesRespDTO> resources = result.getData();
        if(CollUtil.isEmpty(resources)){
            throw new ServiceException(JUDGE_PROBLEM_RESOURCES_NOT_FOUND_ERROR);
        }
        try {
            Files.createDirectories(targetDirectoryPath);
        } catch (IOException e) {
            throw new RuntimeException(String.format("TestCase文件夹创建失败 Path = %s, ProblemNum = %d", targetDirectoryPath, problemNum),e);
        }
        resources.forEach(each -> {
            Path testCase = targetDirectoryPath.resolve(each.getFileName());
            try {
                Files.writeString(testCase, each.getFileContent());
            } catch (IOException e) {
                throw new RuntimeException(String.format("TestCase文件创建失败 Path = %s, ProblemNum = %d", targetDirectoryPath, problemNum),e);
            }
        });
    }
}
