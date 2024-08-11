package com.gzhuoj.contest.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.mapper.SubmitMapper;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.service.SubmitService;
import org.springframework.stereotype.Service;

@Service
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, SubmitDO> implements SubmitService {
    @Override
    public SubmitDO getSubmitDO(Integer submitId) {
        LambdaQueryWrapper<SubmitDO> queryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                .eq(SubmitDO::getSubmitId, submitId);
        return baseMapper.selectOne(queryWrapper);
    }
}
