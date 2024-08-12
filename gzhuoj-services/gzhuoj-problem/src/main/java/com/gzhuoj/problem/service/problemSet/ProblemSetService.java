package com.gzhuoj.problem.service.problemSet;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.problem.dto.req.problemSet.ProblemSetRequestDTO;
import com.gzhuoj.problem.dto.resp.problemSet.ProblemSetResponseDTO;

public interface ProblemSetService {
    IPage<ProblemSetResponseDTO> all(ProblemSetRequestDTO requestParam);
}
