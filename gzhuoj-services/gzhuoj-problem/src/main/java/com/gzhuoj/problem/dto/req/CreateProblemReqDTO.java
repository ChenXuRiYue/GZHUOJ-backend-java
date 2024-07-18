package com.gzhuoj.problem.dto.req;

import com.gzhuoj.problem.model.entity.TestExampleDO;
import lombok.Data;

import java.util.List;

@Data
public class CreateProblemReqDTO {
    // 题目在题库中的序号，录题人自定义
    int problemNum;
    String problemTitle;
    int timeLimit;
    int memoryLimit;
    String description;
    List<TestExampleDO> testExampleList;
    // 题目类型： 0 -> 普通题目， 1 -> special judge, 2 -> 交互题
    int problemType;
}
