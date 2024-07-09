package constant;

/**
 * 定义http响应码
 * 一边编码完善一边定义
 */
public enum HttpResponseCode {
    // =================== 成功码 S =====================
    SUCCESS("S1000" , "请求成功"),

    //==================== 一级失败码 E =========
    CLIENT_ERROR("E1000" , "客户端错误"),
    SERVER_ERROR("E1001" , "系统执行错误"),
    REMOTE_ERROR("E1002" , "调用第三方服务出错");


    //==================== 二级错误码 业务逻辑错误 ========
    // 1. 用户模块 U

    // 2. 题库模块 P

    // 3. 评测模块 J

    // 4. 竞赛模块 C

    private final String code;
    private final String message;

    HttpResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {return code;}
    public String message() {return message;}
}
