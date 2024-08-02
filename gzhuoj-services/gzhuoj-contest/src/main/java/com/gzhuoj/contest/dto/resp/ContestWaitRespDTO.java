package com.gzhuoj.contest.dto.resp;

import lombok.Data;

import java.sql.Time;

public class ContestWaitRespDTO {
    public String teamName;
    public String contestName;
    public Integer teamTotal;

    public Long days;
    public Long hours;
    public Long minutes;
    public Long seconds;
}
