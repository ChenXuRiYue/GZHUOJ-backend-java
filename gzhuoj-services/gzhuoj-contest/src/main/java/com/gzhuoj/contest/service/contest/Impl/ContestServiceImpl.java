package com.gzhuoj.contest.service.contest.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuacm.sdk.problem.api.ProblemApi;
import com.gzhuacm.sdk.problem.model.dto.ProblemPrintDTO;
import com.gzhuoj.contest.constant.PathConstant;
import com.gzhuoj.contest.dto.ContestDTO;
import com.gzhuoj.contest.dto.req.contest.*;
import com.gzhuoj.contest.dto.resp.contest.ContestAllRespDTO;
import com.gzhuoj.contest.dto.resp.contest.PrintProblemRespDTO;
import com.gzhuoj.contest.mapper.ContestDescrMapper;
import com.gzhuoj.contest.mapper.ContestMapper;
import com.gzhuoj.contest.mapper.ContestProblemMapper;
import com.gzhuoj.contest.model.entity.*;
import common.biz.user.UserContext;
import com.gzhuoj.contest.service.contest.ContestService;
import common.exception.ClientException;
import common.utils.GenerateRandStrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl extends ServiceImpl<ContestMapper, ContestDO> implements ContestService {

    private final ContestDescrMapper contestDescrMapper;
    private final ContestProblemMapper contestProblemMapper;
    private final ContestMapper contestMapper;
    private final ProblemApi problemApi;

    @Override
    @Transactional
    public void createContest(ContestReqDTO requestParam) {
        if(checkContestIfExist(requestParam.getContestNum())){
            throw new ClientException("比赛编号已存在");
        }
        insertContestDo(requestParam);
        insertContestDescDO(requestParam);
        insertContestProblemDO(requestParam);
    }

    /**
     * 检查当前比赛是否存在
     * @param contestNum
     * @return
     */
    public Boolean checkContestIfExist(Integer contestNum){
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getContestNum, contestNum)
                .eq(ContestDO::getDeleteFlag, 0);
        return ObjectUtils.isNotEmpty(baseMapper.selectOne(queryWrapper));
    }

    public ContestDO constructContestDOByContestRequest(ContestReqDTO requestParam){
        int mask = 0;
        for(Integer num : requestParam.getLanguage()){
            mask |= num;
        }
        ContestDO contestDO = ContestDO.builder()
                .contestNum(requestParam.getContestNum())
                .contestStatus(requestParam.getContestStatus())
                .startTime(DateUtil.date(requestParam.getStartTimes()))
                .endTime(DateUtil.date(requestParam.getEndTimes()))
                .title(requestParam.getTitle())
                .access(requestParam.getAccess())
                .languageMask(mask)
                .frozenMinute(requestParam.getFrozenMinute())
                .frozenAfter(requestParam.getFrozenAfter())
                .awardRatio(StrUtil.join("#", requestParam.getRatioGold(), requestParam.getRatioSilver(), requestParam.getRatioBronze()))
                // TODO 脱敏
                .password(requestParam.getPassword())
                .build();
        // TODO  奖牌分配信息的处理。
        String identify = createUniqueDir();
        contestDO.setAttach(identify);

        return contestDO;
    }

    public void  insertContestDo(ContestReqDTO requestParam){
        ContestDO contestDO = constructContestDOByContestRequest(requestParam);
        baseMapper.insert(contestDO);
    }

    public void insertContestDescDO(ContestReqDTO requestParam){
        ContestDescDO target = ContestDescDO.builder()
                .contestNum(requestParam.getContestNum())
                .description(requestParam.getDescription())
                .descriptionHtml(requestParam.getDescriptionHtml())
                .build();
        contestDescrMapper.insert(target);
    }

    public void insertContestProblemDO(ContestReqDTO requestParam){
        List<SelectedProblemMsgWhenCreateContest> problemMsgWhenCreateContests = requestParam.getSelectedProblemMsgWhenCreateContestList();
        if(CollUtil.isNotEmpty(problemMsgWhenCreateContests)){
            List<ContestProblemDO> contestProblemDOS = new ArrayList<>();
            // TODO 校验题目合法性。完善表逻辑
            for(int i = 0; i < problemMsgWhenCreateContests.size(); i++){
                SelectedProblemMsgWhenCreateContest problemMsg = problemMsgWhenCreateContests.get(i);
                ContestProblemDO contestProblemDO = ContestProblemDO.builder()
                        .problemNum(problemMsg.getProblemNum())
                        .contestNum(requestParam.getContestNum())
                        .problemLetterIndex(i)
                        .build();
                contestProblemDOS.add(contestProblemDO);
            }
            contestProblemMapper.batchInsert(contestProblemDOS);
        }
    }



    @Override
    public ContestDO getContestDO(Integer num) {
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getContestNum, num)
                .eq(ContestDO::getDeleteFlag, 0);
        return baseMapper.selectOne(queryWrapper);
    }


    @Override
    public void changeStatus(ContestStatusReqDTO requestParam) {
        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
                .eq(ContestDO::getContestNum, requestParam.getId())
                .eq(ContestDO::getDeleteFlag, 0);
        ContestDO hasContestDO = baseMapper.selectOne(queryWrapper);
        if(hasContestDO == null){
            throw new ClientException("比赛编号不存在");
        }
        LambdaUpdateWrapper<ContestDO> updateWrapper = Wrappers.lambdaUpdate(ContestDO.class)
                .eq(ContestDO::getContestNum, requestParam.getId())
                .eq(ContestDO::getDeleteFlag, 0);
        ContestDO contestDO = new ContestDO();
        contestDO.setContestStatus(requestParam.getStatus() ^ 1);
        baseMapper.update(contestDO, updateWrapper);
    }

    @Override
    public List<SubmitDO> submitData(Integer contestNum) {
        return contestMapper.sumbitSelectByContestNum(contestNum);
    }

    @Override
    public List<TeamDO> teamData(Integer contestNum) {
        return contestMapper.teamSelectByContestNum(contestNum);
    }

    @Override
    public PrintProblemRespDTO printProblem(Integer contestNum) {
        PrintProblemRespDTO respDTO = new PrintProblemRespDTO();
        ContestDO contestDO = contestMapper.selectByContestNum(contestNum);
        respDTO.setContest(contestDO);
        List<ContestProblemDO> contestProblemDOS = contestProblemMapper.selectByContestNum(contestNum);
        respDTO.setProblems(new ArrayList<>());
        for (ContestProblemDO PDO : contestProblemDOS) {
            Integer problemNum = PDO.getProblemNum();
            ProblemPrintDTO neww= problemApi.selectProblemById(problemNum);

            respDTO.getProblems().add(neww);
        }
        return respDTO;
    }

    @Override
    public IPage<ContestAllRespDTO> contestsView(ContestAllReqDTO requestParam) {


        QueryWrapper<ContestDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", 0);
        if(!StrUtil.isEmpty(requestParam.getSearch())){
            queryWrapper.like("title", requestParam.getSearch())
                    .or().like("contest_num", requestParam.getSearch());
        }
        Pair<String, String> order = requestParam.getOrder();
        if(ObjectUtils.isEmpty(order)){
            order = new Pair<>("contest_num", "asc");
        }
        queryWrapper.orderByAsc(StringUtils.equals("asc", order.getValue()), Collections.singletonList(order.getKey()));
        // 公开视图。 TODO userInFO
        if(!(requestParam.getType() == 1 && Objects.equals("1" , UserContext.getRole()))){
            queryWrapper.eq("contest_status", 0);
        }

        IPage<ContestDO> result = baseMapper.selectPage(requestParam, queryWrapper);
        return result.convert(each -> BeanUtil.toBean(each, ContestAllRespDTO.class));
    }

    /**
     * contest时间、编号
     */
    @Override
    public ContestDTO getBasicInfoForContestProblemView(Integer contestNum) {
        ContestDO contestDO = getContestDO(contestNum);
        return ContestDTO.builder()
                .title(contestDO.getTitle())
                .startTime(contestDO.getStartTime().getTime())
                .endTime(contestDO.getEndTime().getTime())
                .build();
    }

    /**
     *
     */

    @SneakyThrows
    private String createUniqueDir() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String identify = String.format((LocalDate.now().format(dateTimeFormatter))+ "_%s", GenerateRandStrUtil.getRandStr(16));
        Path path = Paths.get("");
        Path upload = path.resolve(String.format(PathConstant.CONTEST_UPLOAD_PATH, identify));
        Files.createDirectories(upload);
        return identify;
    }

    // TODO 代码重构
    @Override
    public void update(ContestReqDTO requestParam) {
//        LambdaQueryWrapper<ContestDO> queryWrapper = Wrappers.lambdaQuery(ContestDO.class)
//                .eq(ContestDO::getContestNum, requestParam.getContestNum())
//                .eq(ContestDO::getDeleteFlag, 0);
//        ContestDO hasContestDO = baseMapper.selectOne(queryWrapper);
//        if(hasContestDO == null){
//            throw new ClientException("比赛编号不存在");
//        }
//
//        int mask = 0;
//        if(CollUtil.isNotEmpty(requestParam.getLanguage())) {
//            for (Integer num : requestParam.getLanguage()) {
//                mask |= num;
//            }
//        }
//        Integer contestID = requestParam.getContestNum();
//        ContestDO contestDO = ContestDO.builder()
//                .contestNum(contestID)
//                .contestStatus(requestParam.getContestStatus())
//                .startTime(DateUtil.date(requestParam.))
//                .endTime(DateUtil.date(requestParam.getStartTimes()))
//                .title(requestParam.getTitle())
//                .access(requestParam.getAccess())
//                .languageMask(mask == 0 ? null : mask)
//                .frozenMinute(requestParam.getFrozenMinute())
//                .frozenAfter(requestParam.getFrozenAfter())
//                .awardRatio(StrUtil.join("#", requestParam.getRatioGold(), requestParam.getRatiosilver(), requestParam.getRatiobronze()))
//                .topteam(requestParam.getTopteam())
//                // TODO 脱敏
//                .password(requestParam.getPassword())
//                .build();
//        LambdaUpdateWrapper<ContestDO> updateWrapper = Wrappers.lambdaUpdate(ContestDO.class)
//                .eq(ContestDO::getContestNum, requestParam.getContestNum())
//                .eq(ContestDO::getDeleteFlag, 0);
//        baseMapper.update(contestDO, updateWrapper);
//
//        LambdaQueryWrapper<ContestDescDO> descrQuery = Wrappers.lambdaQuery(ContestDescDO.class)
//                .eq(ContestDescDO::getContestNum, requestParam.getContestNum());
//        contestDescrMapper.delete(descrQuery);
//        if(!StrUtil.isEmpty(requestParam.getDescription())){
//            contestDescrMapper.insert(new ContestDescDO(contestID, requestParam.getDescription()));
//        }
//
//        LambdaQueryWrapper<ContestProblemDO> deleteWrappers = Wrappers.lambdaQuery(ContestProblemDO.class)
//                .eq(ContestProblemDO::getContestNum, requestParam.getContestNum());
//        contestProblemMapper.delete(deleteWrappers);
//        List<ProblemMapDO> problemMapDOList = requestParam.getProblemMapDOList();
//        if(CollUtil.isNotEmpty(problemMapDOList)){
//            for(int i = 0; i < problemMapDOList.size(); i++ ){
//                ProblemMapDO problemMapDO = problemMapDOList.get(i);
//                ContestProblemDO contestProblemDO = ContestProblemDO.builder()
//                        .problemLetterIndex(i)
//                        .problemNum(problemMapDO.getProblemNum())
//                        .problemColor(problemMapDO.getColor())
//                        .contestNum(contestID)
//                        .build();
//                contestProblemMapper.insert(contestProblemDO);
//            }
//        }
    }

    public static void main(String[] args) {
//        Date parse = DateUtil.parse("2024-11-12 12:42");
//        System.out.println(parse);
    }
}
