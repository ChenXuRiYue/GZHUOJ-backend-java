package com.gzhuoj.contest.service.contestBalloon.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.dto.req.ContestBalloonChangeStReqDTO;
import com.gzhuoj.contest.dto.req.ContestBalloonQueueReqDTO;
import com.gzhuoj.contest.dto.resp.ContestBalloonQueueRespDTO;
import com.gzhuoj.contest.mapper.ContestBalloonMapper;
import com.gzhuoj.contest.model.entity.ContestBalloonDO;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.service.contestBalloon.ContestBalloonService;
import com.gzhuoj.contest.service.contestProblem.ContestProblemService;
import com.gzhuoj.contest.service.contest.ContestService;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static common.convention.errorcode.BaseErrorCode.CONTEST_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class ContestBalloonServiceImpl extends ServiceImpl<ContestBalloonMapper, ContestBalloonDO> implements ContestBalloonService {
    private final ContestService contestService;
    private final ContestProblemService contestProblemService;
    @Override
    public List<ContestBalloonQueueRespDTO> queue(ContestBalloonQueueReqDTO requestParam) {
        ContestDO contestDO = contestService.queryByNum(requestParam.getCid());
        if(contestDO == null){
            throw new ClientException(CONTEST_NOT_FOUND_ERROR);
        }
        Map<Integer, Integer> map = contestProblemService
                .getAllProblem(requestParam.getCid())
                .stream()
                .collect(Collectors.toMap(ContestProblemDO::getProblemId, ContestProblemDO::getActualNum));

        LambdaQueryWrapper<ContestBalloonDO> queryWrapper = Wrappers.lambdaQuery(ContestBalloonDO.class)
                .eq(ContestBalloonDO::getContestId, requestParam.getCid())
                .eq(ContestBalloonDO::getRoom, requestParam.getRoom());
        List<ContestBalloonDO> contestBalloonDOS = baseMapper.selectList(queryWrapper);

        return contestBalloonDOS.stream()
                .map(each -> BeanUtil.toBean(each, ContestBalloonQueueRespDTO.class))
                .peek(each -> each.setActualNum(map.get(each.getProblemId())))
                .sorted(Comparator.comparing(ContestBalloonQueueRespDTO::getAcTime))
                .collect(Collectors.toList());
    }

    @Override
    public void status(ContestBalloonChangeStReqDTO requestParam) {
        LambdaUpdateWrapper<ContestBalloonDO> updateWrapper = Wrappers.lambdaUpdate(ContestBalloonDO.class)
                .eq(ContestBalloonDO::getContestId, requestParam.getCid())
                .eq(ContestBalloonDO::getProblemId, requestParam.getProblemId())
                .eq(ContestBalloonDO::getTeamAccount, requestParam.getTeamAccount());
        ContestBalloonDO contestBalloonDO = new ContestBalloonDO();
        contestBalloonDO.setBst(requestParam.getBst());
        baseMapper.update(contestBalloonDO, updateWrapper);
    }
}
