package com.gzhuoj.problem.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.problem.dto.req.CreateProblemReqDTO;
import com.gzhuoj.problem.mapper.ProblemMapper;
import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.service.ProblemService;
import common.exception.ClientException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, ProblemDO> implements ProblemService {

        /**
         * @param createProblemReqDTO
         */
        @Override
    public void createProblem(CreateProblemReqDTO createProblemReqDTO) {
        // 处理更新时间，创建时间？
        LambdaQueryWrapper<ProblemDO> queryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                .eq(ProblemDO::getProblemNum, createProblemReqDTO.getProblemNum());
        ProblemDO problemDO = baseMapper.selectOne(queryWrapper);
        if(ObjectUtils.isNotEmpty(problemDO)){
            throw new ClientException("录题失败: 题目序号已存在");
        }
        try{
            // updateTime, createTime 这两个字段的更新方式是mybatisPlus ———— MetaObjectHandler 自动插入。
            problemDO = ProblemDO.builder()
                    .problemNum(createProblemReqDTO.getProblemNum())
                    .problemName(createProblemReqDTO.getProblemTitle())
                    .description(createProblemReqDTO.getDescription())
                    .timeLimit(createProblemReqDTO.getTimeLimit())
                    .memoryLimit(createProblemReqDTO.getMemoryLimit())
                    .ProblemType(createProblemReqDTO.getProblemType())
                    .problemStatus(0) // 默认设置为不公开
                    .build();
            baseMapper.insert(problemDO);
        } catch (Exception e){
            e.printStackTrace();
            throw new ClientException("录入失败: 插入数据库失败");
        }
    }
}
