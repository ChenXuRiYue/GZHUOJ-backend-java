package com.gzhuoj.judgeserver.judge;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 编译语言加载类
 */
@Component
@Slf4j
public class LanguageConfigLoader implements InitializingBean {
    private static List<String> defaultEnv = Arrays.asList(
            "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
            "LANG=en_US.UTF-8",
            "LC_ALL=en_US.UTF-8",
            "LANGUAGE=en_US:en",
            "HOME=/w");

    private static List<String> localEnv = Arrays.asList(
        "PATH=C:\\mingw64\\bin"
    );

    private static List<String> python3Env = Arrays.asList("LANG=en_US.UTF-8",
            "LANGUAGE=en_US:en", "LC_ALL=en_US.UTF-8", "PYTHONIOENCODING=utf-8");

    private static List<String> golangCompileEnv = Arrays.asList(
            "GOCACHE=/w", "GOPATH=/w/go", "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
            "LANG=en_US.UTF-8", "LANGUAGE=en_US:en", "LC_ALL=en_US.UTF-8");

    private static List<String> golangRunEnv = Arrays.asList(
            "GOCACHE=off", "GODEBUG=madvdontneed=1",
            "PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin",
            "LANG=en_US.UTF-8", "LANGUAGE=en_US:en", "LC_ALL=en_US.UTF-8");

    private static AtomicBoolean instance = new AtomicBoolean(false);

    // 编译语言配置集合
    private static HashMap<String, LanguageConfig> languageConfigMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 单例
        if(instance.compareAndSet(false, true)) {
            // 从编译语言配置文件中获取参数
            Iterable<Object> languageConfigIter = loadYml("language.yml");
            for (Object configObj : languageConfigIter) {
                JSONObject configJson = JSONUtil.parseObj(configObj);
                // 对每种语言配置参数和组好编译运行语句
                LanguageConfig languageConfig = buildLanguageConfig(configJson);
                languageConfigMap.put(languageConfig.getLanguage(), languageConfig);
            }
            log.info("load language config: {}", languageConfigMap);
        }
    }

    /**
     * 根据语言类型获取配置
     */
    public LanguageConfig getLanguageConfigByName(String langName) {
        return languageConfigMap.get(langName);
    }

    /**
     * 编译语言配置参数
     */
    private LanguageConfig buildLanguageConfig(JSONObject configJson) {
        LanguageConfig languageConfig = LanguageConfig.builder()
                // 获取编译语言
                .language(configJson.getStr("language"))
                // 被编译的文件的路径
                .srcName(configJson.getStr("src_path"))
                // 编译后可执行文件的位置
                .exeName(configJson.getStr("exe_path"))
                .build();
        // 获取编译语句和编译参数
        JSONObject compileJson = configJson.getJSONObject("compile");
        if(compileJson != null) {
            String command = compileJson.getStr("command");
            command = command.replace("{src_path}", languageConfig.getSrcName())
                    .replace("{exe_path}", languageConfig.getExeName());
            languageConfig.setCompileCommand(command);
            String env = compileJson.getStr("env");
            // 小写好识别
            env = env.toLowerCase();
            switch (env) {
                case "python3":
                    languageConfig.setCompileEnvs(python3Env);
                    break;
                case "golang_compile":
                    languageConfig.setCompileEnvs(golangCompileEnv);
                    break;
                default:
//                    languageConfig.setCompileEnvs(defaultEnv);
                    languageConfig.setCompileEnvs(localEnv);
            }
            languageConfig.setMaxCpuTime(parseTimeStr(compileJson.getStr("maxCpuTime")));
            languageConfig.setMaxRealTime(parseTimeStr(compileJson.getStr("maxRealTime")));
            languageConfig.setMaxMemory(parseMemoryStr(compileJson.getStr("maxMemory")));
        }

        // 配置运行指令的参数
        JSONObject runJson = configJson.getJSONObject("run");
        if (runJson != null) {
            String command = runJson.getStr("command");
            command = command.replace("{exe_path}", languageConfig.getExeName());
            languageConfig.setRunCommand(command);
            String env = runJson.getStr("env");
            env = env.toLowerCase();
            switch (env) {
                case "python3":
                    languageConfig.setRunEnvs(python3Env);
                    break;
                case "golang_run":
                    languageConfig.setRunEnvs(golangRunEnv);
                    break;
                default:
                    languageConfig.setRunEnvs(defaultEnv);
            }
        }
        return languageConfig;
    }

    /**
     * 内存大小转为byte
     */
    private Long parseMemoryStr(String memoryStr) {
        if (StrUtil.isBlank(memoryStr)) {
            return 256 * 1024 * 1024L;
        }
        memoryStr = memoryStr.toLowerCase();
        if (memoryStr.endsWith("mb")) {
            return Long.parseLong(memoryStr.replace("mb", "")) * 1024 * 1024;
        } else if (memoryStr.endsWith("kb")) {
            return Long.parseLong(memoryStr.replace("kb", "")) * 1024;
        } else if (memoryStr.endsWith("b")) {
            return Long.parseLong(memoryStr.replace("b", ""));
        } else {
            return Long.parseLong(memoryStr) * 1024 * 1024;
        }
    }

    /**
     * 将时间转为ms
     */
    private Long parseTimeStr(String timeStr) {
        // null则设置为3s
        if (StrUtil.isBlank(timeStr)) {
            return 3000L;
        }
        timeStr = timeStr.toLowerCase();
        if (timeStr.endsWith("s")) {
            return Long.parseLong(timeStr.replace("s", "")) * 1000;
        } else if (timeStr.endsWith("ms")) {
            return Long.parseLong(timeStr.replace("s", ""));
        } else {
            return Long.parseLong(timeStr);
        }
    }

    private Iterable<Object> loadYml(String fileName) {
        try {
            Yaml yaml = new Yaml();
            String ymlContent = ResourceUtil.readUtf8Str(fileName);
            return yaml.loadAll(ymlContent);
        } catch (Exception e) {
            log.error("load language yaml error:", e);
            throw new RuntimeException(e);
        }
    }
}
