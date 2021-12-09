package edu.hzu.englishstudyweb.util;
import java.io.Serializable;

/**
 * @author Jasper Zhan
 * @date 2021年11月26日 22:39
 */
public class Result implements Serializable {

    private static final long serialVersionUID = -3948389268046368059L;

    private Integer code;
    private String msg;
    private Object data;
    private boolean success = false;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setResultCode(ResultCode code) {
        this.code = code.code();
        this.msg = code.message();
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    //成功 不返回数据直接返回成功信息
    public static Result success() {
        Result result = new Result();
        result.setSuccess(true);
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    //成功 并且加上返回数据
    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    //成功 自定义成功返回状态 加上数据
    public static Result success(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    // 单返回失败的状态码
    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    // 返回失败的状态码及数据
    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    public static Result failure(String message) {
        Result result = new Result();
        result.setMsg(message);
        return result;
    }

}

