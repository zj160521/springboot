package com.core.util.response;

import java.io.Serializable;

/**
 * 项目名:
 * 项目描述: 返回消息伪枚举 - 使用时可以直接继承扩展
 * @author : Wu Ren Jun
 * @date: 2018年9月13日
 */
public class ResponseMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 操作成功
     */
    public static final ResponseMessage SUCCESS = new ResponseMessage(2000, "操作成功");
    /**
     * 操作失败
     */
    public static final ResponseMessage FAILURE = new ResponseMessage(4020, "操作失败");

    /**
     * 操作失败
     */
    public static final ResponseMessage EXCEPTION = new ResponseMessage(5000, "操作异常");

    /**
     * 服务器无法理解此请求
     */
    public static final ResponseMessage F4000 = new ResponseMessage(4000, "服务器无法理解此请求");
    /**
     * [服务器]运行时异常
     */
    public static final ResponseMessage F4001 = new ResponseMessage(4001, "[服务器]运行时异常");
    /**
     * [服务器]空值异常
     */
    public static final ResponseMessage F4002 = new ResponseMessage(4002, "[服务器]空值异常");
    /**
     * [服务器]数据类型转换异常
     */
    public static final ResponseMessage F4003 = new ResponseMessage(4003, "[服务器]数据类型转换异常");
    /**
     * [服务器]IO异常
     */
    public static final ResponseMessage F4004 = new ResponseMessage(4004, "[服务器]IO异常");
    /**
     * [服务器]未知方法异常
     */
    public static final ResponseMessage F4005 = new ResponseMessage(4005, "[服务器]未知方法异常");
    /**
     * [服务器]数组越界异常
     */
    public static final ResponseMessage F4006 = new ResponseMessage(4006, "[服务器]数组越界异常");
    /**
     * [服务器]网络异常
     */
    public static final ResponseMessage F4007 = new ResponseMessage(4007, "[服务器]网络异常");
    /**
     * 访问超时，请重新登陆
     */
    public static final ResponseMessage F4010 = new ResponseMessage(401, "访问超时，请重新登陆");
    /**
     * 不支持的媒体类型
     */
    public static final ResponseMessage F4150 = new ResponseMessage(4150, "不支持的媒体类型");
    /**
     * 没有接口访问权限
     */
    public static final ResponseMessage F4030 = new ResponseMessage(403, "没有接口访问权限");
    /**
     * 没有找到请求的接口资源
     */
    public static final ResponseMessage F4040 = new ResponseMessage(4040, "没有找到请求的接口资源");
    /**
     * 不支持的请求方法 GET,POST
     */
    public static final ResponseMessage F4050 = new ResponseMessage(4050, "不支持的请求方法");
    /**
     * 客户端不接受所请求的 MIME类型
     */
    public static final ResponseMessage F4060 = new ResponseMessage(4060, "客户端不接受所请求的MIME类型");
    /**
     * 图片上传失败，请检查网络设置
     */
    public static final ResponseMessage F4070 = new ResponseMessage(4070, "图片上传失败，请检查网络设置");
    /**
     * 参数不能为空
     */
    public static final ResponseMessage F4080 = new ResponseMessage(4080, "参数不能为空，%s");
    /**
     * 服务器内部异常
     */
    public static final ResponseMessage F5000 = new ResponseMessage(5000, "服务器内部异常");

    private int code;

    private String message;

    public ResponseMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
