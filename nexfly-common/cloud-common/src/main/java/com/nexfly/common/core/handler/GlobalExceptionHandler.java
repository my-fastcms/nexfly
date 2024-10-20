package com.nexfly.common.core.handler;

import com.nexfly.common.core.exception.NexflyException;
import com.nexfly.common.core.rest.RestResult;
import com.nexfly.common.core.rest.RestResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NexflyException.class)
    public RestResult<Object> handlerNexflyException(NexflyException e) {
        return RestResultUtils.failed(e.getErrMsg());
    }

    @ExceptionHandler(RuntimeException.class)
    public RestResult<Object> handlerRuntimeException(RuntimeException e) {
        log.error(e.getMessage());
        return RestResultUtils.failed("Server Exception");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public RestResult<Object> handlerNoResourceFoundException(NoResourceFoundException e) {
        log.error("not found resource [" + e.getResourcePath() + "]");
        return RestResultUtils.failed("not found resource [" + e.getResourcePath() + "]");
    }

}
