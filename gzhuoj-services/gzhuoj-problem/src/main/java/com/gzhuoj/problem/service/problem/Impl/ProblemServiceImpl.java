package com.gzhuoj.problem.service.problem.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzhuoj.problem.constant.PathConstant;
import com.gzhuoj.problem.dto.req.problem.CreateProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.ListProblemReqDTO;
import com.gzhuoj.problem.dto.req.problem.UpdateProblemReqDTO;
import com.gzhuoj.problem.dto.resp.problem.ListProblemRespDTO;
import com.gzhuoj.problem.mapper.ProblemDescrMapper;
import com.gzhuoj.problem.mapper.ProblemMapper;
import com.gzhuoj.problem.mapper.TestExampleMapper;
import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.model.entity.ProblemDescrDO;
import com.gzhuoj.problem.model.entity.TestExampleDO;
import com.gzhuoj.problem.service.problem.ProblemService;
import common.convention.errorcode.BaseErrorCode;
import common.exception.ClientException;
import common.toolkit.GenerateRandStrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static common.convention.errorcode.BaseErrorCode.PROBLEM_ID_EXISTED;

@Service
@RequiredArgsConstructor
// 开启事务
@Transactional(propagation= Propagation.REQUIRED)
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, ProblemDO> implements ProblemService {

    private final TestExampleMapper testExampleMapper;
    private final ProblemDescrMapper problemDescrMapper;
    private final ProblemMapper problemMapper;

    // TODO 注意对象加了字段，逻辑可能发生改变
    @Override
    public void createProblem(CreateProblemReqDTO requestParam) {
        // 处理更新时间，创建时间？
        LambdaQueryWrapper<ProblemDO> queryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                .eq(ProblemDO::getProblemNum, requestParam.getProblemNum())
                .eq(ProblemDO::getDeleteFlag, 0);
        ProblemDO hasProblemDO = baseMapper.selectOne(queryWrapper);
        if (ObjectUtils.isNotEmpty(hasProblemDO)) {
            throw new ClientException("录题失败: 题目序号已存在", PROBLEM_ID_EXISTED);
        }
        try {
            // updateTime, createTime 这两个字段的更新方式是mybatisPlus ———— MetaObjectHandler 自动插入。
            ProblemDO problemDO = ProblemDO.builder()
                    .problemNum(requestParam.getProblemNum())
                    .problemName(requestParam.getProblemName())
                    .timeLimit(requestParam.getTimeLimit())
                    .memoryLimit(requestParam.getMemoryLimit())
                    .ProblemType(requestParam.getProblemType())
                    .spj(requestParam.getSpj())
                    .author(requestParam.getAuthor())
                    .submit(0)
                    .accepted(0)
                    .solved(0)
                    .problemStatus(0) // 默认设置为不公开
                    .build();

            ProblemDescrDO problemDescrDO = new ProblemDescrDO(
                    requestParam.getProblemNum(),
                    requestParam.getDescription(),
                    requestParam.getDescriptionHtml(),
                    requestParam.getInputDescription(),
                    requestParam.getInputDescriptionHtml(),
                    requestParam.getOutputDescription(),
                    requestParam.getOutputDescriptionHtml(),
                    requestParam.getExplanation()
            );
            problemDescrMapper.insert(problemDescrDO);

            List<TestExampleDO> testExampleList = requestParam.getTestExampleList();
            // 由于样例一般很小，直接用数据库存
            if (CollUtil.isNotEmpty(testExampleList)) {
                testExampleList
                        .stream()
                        .peek(each -> each.setProblemId(requestParam.getProblemNum()))
                        .forEach(testExampleMapper::insert);
            }
            // 创建题目时，在本地 data/public/problem 目录下创建可唯一标识的文件夹用于分别存储test_case和upload文件
            // test_case 用于 judge
            // upload 用于题面展示
            // identify 文件夹唯一标识
            String identify = createUniqueDir();
            problemDO.setAttach(identify);
            baseMapper.insert(problemDO);
        } catch (Exception e) {
            throw new ClientException("插入数据库过程出现错误", BaseErrorCode.SERVICE_ERROR);
        }
    }

    private String createUniqueDir() throws IOException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String identify = String.format((LocalDate.now().format(dateTimeFormatter)) + "_%s", GenerateRandStrUtil.getRandStr(16));
        Path path = Paths.get("");
        Path testCase = path.resolve(String.format(PathConstant.PROBLEM_TESTCASE_PATH, identify));
        Path upload = path.resolve(String.format(PathConstant.PROBLEM_UPLOAD_PATH, identify));
        Files.createDirectories(testCase);
        Files.createDirectories(upload);
        return identify;
    }

    @Override
    public IPage<ListProblemRespDTO> listProblem(ListProblemReqDTO requestParam) {
        LambdaQueryWrapper<ProblemDO> queryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                .eq(ProblemDO::getDeleteFlag, 0);
        if (!StrUtil.isEmpty(requestParam.getSearch())) {
            queryWrapper.like(ProblemDO::getProblemNum, requestParam.getSearch())
                    .or().like(ProblemDO::getProblemName, requestParam.getSearch());
        }
        if (requestParam.getOrder() == null) {
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
        if (hasProblemDO == null) {
            throw new ClientException("题目不存在");
        }
        Integer problemNum = requestParam.getNewProblemNum() != null ? requestParam.getNewProblemNum() : requestParam.getProblemNum();
        if (!Objects.equals(problemNum, requestParam.getProblemNum())) {
            LambdaQueryWrapper<ProblemDO> hsaQueryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                    .eq(ProblemDO::getProblemNum, problemNum)
                    .eq(ProblemDO::getDeleteFlag, 0);
            ProblemDO problemDO = baseMapper.selectOne(hsaQueryWrapper);
            if (problemDO != null) {
                throw new ClientException(PROBLEM_ID_EXISTED);
            }
        }
        ProblemDO problemDO = ProblemDO.builder()
                .problemNum(problemNum)
                .ProblemType(requestParam.getProblemType())
                .problemName(requestParam.getProblemName())
                .timeLimit(requestParam.getTimeLimit())
                .memoryLimit(requestParam.getMemoryLimit())
                .author(requestParam.getAuthor())
                .spj(requestParam.getSpj())
                .problemStatus(requestParam.getProblemStatus())
                .build();
        // 删除原本的题目描述，插入新的
        LambdaQueryWrapper<ProblemDescrDO> problemDescrDO = Wrappers.lambdaQuery(ProblemDescrDO.class)
                .eq(ProblemDescrDO::getProblemNum, requestParam.getProblemNum());
        problemDescrMapper.delete(problemDescrDO);
        ProblemDescrDO newProblemDescrDO = ProblemDescrDO.builder()
                .problemNum(requestParam.getProblemNum())
                .description(requestParam.getDescription())
                .descriptionHtml(requestParam.getDescriptionHtml())
                .inputDescription(requestParam.getInputDescrition())
                .inputDescriptionHtml(requestParam.getInputDescriptionHtml())
                .outputDescription(requestParam.getOutputDescription())
                .outputDescriptionHtml(requestParam.getOutputDescriptionHtml())
                .explanation(requestParam.getExplanation())
                .build();
//        ProblemDescrDO newProblemDescrDO = new ProblemDescrDO();
        problemDescrMapper.insert(newProblemDescrDO);

        LambdaUpdateWrapper<ProblemDO> updateWrapper = Wrappers.lambdaUpdate(ProblemDO.class)
                .eq(ProblemDO::getProblemNum, requestParam.getProblemNum())
                .eq(ProblemDO::getDeleteFlag, 0);
        baseMapper.update(problemDO, updateWrapper);

        List<TestExampleDO> testExampleList = requestParam.getTestExampleList();
        if (CollUtil.isNotEmpty(testExampleList)) {
            LambdaQueryWrapper<TestExampleDO> testExampleDOWrapper = Wrappers.lambdaQuery(TestExampleDO.class)
                    .eq(TestExampleDO::getProblemId, requestParam.getProblemNum())
                    .eq(TestExampleDO::getDeleteFlag, 0);
            testExampleMapper.delete(testExampleDOWrapper);
            testExampleList.stream()
                    .peek(each -> each.setProblemId(problemNum))
                    .forEach(testExampleMapper::insert);
        }
    }

    @Override
    public ProblemDO queryProByNum(Integer num) {
        LambdaQueryWrapper<ProblemDO> queryWrapper = Wrappers.lambdaQuery(ProblemDO.class)
                .eq(ProblemDO::getProblemNum, num)
                .eq(ProblemDO::getDeleteFlag, 0);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public ProblemDO selectProblemById(Integer id) {
        return problemMapper.selectProblemById(id);
    }

    @SneakyThrows
    public static void main(String[] args) {
        Path currentPath = Paths.get("").toAbsolutePath();
        Path testCase = currentPath.resolve("data/public/problem/test_case");
        Path upload = currentPath.resolve(String.format("data/public/problem/upload/%s", "ljc"));
        Files.createDirectories(upload);
        System.out.println(testCase);
        System.out.println(upload);
    }
}
