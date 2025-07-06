package com.jhzlo.pretest.common.infrastructure.exception

import com.jhzlo.pretest.common.web.response.ErrorCodeResolvingApiErrorException
import com.jhzlo.pretest.common.web.response.ExtendedHttpStatus

class BadRequestException(code: String = "bad-request", cause: Throwable? = null) :
    ErrorCodeResolvingApiErrorException(ExtendedHttpStatus.BAD_REQUEST, code, cause)
