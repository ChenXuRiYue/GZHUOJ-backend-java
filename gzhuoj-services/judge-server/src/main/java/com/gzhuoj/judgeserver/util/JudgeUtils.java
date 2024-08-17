package com.gzhuoj.judgeserver.util;

import cn.hutool.core.util.StrUtil;
import common.convention.errorcode.BaseErrorCode;
import common.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static common.convention.errorcode.BaseErrorCode.JUDGE_COMPILE_PARAM_ERROR;

public class JudgeUtils {
    /**
     * 将编译指令解析成独立的编译参数
     *
     * @param toProcess 编译指令
     * @return 编译参数集合
     */
    public static List<String> translateCommandline(String toProcess) {
        if (!StrUtil.isEmpty(toProcess)) {
            // 以 "和'和' '为分隔符
            // true表示连同分隔符也作为返回值
            StringTokenizer stringTokenizer = new StringTokenizer(toProcess, "\"' ", true);
            // 参数结果集合
            List<String> result = new ArrayList<>();
            // 单一参数整体
            StringBuilder s = new StringBuilder();
            // 0 在 单引号中
            // 1 在 双引号中
            int flag = -1;
            while (stringTokenizer.hasMoreTokens()) {
                String token = stringTokenizer.nextToken();
                // 是否在引号里
                if(flag != -1){
                    switch (flag){
                        case 0:
                            if("'".equals(token)){
                                result.add(s.toString());
                                s.setLength(0);
                                flag = -1;
                            } else {
                                s.append(token);
                            }
                            break;
                        case 1:
                            if("\"".equals(token)){
                                result.add(s.toString());
                                s.setLength(0);
                                flag = -1;
                            } else {
                                s.append(token);
                            }
                            break;
                    }
                    continue;
                }
                if("'".equals(token)) {
                    flag = 0;
                } else if("\"".equals(token)){
                    flag = 1;
                } else {
                    if(!" ".equals(token)){
                        result.add(token);
                    }
                }
            }

            // 引号没有闭合
            if(s.length() > 0 || flag != -1){
                throw new ServiceException(JUDGE_COMPILE_PARAM_ERROR);
            }
            return result;
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        String toProcess = "/usr/bin/gcc -DONLINE_JUDGE -w -fmax-errors=1 -std=c11 \"src_path -lm -o \"exe_path\"";
        List<String> strings = translateCommandline(toProcess);
        System.out.println(strings);
    }
}
