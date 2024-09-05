package com.gzhuoj.judgeserver.constant;

import java.util.regex.Pattern;

public class PatternConstant {
    // [^\\S\\n]  -- 所有空白字符但不包括换行符
    // + 匹配多次
    // (?=\\n) -- 正向肯定断言，只有出现换行符时才执行匹配
    public final static Pattern REMOVE_END_SPACE_PATTERN = Pattern.compile("[^\\S\\n]+(?=\\n)");
}
