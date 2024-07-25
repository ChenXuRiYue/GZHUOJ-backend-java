package com.gzhuoj.problem.service.Impl;

import com.gzhuoj.problem.constant.PathConstant;
import com.gzhuoj.problem.dto.req.ListJudgeDataReqDTO;
import com.gzhuoj.problem.mapper.ProblemMapper;
import com.gzhuoj.problem.model.entity.ProblemDO;
import com.gzhuoj.problem.service.JudgeService;
import com.gzhuoj.problem.service.ProblemService;
import common.exception.ClientException;
import common.toolkit.ZipUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.gzhuoj.problem.constant.PatternConstant.TESTCASE_PATTERN;

@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {
    private final ProblemMapper problemMapper;

    @Override
    public void judgeDataManager(ListJudgeDataReqDTO requestParam) {

    }

}
