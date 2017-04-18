package com.reedsec.exception;

/**
 * Reedsec自定义异常
 * Created by lik@reedsec.com on 2017/4/6 0006.
 */
public class RSException extends Exception {

    private static final long serialVersionUID = 1L;

    public RSException(String msg) {
        super(msg);
    }

    public RSException(String message, Throwable e) {
        super(message, e);
    }

}
