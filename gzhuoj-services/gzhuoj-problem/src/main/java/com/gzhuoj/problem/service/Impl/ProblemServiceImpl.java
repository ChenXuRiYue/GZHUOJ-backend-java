package com.gzhuoj.problem.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.problem.dto.req.CreateProblemReqDTO;
import com.gzhuoj.problem.dto.req.ListProblemReqDTO;
import com.gzhuoj.problem.dto.req.UpdateProblemReqDTO;
import com.gzhuoj.problem.dto.resp.ListProblemRespDTO;
import com.gzhuoj.problem.mapper.ProblemMapper;
import com.gzhuoj.problem.mapper.TestCaseMapper;
import com.gzhuoj.problem.mapper.TestExampleMapper;
import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.model.entity.TestCaseDO;
import com.gzhuoj.problem.model.entity.TestExampleDO;
import com.gzhuoj.problem.service.ProblemService;
import common.exception.ClientException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, ProblemDO> implements ProblemService {

    private final TestCaseMapper testCaseMapper;
    private final TestExampleMapper testExampleMapper;
    @Override
    public void createProblem(CreateProblemReqDTO requestParam) {
        // 处理更新时间，创建时间？
        LambdaQueryWrapper<ProblemDO> queryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                .eq(ProblemDO::getProblemNum, requestParam.getProblemNum())
                .eq(ProblemDO::getDeleteFlag, 0);
        ProblemDO hasProblemDO = baseMapper.selectOne(queryWrapper);
        if(ObjectUtils.isNotEmpty(hasProblemDO)){
            throw new ClientException("录题失败: 题目序号已存在");
        }
        try{
            // updateTime, createTime 这两个字段的更新方式是mybatisPlus ———— MetaObjectHandler 自动插入。
            ProblemDO problemDO = ProblemDO.builder()
                    .problemNum(requestParam.getProblemNum())
                    .problemName(requestParam.getProblemName())
                    .description(requestParam.getDescription())
                    .timeLimit(requestParam.getTimeLimit())
                    .memoryLimit(requestParam.getMemoryLimit())
                    .ProblemType(requestParam.getProblemType())
                    .problemStatus(0) // 默认设置为不公开
                    .build();
            baseMapper.insert(problemDO);
            List<TestExampleDO> testExampleList = requestParam.getTestExampleList();
            if(CollUtil.isNotEmpty(testExampleList)) {
                testExampleList
                        .stream()
                        .peek(each -> each.setProblemId(requestParam.getProblemNum()))
                        .forEach(testExampleMapper::insert);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new ClientException("录入失败: 插入数据库失败");
        }
    }

    @Override
    public IPage<ListProblemRespDTO> listProblem(ListProblemReqDTO requestParam) {
        LambdaQueryWrapper<ProblemDO> queryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                .eq(ProblemDO::getDeleteFlag, 0);
        if(!StrUtil.isEmpty(requestParam.getSearch())){
            queryWrapper.like(ProblemDO::getProblemNum, requestParam.getSearch())
                    .or().like(ProblemDO::getProblemName, requestParam.getSearch());
        }
        if(requestParam.getOrder() == null){
            throw new ClientException("排序默认应为正序");
        }
        boolean flag = requestParam.getOrder().equals("asc");
        queryWrapper.orderBy(true, flag, ProblemDO::getProblemNum);
        IPage<ProblemDO> result = baseMapper.selectPage(requestParam, queryWrapper);
        return result.convert(each -> BeanUtil.toBean(each, ListProblemRespDTO.class));
    }

    @Override
    public void updateProblem(UpdateProblemReqDTO requestParam) {
        LambdaQueryWrapper<ProblemDO> queryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                .eq(ProblemDO::getProblemNum, requestParam.getProblemNum())
                .eq(ProblemDO::getDeleteFlag, 0);
        ProblemDO hasProblemDO = baseMapper.selectOne(queryWrapper);
        if(hasProblemDO == null){
            throw new ClientException("题目不存在");
        }
        Integer problemNum = requestParam.getNewProblemNum() != null ? requestParam.getNewProblemNum() : requestParam.getProblemNum();
        ProblemDO problemDO = ProblemDO.builder()
                .problemNum(problemNum)
                .ProblemType(requestParam.getProblemType())
                .problemName(requestParam.getProblemName())
                .timeLimit(requestParam.getTimeLimit())
                .memoryLimit(requestParam.getMemoryLimit())
                .description(requestParam.getDescription())
                .build();
        LambdaUpdateWrapper<ProblemDO> updateWrapper = Wrappers.lambdaUpdate(ProblemDO.class)
                .eq(ProblemDO::getProblemNum, requestParam.getProblemNum())
                .eq(ProblemDO::getDeleteFlag, 0);
        baseMapper.update(problemDO, updateWrapper);

        List<TestExampleDO> testExampleList = requestParam.getTestExampleList();
        if(CollUtil.isNotEmpty(testExampleList)){
            LambdaQueryWrapper<TestExampleDO> testExampleDOWrapper = Wrappers.lambdaQuery(TestExampleDO.class)
                    .eq(TestExampleDO::getProblemId, requestParam.getProblemNum())
                    .eq(TestExampleDO::getDeleteFlag, 0);
            testExampleMapper.delete(testExampleDOWrapper);
            testExampleList.stream()
                    .peek(each -> each.setProblemId(problemNum))
                    .forEach(testExampleMapper::insert);
        }
    }
}
