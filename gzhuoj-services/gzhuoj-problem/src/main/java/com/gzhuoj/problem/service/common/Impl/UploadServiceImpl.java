package com.gzhuoj.problem.service.common.Impl;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.problem.model.entity.ProblemJudgeResourcesDO;
import com.gzhuoj.problem.service.problem.ProblemService;
import com.gzhuoj.problem.service.common.UploadService;
import com.gzhuoj.problem.service.resources.FileResourceService;
import common.exception.ClientException;
import common.toolkit.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {
    private final ProblemService problemService;
    private final FileResourceService fileResourceService;

    @Value("${Judge.max-file-num}")
    private Integer MAX_FILE_NUM;


    /**
     * 默认上传的数据就是一个 zip 压缩包。 防止出错不允许上传其它类型数据。
     * @param problemNum
     * @param testCase
     * @param path
     * @param pattern
     * @return
     */
    @SneakyThrows
    @Override
    public void upload(Integer problemNum, List<MultipartFile> testCase, String path, Pattern pattern) {
        CheckZipValidity(testCase);
        checkProblemIfExist(problemNum);
        unzipAndSaveTestCase(testCase, problemNum);
    }

    private void CheckZipValidity( List<MultipartFile> testCase){
        // 空, 或者长度为1.
        if(CollectionUtils.isEmpty(testCase) || testCase.size() != 1){
            throw new ClientException(BaseErrorCode.PROBLEM_DATA_SET_FILES_ILLEGAL);
        }
        // 文件格式必须是 ”zip"
        MultipartFile file = testCase.get(0);
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String fileExt = getExt(fileName).toLowerCase();
        if(ObjectUtil.notEqual(fileExt, "zip")){
            throw new ClientException(BaseErrorCode.PROBLEM_DATA_SET_FILES_ILLEGAL);
        }
    }
    private void checkProblemIfExist(Integer problemNum){
        ProblemRespDTO problemRespDTO = problemService.queryProByNum(problemNum);
        if(problemRespDTO == null){
            throw new ClientException("题目不存在");
        }
    }

    @SneakyThrows
    private void unzipAndSaveTestCase(List<MultipartFile> testCase, Integer problemId) {
        // 确保前提已经满足
        MultipartFile zipFile = testCase.get(0);
        List<Pair<String, String>> filesContents = ZipUtils.decompressZipToStrings(zipFile);
        fileResourceService.insertFileResource(filesContents, problemId);
    }

    private String getDirPath(String targetFilePath) {
        return targetFilePath.length() <= 4 ? "" : targetFilePath.substring(0, targetFilePath.lastIndexOf('.'));
    }

    private static String getExt(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return idx == -1 ? "" : fileName.substring(idx + 1);
    }
}
