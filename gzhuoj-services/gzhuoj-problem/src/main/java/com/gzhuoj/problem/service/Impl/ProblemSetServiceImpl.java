package com.gzhuoj.problem.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuoj.problem.dto.req.ProblemSetRequestDTO;
import com.gzhuoj.problem.dto.resp.ListProblemRespDTO;
import com.gzhuoj.problem.dto.resp.ProblemSetResponseDTO;
import com.gzhuoj.problem.mapper.ProblemMapper;
import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.service.ProblemSetService;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemSetServiceImpl implements ProblemSetService {
    private final ProblemMapper problemMapper;
    @Override
    public IPage<ProblemSetResponseDTO> all(ProblemSetRequestDTO requestParam) {
        LambdaQueryWrapper<ProblemDO> queryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                .eq(ProblemDO::getProblemStatus, 1)
                .eq(ProblemDO::getDeleteFlag, 0);
        if(!StrUtil.isEmpty(requestParam.getSearch())){
            queryWrapper.like(ProblemDO::getProblemNum, requestParam.getSearch())
                    .or().like(ProblemDO::getProblemName, requestParam.getSearch());
        }
        if (requestParam.getOrder() == null) {
            throw new ClientException("排序默认应为正序");
        }
        boolean flag = requestParam.getOrder().equals("asc");
        queryWrapper.orderBy(true, flag, ProblemDO::getProblemNum);
        IPage<ProblemDO> result = problemMapper.selectPage(requestParam, queryWrapper);
        return result.convert(each -> BeanUtil.toBean(each, ProblemSetResponseDTO.class));
    }
}
