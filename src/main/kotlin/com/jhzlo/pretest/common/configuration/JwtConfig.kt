package com.jhzlo.pretest.common.configuration

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets
import java.util.*

@Configuration
class JwtConfig {

    @Value("\${jwt.secret}")
    lateinit var jwtSecret: String

    companion object {
        const val ACCESS_TOKEN_EXPIRATION_TIME: Long = 1000 * 60 * 60L // 1시간
        const val REFRESH_TOKEN_EXPIRATION_TIME: Long = 1000 * 60 * 60 * 24 * 7L // 1주
    }

    @PostConstruct
    protected fun init() {
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.toByteArray(StandardCharsets.UTF_8))
    }
}
