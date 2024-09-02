package com.gzhuoj.problem.service.resources;

import cn.hutool.core.lang.Pair;
import com.gzhuacm.sdk.problem.model.dto.ProblemJudgeResourcesRespDTO;

import java.util.List;

public interface FileResourceService {
     void insertFileResource(List<Pair<String, String>> files, Integer problemId);

     List<ProblemJudgeResourcesRespDTO> getUpLoadData(Integer problemId);
}
