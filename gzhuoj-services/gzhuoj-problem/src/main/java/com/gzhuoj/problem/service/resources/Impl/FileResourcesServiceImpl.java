package com.gzhuoj.problem.service.resources.Impl;

import cn.hutool.core.lang.Pair;
import com.gzhuoj.problem.mapper.ProblemJudgeResourcesMapper;
import com.gzhuoj.problem.model.entity.ProblemJudgeResourcesDO;
import com.gzhuoj.problem.service.resources.FileResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FileResourcesServiceImpl implements FileResourceService {
    private final ProblemJudgeResourcesMapper problemJudgeResourcesMapper;

    @Override
    public void insertFileResource(List<Pair<String, String>> files, Integer ProblemId) {
        for (Pair<String, String> file : files) {
            ProblemJudgeResourcesDO problemJudgeResourcesDO = ProblemJudgeResourcesDO.builder()
                    .problemId(ProblemId)
                    .fileName(file.getKey())
                    .fileContent(file.getValue())
                    .build();
            problemJudgeResourcesMapper.insert(problemJudgeResourcesDO);
        }
    }
}
