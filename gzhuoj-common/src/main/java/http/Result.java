package http;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 请求响应对象实体
 */

@Data
@Builder
public class Result<T> {

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;


//    /**
//     * 请求id
//     */
//    private String requestId;
}
