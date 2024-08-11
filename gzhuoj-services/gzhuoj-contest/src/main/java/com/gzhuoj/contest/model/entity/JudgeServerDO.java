package com.gzhuoj.contest.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@TableName("judge_server")
public class JudgeServerDO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 判题服务名字
     */
    private String name;

    /**
     * 判题机ip
     */
    private String ip;

    /**
     * 判题机端口号
     */
    private Integer port;

    /**
     * ip:port
     */
    private String url;

    /**
     * 判题机所在服务器cpu核心数
     */
    private Integer cpuCore;

    /**
     * 当前判题数
     */
    private Integer taskNumber;

    /**
     * 判题并发最大数
     */
    private Integer maxTaskNumber;

    /**
     * 0可用，1不可用
     */
    private Integer status;
}
