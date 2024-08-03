package com.gzhuoj.contest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.mapper.ContestProblemMapper;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.service.ContestProblemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestProblemServiceImpl extends ServiceImpl<ContestProblemMapper, ContestProblemDO> implements ContestProblemService {
    @Override
    public List<ContestProblemDO> getAllProblem(Integer cid) {
        LambdaQueryWrapper<ContestProblemDO> queryWrapper = Wrappers.lambdaQuery(ContestProblemDO.class)
                .eq(ContestProblemDO::getContestId, cid);
        return baseMapper.selectList(queryWrapper);
    }
}
