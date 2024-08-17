package com.gzhuoj.judgeserver.judge;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import com.gzhuoj.judgeserver.util.JudgeUtils;
import common.enums.SubmissionStatus;
import common.exception.ClientException;
import common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.compiler.CompileError;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 编译类
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class Compiler {
    public static String compiler(LanguageConfig languageConfig, String code, String language) {
        if(StrUtil.isEmpty(language)) {
            throw new RuntimeException("不支持该种语言 " + language);
        }
        JSONArray compile = SandboxRun.compile(
                parseCompileCommand(languageConfig.getCompileCommand()),
                languageConfig.getCompileEnvs(),
                languageConfig.getMaxCpuTime(),
                languageConfig.getMaxMemory(),
                256 * 1024 * 1024L,
                code,
                languageConfig.getSrcName(),
                languageConfig.getExeName(),
                true
        );

        JSONObject compileResult = (JSONObject) compile.get(0);
        System.out.println(compileResult);
        // 编译成功的返回status为Accepted
        if (compileResult.getInt("status").intValue() != SubmissionStatus.ACCEPTED.getCode()) {
            throw new ClientException("Compile Error.");
        }
        // fileId为编译后的文件的唯一标识
        String fileId = ((JSONObject) compileResult.get("fileIds")).getStr(languageConfig.getExeName());
        if (StringUtils.isEmpty(fileId)) {
            throw new ServiceException("Executable file not found.");
        }
        return fileId;
    }

    private static List<String> parseCompileCommand(String compileCommand) {
        return JudgeUtils.translateCommandline(compileCommand);
    }
}
