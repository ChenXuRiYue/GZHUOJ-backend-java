package common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum SubmissionLanguage {
    C(0, "C"),
    CPlusPlus(1, "C++ 17 With O2"),
    Java(2, "Java"),
    Python(3, "Python"),
    Go(4,"Go");
    /**
     * 提交语言编号
     */
    private final Integer code;

    /**
     * 语言字符串
     */
    private final String lang;

    // 创建一个静态的 Map 用于缓存 code 到 lang 的映射
    private static final Map<Integer, String> codeToLangMap = new HashMap<>();

    // 静态初始化块来填充 Map
    static {
        for (SubmissionLanguage language : SubmissionLanguage.values()) {
            codeToLangMap.put(language.getCode(), language.getLang());
        }
    }

    /**
     * 根据语言编号获取语言字符串
     */
    public static String getLangById(Integer languageId) {
        return codeToLangMap.get(languageId);
    }

    public static void main(String[] args) {
        String s = "#include<bits/stdc++.h>\n" +
                "using namespace std;\n" +
                "vector<vector<pair<int, int>>> Char = {\n" +
                "    {\n" +
                "        {0, 0}, {0, 1}, {0, 2},\n" +
                "        {1, 0},         {1, 2},\n" +
                "        {2, 0}, {2, 1}, {2, 2},\n" +
                "        {3, 0},\n" +
                "        {4, 0}\n" +
                "    },\n" +
                "    {\n" +
                "        {0, 0}, {0, 1}, {0, 2},\n" +
                "                        {1, 2},\n" +
                "                {2, 1}, {2, 2},\n" +
                "                        {3, 2},\n" +
                "                        {4, 2}\n" +
                "    },\n" +
                "    {\n" +
                "        {0, 0}, {0, 1}, {0, 2},\n" +
                "        {1, 0},         {1, 2},\n" +
                "        {2, 0}, {2, 1}, {2, 2},\n" +
                "        {3, 0},         {3, 2},\n" +
                "        {4, 0}, {4, 1}, {4, 2}\n" +
                "    }\n" +
                "};\n" +
                "int main() \n" +
                "{\n" +
                "    ios::sync_with_stdio(false);\n" +
                "    cin.tie(nullptr);\n" +
                "    int n, m;\n" +
                "    cin >> n >> m;\n" +
                "    vector<string> s(n);\n" +
                "    for(int i = 0; i < n; i++ ){\n" +
                "    \tcin >> s[i];\n" +
                "    }\n" +
                "\n" +
                "    vector<int> ans(3);\n" +
                "    auto print = [&](int i, int j, int id){\n" +
                "        for(auto [dx, dy] : Char[id]){\n" +
                "            int x = i + dx, y = j + dy;\n" +
                "            s[x][y] = '.';\n" +
                "        }\n" +
                "        ans[id] += 1;\n" +
                "    };\n" +
                "    for(int i = 0; i < m; i++ ){\n" +
                "        for(int j = 0; j < n; j++ ){\n" +
                "            if(s[j][i] != '#'){\n" +
                "                continue;\n" +
                "            }\n" +
                "            // 先判断字符1\n" +
                "            if(s[j + 1][i] != '#'){\n" +
                "                print(j, i, 1);\n" +
                "                continue;\n" +
                "            }\n" +
                "            // 再判断字符3\n" +
                "            bool ok = true;\n" +
                "            for(auto [dx, dy] : Char[2]){\n" +
                "                int x = j + dx, y = i + dy;\n" +
                "                if(s[x][y] != '#'){\n" +
                "                    ok = false;\n" +
                "                }\n" +
                "            }\n" +
                "            print(j, i, (ok ? 2 : 0));\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    for(int i = 0; i < 3; i++ ){\n" +
                "        cout << ans[i] << \" \\n\"[i == 2];\n" +
                "    }\n" +
                "    return 0;\n" +
                "}";
        System.out.println(s);
    }
}
