package com.woo.outstagram.util.exception;

/**
 * RuntimeException을 상속받는 CustomException
 */
public class BizException extends RuntimeException {
    public BizException(String msg){
        super(msg);
    }
    public BizException(Exception ex){
        super(ex);
    }
}