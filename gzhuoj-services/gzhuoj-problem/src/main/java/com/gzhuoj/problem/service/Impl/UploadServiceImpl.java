package com.gzhuoj.problem.service.Impl;

import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.service.ProblemService;
import com.gzhuoj.problem.service.UploadService;
import common.exception.ClientException;
import common.toolkit.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    @Value("${Judge.max-file-num}")
    private Integer MAX_FILE_NUM;

    @SneakyThrows
    @Override
    public Boolean upload(Integer problemNum, List<MultipartFile> testCase, String path, Pattern pattern) {
        if(testCase.size() > MAX_FILE_NUM){
            throw new ClientException("一次性上传的文件数不能超过" + MAX_FILE_NUM + "个");
        }
        boolean fail= false;
        for(MultipartFile file : testCase){
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            if(!pattern.matcher(fileName).matches()){
                throw new ClientException("文件名不合法");
            }
            ProblemDO problemDO = problemService.queryProByNum(problemNum);
            if(problemDO == null){
                throw new ClientException("题目不存在");
            }
            String fileExt = getExt(fileName).toLowerCase();
            File targetFile = new File(String.format(path, problemDO.getAttach()), fileName);
            // 将文件传输到对应文件夹
            file.transferTo(targetFile.toPath());
            // 解压zip
            if(Objects.equals(fileExt, "zip")){
                String targetFilePath = targetFile.getPath();
                fail = ZipUtils.decompressZip(targetFilePath, getDirPath(targetFilePath), pattern);
                Files.delete(targetFile.toPath());
            }
        }
        return fail;
    }
    private String getDirPath(String targetFilePath) {
        return targetFilePath.length() <= 4 ? "" : targetFilePath.substring(0, targetFilePath.lastIndexOf('.'));
    }

    private static String getExt(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return idx == -1 ? "" : fileName.substring(idx + 1);
    }
}
