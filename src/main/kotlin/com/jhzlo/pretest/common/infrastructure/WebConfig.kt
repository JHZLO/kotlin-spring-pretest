package com.jhzlo.pretest.common.infrastructure

import com.jhzlo.pretest.common.annotation.loginUser.LoginUserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig(
    private val loginUserArgumentResolver: LoginUserArgumentResolver
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginUserArgumentResolver)
    }
}
