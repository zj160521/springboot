package com.core.handle;


import com.core.exception.BizException;
import com.core.exception.SystemException;
import com.core.util.response.BaseResponse;
import com.core.util.response.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandle {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ExceptionHandler(value = Exception.class)
    public BaseResponse<String> handle(Exception e){
		return getExceptionResult(e);
	}


	/**
	 * 校验错误
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public BaseResponse<String> handleMethodArgumentNotValidException (MethodArgumentNotValidException ex) {
		FieldError fieldError = ex.getBindingResult().getFieldError();
    	return BaseResponse.failed(fieldError.getDefaultMessage());
	}


    /**
     * 返回异常结果
     *
     * @param e 异常
     * @return
     */
	private BaseResponse<String> getExceptionResult(Exception e) {
		logger.error("exception:",e);
		if(e instanceof BizException) {
			return BaseResponse.failed(((BizException)e).getCode(), e.getMessage());
		}
		if(e instanceof SystemException) {
			return BaseResponse.failed(((SystemException)e).getCode(), e.getMessage());
		}
		return BaseResponse.failed(ResponseMessage.EXCEPTION.getCode(), e.getMessage(),null);
	}
}
