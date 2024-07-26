package com.gzhuoj.problem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.problem.dto.req.ProblemSetRequestDTO;
import com.gzhuoj.problem.dto.resp.ProblemSetResponseDTO;

public interface ProblemSetService {
    IPage<ProblemSetResponseDTO> all(ProblemSetRequestDTO requestParam);
}
