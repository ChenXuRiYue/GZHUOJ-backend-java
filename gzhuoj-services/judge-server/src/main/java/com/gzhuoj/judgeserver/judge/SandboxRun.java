package com.gzhuoj.judgeserver.judge;

import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import common.enums.SubmissionStatus;
import common.exception.ClientException;
import common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class SandboxRun {
    private final static RestTemplate restTemplate;

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        // 设置连接超时为20秒
        requestFactory.setConnectTimeout(20000);
        // 设置读取超时为3分钟
        requestFactory.setReadTimeout(180000);
        restTemplate = new RestTemplate(requestFactory);
    }
    private static final String SANDBOX_BASE_URL = "http://localhost:5050";

    private static final Integer maxProcNum = 100;

    private static final Integer STDIO_SIZE_MB = 32;

    private static final Integer maxProcessNumber = 128;
    // 实现单例
    private static volatile SandboxRun instance;
    private SandboxRun(){

    }
    public static SandboxRun getInstance(){
        if(instance == null){
            synchronized (SandboxRun.class){
                if(instance == null){
                    instance = new SandboxRun();
                }
            }
        }
        return instance;
    }

    /**
     * 跑测试数据
     * @param args cmd命令参数
     * @param runEnvs 运行环境
     * @param exeName 可执行文件扩展名
     * @param testCaseInputPath 该测试点的绝对路径
     * @param maxTime 沙箱最大评测时间
     * @param fileId 编译文件的全局唯一标识
     * @param maxStack 运行最大栈空间
     * @param maxMemory 最大内存
     * @return 沙箱评测返回值
     */
    public static JSONArray testCase(List<String> args,
                                      List<String> runEnvs,
                                      String exeName,
                                      String testCaseInputPath,
                                      Long maxTime,
                                      String fileId,
                                      Integer maxStack,
                                      Long maxMemory) {
        JSONObject cmd = new JSONObject();
        cmd.set("args", args);
        cmd.set("env", runEnvs);

        JSONArray files = new JSONArray();
        JSONObject testCaseInput = new JSONObject();
        testCaseInput.put("src", testCaseInputPath);

        JSONObject stdout = new JSONObject();
        stdout.set("name", "stdout");
        stdout.set("max", 1024 * 1024 * 16);
        files.put(testCaseInput);
        files.put(stdout);

        JSONObject stderr = new JSONObject();
        stderr.set("name", "stderr");
        stderr.set("max", 1024 * 1024 * 16);
        files.put(stderr);

        cmd.set("files", files);

        cmd.set("cpuLimit", maxTime * 1000 * 1000L);
        cmd.set("clockLimit", maxTime * 1000 * 1000L * 3);

        cmd.set("memoryLimit", (maxMemory + 100) * 1024 * 1024L);
        cmd.set("procLimit", maxProcessNumber);
        cmd.set("stackLimit", maxStack * 1024 * 1024L);

        JSONObject exeFile = new JSONObject();
        exeFile.set("fileId", fileId);
        JSONObject copyIn = new JSONObject();
        copyIn.set(exeName, exeFile);

        JSONArray copyOut = new JSONArray();
        copyOut.put("stderr");
        copyOut.put("stdout");
        cmd.set("copyIn", copyIn);
        cmd.set("copyOut", copyOut);

        JSONObject param = new JSONObject();
        param.set("cmd", new JSONArray().put(cmd));

        // 调用判题安全沙箱
        JSONArray result = instance.run("/run", param);

        JSONObject testcaseRes = (JSONObject) result.get(0);
        testcaseRes.set("originalStatus", testcaseRes.getStr("status"));
        testcaseRes.set("status", RESULT_MAP_STATUS.get(testcaseRes.getStr("status")));
        return result;
    }

    public JSONArray run(String uri, JSONObject requestParam){
        HttpHeaders httpHeaders = new HttpHeaders();
        // json格式
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(JSONUtil.toJsonStr(requestParam), httpHeaders);
        ResponseEntity<String> postForEntity;
        try {
            postForEntity = restTemplate.postForEntity(SANDBOX_BASE_URL + uri, httpEntity, String.class);
            return JSONUtil.parseArray(postForEntity.getBody());
        } catch (RestClientResponseException ex) {
            if (ex.getRawStatusCode() != 200) {
                throw new ServiceException("Cannot connect to sandbox service.");
            }
        } catch (Exception e) {
            throw new ServiceException("Call SandBox Error.");
        }
        return null;
    }
    // 评测机返回结果类型
    public static final HashMap<String, Integer> RESULT_MAP_STATUS = new HashMap<>();

    static {
        RESULT_MAP_STATUS.put("Time Limit Exceeded", SubmissionStatus.TIME_LIMIT_EXCEED.getCode());
        RESULT_MAP_STATUS.put("Memory Limit Exceeded", SubmissionStatus.MEMORY_LIMIT_EXCEED.getCode());
        RESULT_MAP_STATUS.put("Output Limit Exceeded", SubmissionStatus.OUTPUT_LIMIT_EXCEED.getCode());
        RESULT_MAP_STATUS.put("Accepted", SubmissionStatus.ACCEPTED.getCode());
        RESULT_MAP_STATUS.put("Nonzero Exit Status", SubmissionStatus.NONZERO_EXIT_STATUS.getCode());
        RESULT_MAP_STATUS.put("Internal Error", SubmissionStatus.INTERNAL_ERROR.getCode());
        RESULT_MAP_STATUS.put("File Error", SubmissionStatus.FILE_ERROR.getCode());
        RESULT_MAP_STATUS.put("Signalled", SubmissionStatus.SIGNALLED.getCode());
    }

    private static final JSONArray COMPILE_FILES = new JSONArray();

    static {
        JSONObject content = new JSONObject();
        content.set("content", "");

        JSONObject stdout = new JSONObject();
        stdout.set("name", "stdout");
        stdout.set("max", 1024 * 1024 * STDIO_SIZE_MB);

        JSONObject stderr = new JSONObject();
        stderr.set("name", "stderr");
        stderr.set("max", 1024 * 1024 * STDIO_SIZE_MB);
        COMPILE_FILES.put(content);
        COMPILE_FILES.put(stdout);
        COMPILE_FILES.put(stderr);
    }

    /**
     * 源代码编译
     * @param args cmd参数
     * @param env 环境参数
     * @param cpuLimit CPU时间限制，单位纳秒
     * @param memoryLimit 内存限制，单位 byte
     * @param stackLimit 栈内存限制，单位 byte
     * @param code 源代码
     * @param srcName 源文件名称
     * @param exeName 扩展名
     * @param needCopyOutCached 可执行文件的名称
     * @return
     */
    public static JSONArray compile(List<String> args,
                                    List<String> env,
                                    Long cpuLimit, // S
                                    Long memoryLimit,
                                    Long stackLimit,
                                    String code,
                                    String srcName,
                                    String exeName,
                                    Boolean needCopyOutCached){
        JSONObject cmd = new JSONObject();
        cmd.set("args", args);
        cmd.set("env", env);
        cmd.set("cpuLimit", cpuLimit * 1000 * 1000L);
        cmd.set("clockLimit", cpuLimit * 1000 * 1000 * 2L);

        cmd.set("memoryLimit", memoryLimit);
        cmd.set("stackLimit", stackLimit);
        cmd.set("procLimit", maxProcNum);

        JSONObject fileContent = new JSONObject();
        fileContent.set("content", code);
        JSONObject copyIn = new JSONObject();
        copyIn.set(srcName, fileContent);

        cmd.set("copyIn", copyIn);

        cmd.set("files", COMPILE_FILES);

        if (needCopyOutCached) {
            cmd.set("copyOutCached", new JSONArray().put(exeName+".exe"));
        }

        cmd.set("copyOut", new JSONArray().put("stdout").put("stderr"));
        JSONObject request = new JSONObject();
        request.set("cmd", new JSONArray().put(cmd));
        JSONArray result = getInstance().run("/run", request);
        JSONObject compileRes = (JSONObject) result.get(0);
        compileRes.set("originalStatus", compileRes.getStr("status"));
        compileRes.set("status", RESULT_MAP_STATUS.get(compileRes.getStr("status")));
        return result;
    }
}
