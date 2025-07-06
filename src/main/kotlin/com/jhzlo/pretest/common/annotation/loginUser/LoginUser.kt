package com.jhzlo.pretest.common.annotation.loginUser

import io.swagger.v3.oas.annotations.Parameter

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Parameter(hidden = true)
@MustBeDocumented
annotation class LoginUser
