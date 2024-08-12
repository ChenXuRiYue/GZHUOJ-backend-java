package com.gzhuoj.usr.dto.resp.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserBatchImportRespDTO {
    public ByteArrayResource file;
}
