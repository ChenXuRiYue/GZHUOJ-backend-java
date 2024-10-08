package com.gzhuoj.contest.service.judge.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.mapper.SubmitCodeMapper;
import com.gzhuoj.contest.mapper.SubmitMapper;
import com.gzhuoj.contest.model.entity.SubmitCodeDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuacm.sdk.contest.model.dto.SubmitRemoteDTO;
import com.gzhuoj.contest.service.judge.SubmitService;
import common.enums.SubmissionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, SubmitDO> implements SubmitService {
    private final SubmitCodeMapper submitCodeMapper;

    @Override
    public SubmitDO getSubmitDO(Integer submitId) {
        LambdaQueryWrapper<SubmitDO> queryWrapper = Wrappers.lambdaQuery(SubmitDO.class)
                .eq(SubmitDO::getSubmitId, submitId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean updateSubmitDO(SubmitRemoteDTO requestParam) {
        LambdaUpdateWrapper<SubmitDO> updateWrapper = Wrappers.lambdaUpdate(SubmitDO.class)
                .eq(SubmitDO::getSubmitId, requestParam.getSubmitId())
                .ne(SubmitDO::getStatus, SubmissionStatus.STATUS_CANCELLED.getCode());
        int update = baseMapper.update(BeanUtil.toBean(requestParam, SubmitDO.class), updateWrapper);
        return update != 0;
    }

    @Override
    public String getCode(Integer submitId) {
        LambdaQueryWrapper<SubmitCodeDO> queryWrapper = Wrappers.lambdaQuery(SubmitCodeDO.class)
                .eq(SubmitCodeDO::getSubmitId, submitId);
        SubmitCodeDO submitCodeDO = submitCodeMapper.selectOne(queryWrapper);
        return submitCodeDO.getCode();
    }
}
