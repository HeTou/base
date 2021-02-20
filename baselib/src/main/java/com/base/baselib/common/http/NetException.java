package com.base.baselib.common.http;

/**
 * @author Administrator
 * @date 2020/8/29 0029
 */
public class NetException extends Exception {
    public NetException() {
        super();
    }

    public NetException(String message) {
        super(message);
    }

    public NetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetException(Throwable cause) {
        super(cause);
    }
}
