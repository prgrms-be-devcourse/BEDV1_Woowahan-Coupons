package com.coumin.woowahancoupons.global.exception;

public class StoreNotFoundException extends EntityNotFoundException {

    public StoreNotFoundException(String target) {
        super(target + " is not found");
    }
}