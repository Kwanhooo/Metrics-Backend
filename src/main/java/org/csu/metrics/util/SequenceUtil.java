package org.csu.metrics.util;

public class SequenceUtil {
    public static synchronized Long gen() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return System.currentTimeMillis();
    }
}
