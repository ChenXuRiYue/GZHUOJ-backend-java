package com.gzhuoj.judgeserver.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuoj.judgeserver.model.entity.JudgeServerDO;
import com.gzhuoj.judgeserver.service.JudgeServerService;
import com.gzhuoj.judgeserver.util.IpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * 启动时将JudgeServer实例存入数据库
 */
//@Component
@Slf4j
@RequiredArgsConstructor
public class ServiceInitRunner implements CommandLineRunner {
    private final JudgeServerService judgeServer;

    @Value("${gzhuoj-judge-server.ip}")
    private String ip;

    @Value("${gzhuoj-judge-server.port}")
    private Integer port;

    // judge服务名 唯一
    @Value("${gzhuoj-judge-server.name}")
    private String name;

    // 最大并行任务数为cpu数 + 1
    @Value("${gzhuoj-judge-server.max-task-num}")
    private Integer maxTaskNum;

    // cpu数
    private static final int cpuNum = Runtime.getRuntime().availableProcessors();

    @Override
    public void run(String... args) throws Exception {
        log.info("IP  of the current judge server:" + ip);
        log.info("Port of the current judge server:" + port);

        if (maxTaskNum == -1) {
            maxTaskNum = cpuNum + 1;
        }
        if (ip.equals("-1")) {
            ip = IpUtils.getLocalIpv4Address();
        }
        // 先把已存在的可能相同的实例删掉
        LambdaQueryWrapper<JudgeServerDO> queryWrapper = Wrappers.lambdaQuery(JudgeServerDO.class)
                .eq(JudgeServerDO::getIp, ip)
                .eq(JudgeServerDO::getPort, port);
        judgeServer.remove(queryWrapper);
        JudgeServerDO judgeServerDO = JudgeServerDO.builder()
                .cpuCore(cpuNum)
                .ip(ip)
                .port(port)
                .taskNumber(0)
                .maxTaskNumber(maxTaskNum)
                .name(name)
                .url(ip + ":" + port)
                .build();
        // 将实例存入数据库
        boolean hasSave = judgeServer.save(judgeServerDO);
        if (!hasSave){
            log.error("初始化判题机信息到数据库失败，请重新启动试试！");
        } else {
            // TODO 获取到Judge服务的状态
            HashMap<String, Object> judgeServerInfo = judgeServer.getJudgeServerInfo();
            log.info("HOJ-JudgeServer had successfully started! The judge config and sandbox config Info:" + judgeServerInfo);
        }
    }
}
