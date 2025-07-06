package com.jhzlo.pretest.auth.application

import com.jhzlo.pretest.auth.dto.LoginRequest
import com.jhzlo.pretest.auth.dto.SignUpRequest
import com.jhzlo.pretest.auth.service.AuthService
import com.jhzlo.pretest.common.infrastructure.exception.BadRequestException
import com.jhzlo.pretest.common.infrastructure.jwt.JwtProvider
import com.jhzlo.pretest.user.domain.entity.User
import com.jhzlo.pretest.user.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthFacade(
    private val userService: UserService,
    private val jwtProvider: JwtProvider,
    private val authService: AuthService,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUp(request: SignUpRequest) {
        if (userService.existByEmail(request.email)) {
            throw BadRequestException("pretest.auth.email-exists")
        }
        val encodedPassword = passwordEncoder.encode(request.password)
        val user = userService.save(User.of(request, encodedPassword))
    }

    @Transactional
    fun login(request: LoginRequest, response: HttpServletResponse) {
        val user = userService.getByEmail(request.email)

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadRequestException("pretest.auth.invalid-password")
        }

        val accessToken = jwtProvider.generateAccessToken(user.id, user.userRole)
        val refreshToken = jwtProvider.generateRefreshToken(user.id)

        authService.login(accessToken, refreshToken, response)
    }

    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        authService.logout(response)
    }
}
