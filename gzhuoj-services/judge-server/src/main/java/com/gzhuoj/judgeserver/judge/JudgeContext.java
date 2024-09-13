package com.gzhuoj.judgeserver.judge;

import cn.hutool.core.collection.CollUtil;
import com.gzhuacm.sdk.problem.api.ProblemApi;
import com.gzhuacm.sdk.problem.model.dto.ProblemJudgeResourcesRespDTO;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import common.enums.Language;
import common.exception.ServiceException;
import common.toolkit.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class JudgeContext {
    private final ProblemApi problemRemoteService;
    private final LanguageConfigLoader languageConfigLoader;
    private final JudgeStrategy judgeStrategy;
    private final Path basePath = Paths.get("").toAbsolutePath().resolve(PROBLEM_BASE_PATH);

    public SubmitDO judge(ProblemRespDTO problemRespDTO, SubmitDO submitDO) {
        LanguageConfig languageConfig = languageConfigLoader
                .getLanguageConfigByName(Language.getLangById(submitDO.getLanguage()));
        // 对于非C的语言给两倍空间和时间
        if(languageConfig.getSrcName() != null &&
                !(languageConfig.getSrcName().endsWith(".c") || languageConfig.getSrcName().endsWith(".cpp"))) {
            problemRespDTO.setTimeLimit(problemRespDTO.getTimeLimit() * 2);
            problemRespDTO.setMemoryLimit(problemRespDTO.getMemoryLimit() * 2);
        }
        // 目标文件夹
        Path targetDirectoryPath = basePath.resolve(problemRespDTO.getAttach() + "/test_case");
        // 该题目文件夹如果为空
        if(!FileUtils.isDirectoryExists(targetDirectoryPath.toString())){
            loadProblemTestCase(targetDirectoryPath, submitDO.getProblemNum());
        }
        // judge结果用于判断错误信息
        Map<String, Object> judgeResult = judgeStrategy.judge(problemRespDTO, submitDO);
        //TODO judge AC
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
            // 创建目标目录
            Files.createDirectories(targetDirectoryPath);

            // 写入所有测试用例文件
            for (ProblemJudgeResourcesRespDTO resource : resources) {
                Path testCasePath = targetDirectoryPath.resolve(resource.getFileName());
                Files.write(testCasePath, resource.getFileContent().getBytes());
            }
        } catch (IOException e) {
            log.error("Failed to load problem test cases for problemNum = {}", problemNum, e);
            throw new RuntimeException("加载测试用例失败", e);
        }
    }
}
