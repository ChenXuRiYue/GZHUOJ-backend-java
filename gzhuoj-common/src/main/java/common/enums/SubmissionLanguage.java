package common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum SubmissionLanguage {
    C(0, "C"),
    CPlusPlus(1, "C++"),
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
}
