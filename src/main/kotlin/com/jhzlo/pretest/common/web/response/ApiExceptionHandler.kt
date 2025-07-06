package com.jhzlo.pretest.common.web.response

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(ApiErrorException::class)
    fun handleApiErrorException(ex: ApiErrorException): ResponseEntity<ApiResponse<Nothing>> {
        val status = ex.error.element.status
        return ResponseEntity.status(status.code).body(ApiResponse.error(ex.error))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnknownException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        val error = ApiError.of(
            ApiErrorElement(
                appId = "app-id",
                status = ExtendedHttpStatus.INTERNAL_SERVER_ERROR,
                code = ApiErrorCode.of("internal.error"),
                message = ApiErrorMessage.of("internal.error")
            )
        )
        return ResponseEntity.status(500).body(ApiResponse.error(error))
    }
}
