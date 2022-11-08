package site.metacoding.humancloud.handler;

import site.metacoding.humancloud.dto.ResponseDto;

//@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(RuntimeException.class)
    public ResponseDto<?> apiError(Exception e) {
        return new ResponseDto<>(-1, e.getMessage(), null);
    }
}
