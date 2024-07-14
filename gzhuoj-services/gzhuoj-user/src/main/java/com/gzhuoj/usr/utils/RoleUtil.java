package com.gzhuoj.usr.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleUtil {

    private static final Map<Integer, String> roleMap = new HashMap<Integer, String>();

    static {
        roleMap.put(0, "普通用户");

        roleMap.put(1, "管理员");

        roleMap.put(2, "超级管理员");
    }

    public static String getRoleName(Integer num) {
        return roleMap.get(num);
    }

}
