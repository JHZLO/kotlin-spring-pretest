package com.jhzlo.pretest.common.web.response

import com.jhzlo.pretest.common.web.response.ApiError

open class ApiErrorException(
    val error: ApiError,
    cause: Throwable? = null,
) : RuntimeException(cause)
