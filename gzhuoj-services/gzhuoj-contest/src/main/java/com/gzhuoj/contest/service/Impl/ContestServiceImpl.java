package com.gzhuoj.contest.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.contest.constant.PathConstant;
import com.gzhuoj.contest.dto.req.ContestAllReqDTO;
import com.gzhuoj.contest.dto.req.ContestCreateReqDTO;
import com.gzhuoj.contest.dto.req.ContestUpdateReqDTO;
import com.gzhuoj.contest.dto.resp.ContestAllRespDTO;
import com.gzhuoj.contest.mapper.ContestDescrMapper;
import com.gzhuoj.contest.mapper.ContestMapper;
import com.gzhuoj.contest.mapper.ContestProblemMapper;
import com.gzhuoj.contest.model.entity.ContestDO;
import com.gzhuoj.contest.model.entity.ContestDescrDO;
import com.gzhuoj.contest.model.entity.ContestProblemDO;
import com.gzhuoj.contest.model.entity.ProblemMapDO;
import com.gzhuoj.contest.service.ContestService;
import common.exception.ClientException;
import common.toolkit.GenerateRandStrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            mask |= num;
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
        String identify = createUniqueDir();
        contestDO.setAttach(identify);
        baseMapper.insert(contestDO);

        contestDescrMapper.insert(new ContestDescrDO(requestParam.getContestId(), requestParam.getDescription()));

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
    public ContestDO queryByNum(Integer num) {
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getContestId, num)
                .eq(ContestDO::getDeleteFlag, 0);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<ContestAllRespDTO> all(ContestAllReqDTO requestParam) {
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getDeleteFlag, 0);
        if(!StrUtil.isEmpty(requestParam.getSearch())){
            queryWrapper.like(ContestDO::getTitle, requestParam.getSearch())
                    .or().like(ContestDO::getContestId, requestParam.getSearch());
        }
        if(requestParam.getOrder() == null){
            throw new ClientException("排序默认应为正序");
        }
        boolean flag = requestParam.getOrder().equals("asc");
        queryWrapper.orderBy(true, flag, ContestDO::getContestId);
        IPage<ContestDO> result = baseMapper.selectPage(requestParam, queryWrapper);
        return result.convert(each -> BeanUtil.toBean(each, ContestAllRespDTO.class));
    }

    @SneakyThrows
    private String createUniqueDir() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String identify = String.format((LocalDate.now().format(dateTimeFormatter))+ "_%s", GenerateRandStrUtil.getRandStr(16));
        Path path = Paths.get("");
        Path upload = path.resolve(String.format(PathConstant.CONTEST_UPLOAD_PATH, identify));
        Files.createDirectories(upload);
        return identify;
    }

    @Override
    public void update(ContestUpdateReqDTO requestParam) {
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getContestId, requestParam.getContestId())
                .eq(ContestDO::getDeleteFlag, 0);
        ContestDO hasContestDO = baseMapper.selectOne(queryWrapper);
        if(hasContestDO == null){
            throw new ClientException("比赛编号不存在");
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
            mask |= num;
        }
        Integer contestID = requestParam.getNewContestId() == null ? requestParam.getContestId() : requestParam.getNewContestId();
        ContestDO contestDO = ContestDO.builder()
                .contestId(contestID)
                .contestStatus(requestParam.getContestStatus())
                .startTime(DateUtil.parse(startTime))
                .endTime(DateUtil.parse(endTime))
                .title(requestParam.getTitle())
                .access(requestParam.getAccess())
                .languageMask(mask == 0 ? null : mask)
                .frozenMinute(requestParam.getFrozenMinute())
                .frozenAfter(requestParam.getFrozenAfter())
                .awardRatio(StrUtil.join("#", requestParam.getRatioGold(), requestParam.getRatiosilver(), requestParam.getRatiobronze()))
                .topteam(requestParam.getTopteam())
                // TODO 脱敏
                .password(requestParam.getPassword())
                .build();
        LambdaUpdateWrapper<ContestDO> updateWrapper = Wrappers.lambdaUpdate(ContestDO.class)
                .eq(ContestDO::getContestId, requestParam.getContestId())
                .eq(ContestDO::getDeleteFlag, 0);
        baseMapper.update(contestDO, updateWrapper);

        LambdaQueryWrapper<ContestDescrDO> descrQuery = Wrappers.lambdaQuery(ContestDescrDO.class)
                .eq(ContestDescrDO::getContestId, requestParam.getContestId());
        contestDescrMapper.delete(descrQuery);
        if(!StrUtil.isEmpty(requestParam.getDescription())){
            contestDescrMapper.insert(new ContestDescrDO(contestID, requestParam.getDescription()));
        }

        LambdaQueryWrapper<ContestProblemDO> deleteWrappers = Wrappers.lambdaQuery(ContestProblemDO.class)
                .eq(ContestProblemDO::getContestId, requestParam.getContestId());
        contestProblemMapper.delete(deleteWrappers);
        List<ProblemMapDO> problemMapDOList = requestParam.getProblemMapDOList();
        if(CollUtil.isNotEmpty(problemMapDOList)){
            for(int i = 0; i < problemMapDOList.size(); i++ ){
                ProblemMapDO problemMapDO = problemMapDOList.get(i);
                ContestProblemDO contestProblemDO = ContestProblemDO.builder()
                        .actualNum(i)
                        .problemId(problemMapDO.getProblemNum())
                        .problemColor(problemMapDO.getColor())
                        .contestId(contestID)
                        .build();
                contestProblemMapper.insert(contestProblemDO);
            }
        }
    }

    public static void main(String[] args) {
//        Date parse = DateUtil.parse("2024-11-12 12:42");
//        System.out.println(parse);
    }
}
