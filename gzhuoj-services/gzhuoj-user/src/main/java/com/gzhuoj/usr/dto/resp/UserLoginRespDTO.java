package com.gzhuoj.usr.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserLoginRespDTO {
    private String token;
}
