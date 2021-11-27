package edu.hzu.englishstudyweb.util;

/**
 * API 统一返回状态码
 */
public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(1, "成功"),
    /* 失败状态码 */
    FAILURE(2, "失败"),
    /* 查询失败（结果为空）状态码 */
    FAILURE_OF_QUERY_NULL(201, "失败：查询结果为空");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public static String getMessage(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.name();
    }

}