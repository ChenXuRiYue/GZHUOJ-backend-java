package com.gzhuacm.sdk.problem.model.dto;


import lombok.Data;

import java.util.List;

@Data
public class ProblemPrintDTO {
    Object problemDO;
    Object problemDescrDO;
    List<Object> testExampleDO;
}