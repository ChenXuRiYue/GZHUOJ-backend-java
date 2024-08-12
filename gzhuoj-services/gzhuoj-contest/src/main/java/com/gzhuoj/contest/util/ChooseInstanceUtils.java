package com.gzhuoj.contest.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuoj.contest.model.entity.JudgeServerDO;
import com.gzhuoj.contest.service.judge.JudgeServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 选择可使用的评测机实例
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class ChooseInstanceUtils {
    private final DiscoveryClient discoveryClient;
    private final JudgeServerService judgeServerService;

    @Value("${gzhuoj.judge.server-name}")
    private String serverName;

    public List<ServiceInstance> getInstances(String serviceId) {
        try {
            // 获取指定服务的所有健康实例
            return discoveryClient.getInstances(serviceId);
        } catch (Exception e) {
            log.error("获取微服务健康实例发生异常--------->{}", e);
            return Collections.emptyList();
        }
    }

    public JudgeServerDO chooseServer() {
        List<ServiceInstance> instances = getInstances(serverName);
        if (instances.isEmpty()) {
            return null;
        }

        ArrayList<String> url = new ArrayList<>();
        for(ServiceInstance instance : instances) {
            url.add(instance.getHost() + ":" + instance.getPort());
        }

        LambdaQueryWrapper<JudgeServerDO> queryWrapper = Wrappers.lambdaQuery(JudgeServerDO.class)
                .in(JudgeServerDO::getUrl, url)
                // 优先选评测压力小的
                .orderByAsc(JudgeServerDO::getTaskNumber)
                // 加入悲观锁防止有点rowlock被释放
                .last("for update");
        List<JudgeServerDO> judgeServerDOS = judgeServerService.list(queryWrapper);

        for (JudgeServerDO judgeServerDO : judgeServerDOS) {
            if(judgeServerDO.getTaskNumber() < judgeServerDO.getMaxTaskNumber()) {
                // 增加一个任务数
                judgeServerDO.setTaskNumber(judgeServerDO.getTaskNumber() + 1);
                boolean hasUpdate = judgeServerService.updateById(judgeServerDO);
                if(hasUpdate) {
                    return judgeServerDO;
                }
            }
        }
        return null;
    }
}
