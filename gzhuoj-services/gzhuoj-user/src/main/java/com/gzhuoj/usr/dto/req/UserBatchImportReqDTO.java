package com.gzhuoj.usr.dto.req;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserBatchImportReqDTO {
    private String schoolEngName;
    MultipartFile userExcelFile;
}
