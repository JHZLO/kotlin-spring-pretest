package com.jhzlo.pretest.common.web.response

import com.jhzlo.pretest.common.env.AppEnv
import com.jhzlo.pretest.common.support.SpringContextHolder

open class ErrorCodeResolvingApiErrorException : ApiErrorException {

    private companion object {
        val appEnv: AppEnv by lazy {
            SpringContextHolder.getBean(AppEnv::class.java)
        }
    }

    constructor(statusCode: ExtendedHttpStatus, code: String) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code)
                    )
                )
            )

    constructor(statusCode: ExtendedHttpStatus, code: String, cause: Throwable?) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code)
                    )
                ), cause
            )

    constructor(statusCode: ExtendedHttpStatus, code: String, args: Array<out Any>?) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code, args = args)
                    )
                )
            )

    constructor(statusCode: ExtendedHttpStatus, code: String, args: Array<out Any>?, cause: Throwable?) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code, args = args)
                    )
                ), cause
            )

    constructor(statusCode: ExtendedHttpStatus, code: String, args: Array<out Any>?, cause: Throwable?, data: Any?) :
            super(
                ApiError.of(
                    ApiErrorElement(
                        appEnv.getId(), statusCode, ApiErrorCode.of(code), ApiErrorMessage.of(code, args = args), data
                    )
                ), cause
            )
}
