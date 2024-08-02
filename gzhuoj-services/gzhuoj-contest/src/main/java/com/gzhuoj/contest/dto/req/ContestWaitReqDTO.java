package com.gzhuoj.contest.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContestWaitReqDTO {
    private String teamAccount;
    private Integer contestId;


}
