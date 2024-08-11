package com.gzhuoj.contest.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName(value = "submit_code")
public class SubmitCodeDO {
    /**
     * 提交编号
     */
    private Integer submitId;

    /**
     * 源代码
     */
    private String code;
}
