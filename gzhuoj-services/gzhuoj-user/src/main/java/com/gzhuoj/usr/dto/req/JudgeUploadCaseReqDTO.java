package com.gzhuoj.usr.dto.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class JudgeUploadCaseReqDTO {
    /**
     * 测试数据
     */
    List<MultipartFile> testCase;
}
