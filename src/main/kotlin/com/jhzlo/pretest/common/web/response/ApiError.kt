package com.jhzlo.pretest.common.web.response

data class ApiError(val element: ApiErrorElement) {
    companion object {
        fun of(element: ApiErrorElement): ApiError = ApiError(element)
    }
}
