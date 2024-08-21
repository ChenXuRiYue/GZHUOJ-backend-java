package com.gzhuoj.judgeserver.judge;

import cn.hutool.json.JSONObject;
import com.gzhuoj.judgeserver.judge.JudgeType.DefaultJudge;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import com.gzhuoj.judgeserver.remote.DTO.ContestRemoteService;
import com.gzhuoj.judgeserver.remote.DTO.resp.ProblemRespDTO;
import common.convention.errorcode.BaseErrorCode;
import common.enums.SubmissionLanguage;
import common.enums.SubmissionStatus;
import common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JudgeStrategy {
    private final LanguageConfigLoader languageConfigLoader;

    private final ContestRemoteService contestRemoteService;

    private final JudgeRun judgeRun;

    public Map<String, Object> judge(ProblemRespDTO problemRespDTO, SubmitDO submitDO) {
        String userFileId = null;
        try{
            LanguageConfig languageConfig = languageConfigLoader
                    .getLanguageConfigByName(SubmissionLanguage.getLangById(submitDO.getLanguage()));
            if(languageConfig.getCompileCommand() != null){
                // 通过沙箱编译后返回全局唯一的编译后文件的唯一标识
                userFileId = Compiler.compiler(languageConfig,
                        contestRemoteService.getCode(submitDO.getSubmitId()).getData(),
                        SubmissionLanguage.getLangById(submitDO.getLanguage())
                );
            }
            // 当前工作目录
            Path absolutePath = Paths.get("").toAbsolutePath();
            Path relativePath = Paths.get("data", "public", "problem", problemRespDTO.getAttach(), "test_case");
            // test绝对路径
            Path testCasePath = absolutePath.resolve(relativePath);
            // 读取testCase目录下的每一个.in
            List<List<String>> allTestFromLocal = getAllTestFromLocal(testCasePath);
            List<String> testCaseInputList = allTestFromLocal.get(0);
            List<String> testCaseOutputList = allTestFromLocal.get(1);

            // 对每个测试点进行多线程评测
            List<JSONObject> result = judgeRun.MulThreadJudge(testCaseInputList, testCaseOutputList, submitDO, problemRespDTO, userFileId);
            return getJudgeInfo(result, problemRespDTO, submitDO);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对每个测试点结果进行分析，返回本次提交的整体结果
     */
    private Map<String, Object> getJudgeInfo(List<JSONObject> result, ProblemRespDTO problemRespDTO, SubmitDO submitDO) {
        int maxTime = 0;
        int maxMemory = 0;
        Integer resStatus = -1;
        String errorMsg = null;
        for(JSONObject jsonObject : result){
            int time = jsonObject.getLong("time").intValue();
            int memory = jsonObject.getLong("memory").intValue();
            Integer status = jsonObject.getInt("status");
            if(resStatus == -1 && status != SubmissionStatus.ACCEPTED.getCode()){
                resStatus = status;
                errorMsg = jsonObject.getStr("errMsg");
            }
            maxTime = Math.max(maxTime, time);
            maxMemory = Math.max(maxMemory, memory);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("maxTime", maxTime);
        resultMap.put("maxMemory", maxMemory);
        if(resStatus == -1){
            resultMap.put("code", SubmissionStatus.ACCEPTED.getCode());
        } else {
            resultMap.put("code", resStatus);
            resultMap.put("msg", errorMsg);
        }
        return resultMap;
    }

    private List<List<String>> getAllTestFromLocal(Path testCasePath) throws IOException {
        List<String> testCaseInputList = new ArrayList<>();
        List<String> testCaseOutputList = new ArrayList<>();
        if(Files.exists(testCasePath)){
            if(Files.isDirectory(testCasePath)) {
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(testCasePath)) {
                    for (Path childPath : directoryStream) {
                        if (childPath.toString().endsWith(".in")){
                            testCaseInputList.add(childPath.toString());
                        } else {
                            testCaseOutputList.add(childPath.toString());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        HashMap<String, List<String>> getAscMap = new HashMap<>();
        for (int i = 0; i < testCaseInputList.size(); i++) {
            String s = testCaseInputList.get(i).substring(0, testCaseInputList.get(i).lastIndexOf("."));
            String t = testCaseOutputList.get(i).substring(0, testCaseOutputList.get(i).lastIndexOf("."));
            if(!getAscMap.containsKey(s)) {
                getAscMap.put(s, new ArrayList<>());
            }
            getAscMap.get(s).add(testCaseInputList.get(i));

            if(!getAscMap.containsKey(t)) {
                getAscMap.put(t, new ArrayList<>());
            }
            getAscMap.get(t).add(testCaseOutputList.get(i));
        }
        testCaseOutputList.clear();
        testCaseInputList.clear();
        for(String dirPath : getAscMap.keySet()) {
            if(getAscMap.get(dirPath).size() != 2){
                throw new ServiceException(BaseErrorCode.JUDGE_TESTCASE_NUMBER_NOT_SAME_ERROR);
            }
            for(String item : getAscMap.get(dirPath)){
                if(item.endsWith(".in")){
                    testCaseInputList.add(item);
                } else {
                    StringBuilder output = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new FileReader(item))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            output.append(line).append("\r\n"); // 逐行读取并追加换行符
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    testCaseOutputList.add(DigestUtils.md5DigestAsHex(DefaultJudge.removeEndSpace(output.toString()).getBytes(StandardCharsets.UTF_8)));
                }
            }
        }
        return Arrays.asList(testCaseInputList, testCaseOutputList);
    }

    public static void main(String[] args) {
        Path path = Paths.get("C:\\Users\\11493\\Desktop\\GZHUOJ\\GZHUOJ-backend-java\\data\\public\\problem\\2024-08-17_C7ZK1B0PJ8S6NTIV\\test_case");
        if (Files.exists(path)){
            System.out.println("yes");
        }
    }
}
