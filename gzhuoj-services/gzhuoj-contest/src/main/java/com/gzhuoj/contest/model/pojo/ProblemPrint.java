package com.gzhuoj.contest.model.pojo;

import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.model.entity.ProblemDescrDO;
import com.gzhuoj.problem.model.entity.TestExampleDO;
import lombok.Data;

@Data
public  class ProblemPrint {
    ProblemDO problemDO;
    ProblemDescrDO problemDescrDO;
    TestExampleDO testExampleDO;
}