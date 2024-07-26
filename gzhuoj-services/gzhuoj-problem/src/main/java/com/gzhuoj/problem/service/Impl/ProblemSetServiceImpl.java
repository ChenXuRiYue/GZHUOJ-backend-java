package com.gzhuoj.problem.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gzhuoj.problem.dto.req.ProblemSetRequestDTO;
import com.gzhuoj.problem.dto.resp.ProblemSetResponseDTO;
import com.gzhuoj.problem.service.ProblemSetService;
import org.springframework.stereotype.Service;

@Service
public class ProblemSetServiceImpl implements ProblemSetService {
    @Override
    public IPage<ProblemSetResponseDTO> all(ProblemSetRequestDTO requestParam) {
        return null;
    }
}
