package com.wsi.proshipperformance.exception;

public class ProshipPerformanceTestException extends Exception {

    public ProshipPerformanceTestException(String msg, Exception ex)
    {
        super(msg, ex);
    }

    public ProshipPerformanceTestException(String msg)
    {
        super(msg);
    }

}
