package com.gzhuoj.problem.service.resources.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Pair;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuacm.sdk.problem.model.dto.ProblemJudgeResourcesRespDTO;
import com.gzhuoj.problem.mapper.ProblemJudgeResourcesMapper;
import com.gzhuoj.problem.model.entity.ProblemJudgeResourcesDO;
import com.gzhuoj.problem.service.resources.FileResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FileResourcesServiceImpl implements FileResourceService {
    private final ProblemJudgeResourcesMapper problemJudgeResourcesMapper;

    @Override
    public void insertFileResource(List<Pair<String, String>> files, Integer ProblemNum) {
        for (Pair<String, String> file : files) {
            ProblemJudgeResourcesDO problemJudgeResourcesDO = ProblemJudgeResourcesDO.builder()
                    .problemNum(ProblemNum)
                    .fileName(file.getKey())
                    .fileContent(file.getValue())
                    .build();
            problemJudgeResourcesMapper.insert(problemJudgeResourcesDO);
        }
    }

    @Override
    public List<ProblemJudgeResourcesRespDTO> getUpLoadData(Integer problemNum) {
        LambdaQueryWrapper<ProblemJudgeResourcesDO> queryWrapper = Wrappers.lambdaQuery(ProblemJudgeResourcesDO.class)
                .eq(ProblemJudgeResourcesDO::getProblemNum, problemNum)
                .eq(ProblemJudgeResourcesDO::getDeleteFlag, 0);
        List<ProblemJudgeResourcesDO> judgeResourcesDOS = problemJudgeResourcesMapper.selectList(queryWrapper);
        return judgeResourcesDOS.stream().map(each -> BeanUtil.toBean(each, ProblemJudgeResourcesRespDTO.class)).collect(Collectors.toList());
    }
}
