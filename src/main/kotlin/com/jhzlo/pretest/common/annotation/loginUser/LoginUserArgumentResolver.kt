package com.jhzlo.pretest.common.annotation.loginUser

import com.jhzlo.pretest.common.infrastructure.exception.BadRequestException
import org.springframework.core.MethodParameter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginUserArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(LoginUser::class.java) != null &&
                (parameter.parameterType == java.lang.Long::class.java || parameter.parameterType == Long::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal

        if (principal is Long) {
            return principal
        }

        return if (parameter.parameterType == java.lang.Long::class.java) {
            null
        } else {
            throw BadRequestException("pretest.auth.user-empty")
        }
    }
}
