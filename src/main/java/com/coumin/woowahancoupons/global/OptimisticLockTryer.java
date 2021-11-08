package com.coumin.woowahancoupons.global;

import com.coumin.woowahancoupons.global.exception.RequestRetryFailedException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OptimisticLockTryer {

    public void attempt(Runnable runnable, int tryCount) {
        for (int i = 0; i < tryCount; i++) {
            try {
                runnable.run();
            } catch (ObjectOptimisticLockingFailureException e) {
                log.error("error occurred when try {}", i, e);
                if (i == tryCount - 1) {
                    throw new RequestRetryFailedException();
                }
                continue;
            }
            break;
        }
    }
}
