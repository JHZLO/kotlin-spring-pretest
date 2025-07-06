package com.jhzlo.pretest.common.infrastructure.jwt

import com.jhzlo.pretest.common.configuration.JwtConfig
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*


@Component
class JwtProvider(
    private val secretKeyFactory: SecretKeyFactory
) {

    fun generateAccessToken(userId: Long, role: UserRole): String {
        val now = Instant.now()
        val expiry = now.plusMillis(JwtConfig.ACCESS_TOKEN_EXPIRATION_TIME)

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .claim("role", role.name)
            .signWith(secretKeyFactory.createSecretKey())
            .compact()
    }

    fun generateRefreshToken(userId: Long): String {
        val now = Instant.now()
        val expiry = now.plusMillis(JwtConfig.REFRESH_TOKEN_EXPIRATION_TIME)

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .claim("type", "refresh")
            .signWith(secretKeyFactory.createSecretKey())
            .compact()
    }
}
