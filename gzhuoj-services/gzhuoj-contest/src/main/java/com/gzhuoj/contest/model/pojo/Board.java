package com.gzhuoj.contest.model.pojo;


import lombok.Data;

import java.util.List;

@Data
public class Board {
    private String contestNum;
    private List<PersonalScore> standings;
}
