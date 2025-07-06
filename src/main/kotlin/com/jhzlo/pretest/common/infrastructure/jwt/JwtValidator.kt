package com.jhzlo.pretest.common.infrastructure.jwt

import com.jhzlo.pretest.common.infrastructure.exception.BadRequestException
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class JwtValidator(
    private val secretKeyFactory: SecretKeyFactory
) {

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
                .parseSignedClaims(token)
            true
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun validateRefreshToken(token: String): Long? {
        return try {
            val claims = Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
                .parseSignedClaims(token).payload

            if (claims["type"] != "refresh") {
                null
            } else {
                claims.subject.toLong()
            }
        } catch (e: Exception) {
            null
        }
    }


    fun getSubject(token: String): String {
        return try {
            Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
                .parseSignedClaims(token).payload.subject
        } catch (e: Exception) {
            throw BadRequestException("pretest.auth.invalid-token")
        }
    }

    fun getRole(token: String): UserRole {
        return try {
            val claims: Claims = Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
                .parseSignedClaims(token).payload
            val roleRaw = claims["role", String::class.java]
            UserRole.valueOf(roleRaw)
        } catch (e: Exception) {
            throw BadRequestException("pretest.auth.invalid-token")
        }
    }

    fun getExpiration(token: String): Instant {
        return Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
            .parseSignedClaims(token).payload.expiration.toInstant()
    }
}
