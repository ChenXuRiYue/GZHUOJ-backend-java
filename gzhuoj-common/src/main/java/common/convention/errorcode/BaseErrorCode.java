package common.convention.errorcode;

import common.convention.errorcode.IErrorCode;
import common.exception.AbstractException;

/**
 * 基础错误码定义
 */
public enum BaseErrorCode implements IErrorCode {

    // ========== 一级宏观错误码 客户端错误 ==========
    CLIENT_ERROR("A000001", "用户端错误"),

    // ========== 二级宏观错误码 用户操作错误 ==========】
    ADMIN_UPLOAD_ILLEGAL_FILE_ERROR("A000100", "上传测试数据文件部分失败，包含不合法的文件"),
    ADMIN_UPLOAD_ILLEGAL_PROBLEM_DESCRIPTION_ERROR("A000101", "上传题目描述附件部分失败，包含不合法的文件"),
    USER_ACCOUNT_VERIFY_ERROR("A000110", "用户账号校验失败"),
    USER_PASSWORD_VERIFY_ERROR("A000111", "用户密码校验失败"),
    TEAM_LOGIN_ACCOUNT_ERROR("A000201", "队伍不存在"),
    TEAM_LOGIN_PASSWORD_ERROR("A000202", "密码错误"),
    TEAM_DELETE_ERROR("A000203", "删除队伍失败"),
    TEAM_UPDATE_NOT_FOUND_ERROR("A000204", "队伍不存在"),
    TEAM_INFO_NOT_FOUND_ERROR("A000205", "队伍不存在"),
    TEAM_UPDATE_LOW_PASSWORD_ERROR("A000206", "密码长度至少为六位"),
    CONTEST_HAVE_BEGIN("A000207","比赛已经开始了"),
    PROBLEM_ID_EXISTED("A000301", "新设题目编号已存在"),



    // ========== 二级宏观错误码 系统请求缺少幂等Token ==========
    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "幂等Token为空"),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "幂等Token已被使用或失效"),

    // ========== 一级宏观错误码 系统执行出错 ==========
    SERVICE_ERROR("B000001", "系统执行出错"),
    // ========== 二级宏观错误码 系统执行超时 ==========
    SERVICE_TIMEOUT_ERROR("B000100", "系统执行超时"),

    // ========== 一级宏观错误码 调用第三方服务出错 ==========
    REMOTE_ERROR("C000001", "调用第三方服务出错");

    private final String code;
    private final String message;

    BaseErrorCode(String code, String message){
        this.code = code;
        this.message = message;
    }
    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}