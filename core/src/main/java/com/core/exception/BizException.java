package com.core.exception;

/**
 * 自定义业务异常
 */
public class BizException extends SystemException {

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }
    public BizException(Exception e) {
        super(e);
    }
    public BizException(String exceptionInfo,int code) {
        super(exceptionInfo, code);
    }
    public BizException(Exception e,int code) {
        super(e, code);
    }
}
