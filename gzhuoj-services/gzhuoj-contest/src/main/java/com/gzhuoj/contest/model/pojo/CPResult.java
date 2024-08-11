package com.gzhuoj.contest.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 该类用于接受查询比赛中某题的汇总结果
 */



public class CPResult {
    public String name;
    public Integer ac;
    public Integer pe;
    public Integer wa;
    public Integer tle;
    public Integer mle;
    public Integer ole;
    public Integer re;
    public Integer ce;
    public Integer total;
    public Integer c;
    public Integer cPlus;
    public Integer java;
    public Integer python;
    public Integer go;
    public CPResult()
    {
        this.ac=0;
        this.pe=0;
        this.wa=0;
        this.tle=0;
        this.mle=0;
        this.ole=0;
        this.re=0;
        this.ce=0;
        this.total=0;
        this.c=0;
        this.cPlus=0;
        this.java=0;
        this.python=0;
        this.go=0;
    }
}
