package com.gzhuoj.contest.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.dto.req.ContestCreateReqDTO;
import com.gzhuoj.contest.dto.req.ContestUpdateReqDTO;
import com.gzhuoj.contest.mapper.ContestDescrMapper;
import com.gzhuoj.contest.mapper.ContestMapper;
import com.gzhuoj.contest.mapper.ContestProblemMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.ContestDescrDO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.entity.ProblemMapDO;
import com.gzhuoj.contest.service.ContestService;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl extends ServiceImpl<ContestMapper, ContestDO> implements ContestService {

    private final ContestDescrMapper contestDescrMapper;
    private final ContestProblemMapper contestProblemMapper;
    private static final String DATE_FORMAT = "%s-%s-%s %s:%s";
    @Override
    public void create(ContestCreateReqDTO requestParam) {
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getContestId, requestParam.getContestId())
                .eq(ContestDO::getDeleteFlag, 0);
        ContestDO hasContestDO = baseMapper.selectOne(queryWrapper);
        if(hasContestDO != null){
            throw new ClientException("比赛编号已存在");
        }

        String startTime = String.format(DATE_FORMAT
                , requestParam.getStartYear()
                , requestParam.getStartMonth()
                , requestParam.getStartDay()
                , requestParam.getStartHour()
                , requestParam.getStartMinute()
        );
        String endTime = String.format(DATE_FORMAT
                , requestParam.getEndYear()
                , requestParam.getEndMonth()
                , requestParam.getEndDay()
                , requestParam.getEndHour()
                , requestParam.getEndMinute()
        );
        int mask = 0;
        for(Integer num : requestParam.getLanguage()){
            mask |= 1 << num;
        }
        ContestDO contestDO = ContestDO.builder()
                .contestId(requestParam.getContestId())
                .contestStatus(requestParam.getContestStatus())
                .startTime(DateUtil.parse(startTime))
                .endTime(DateUtil.parse(endTime))
                .title(requestParam.getTitle())
                .access(requestParam.getAccess())
                .languageMask(mask)
                .frozenMinute(requestParam.getFrozenMinute())
                .frozenAfter(requestParam.getFrozenAfter())
                .awardRatio(StrUtil.join("#", requestParam.getRatioGold(), requestParam.getRatiosilver(), requestParam.getRatiobronze()))
                .topteam(requestParam.getTopteam())
                // TODO 脱敏
                .password(requestParam.getPassword())
                .build();
        baseMapper.insert(contestDO);

        if(!StrUtil.isEmpty(requestParam.getDescription())){
//            LambdaQueryWrapper<ContestDescrDO> descrQuery = Wrappers.lambdaQuery(ContestDescrDO.class)
//                    .eq(ContestDescrDO::getContestId, requestParam.getContestId());
//            contestDescrMapper.delete(descrQuery);
            contestDescrMapper.insert(new ContestDescrDO(requestParam.getContestId(), requestParam.getDescription()));
        }

        List<ProblemMapDO> problemMapDOList = requestParam.getProblemMapDOList();
        if(CollUtil.isNotEmpty(problemMapDOList)){
            for(int i = 0; i < problemMapDOList.size(); i++ ){
                ProblemMapDO problemMapDO = problemMapDOList.get(i);
                ContestProblemDO contestProblemDO = ContestProblemDO.builder()
                        .actualNum(i)
                        .problemId(problemMapDO.getProblemNum())
                        .problemColor(problemMapDO.getColor())
                        .contestId(requestParam.getContestId())
                        .build();
                contestProblemMapper.insert(contestProblemDO);
            }
        }
    }

    @Override
    public void update(ContestUpdateReqDTO requestParam) {

    }

    public static void main(String[] args) {
//        Date parse = DateUtil.parse("2024-11-12 12:42");
//        System.out.println(parse);
    }
}
