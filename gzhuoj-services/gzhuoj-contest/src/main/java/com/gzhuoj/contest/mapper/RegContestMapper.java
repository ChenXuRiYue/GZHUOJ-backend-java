package com.gzhuoj.contest.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzhuoj.contest.dto.req.regContest.RegContestSubmissionsReqDTO;
import com.gzhuoj.contest.dto.resp.regContest.RegContestSubmissionRespDTO;
import org.apache.ibatis.annotations.Param;

public interface RegContestMapper {
    IPage<RegContestSubmissionRespDTO> querySubmissions(Page<RegContestSubmissionRespDTO> page, @Param("request") RegContestSubmissionsReqDTO request);
}
