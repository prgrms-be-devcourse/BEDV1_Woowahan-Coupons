package com.coumin.woowahancoupons.global.exception;

import com.coumin.woowahancoupons.global.error.ErrorCode;

public class RequestRetryFailedException extends BusinessException{

    public RequestRetryFailedException() {
        super(ErrorCode.REQUEST_RETRY_FAILED);
    }
}
