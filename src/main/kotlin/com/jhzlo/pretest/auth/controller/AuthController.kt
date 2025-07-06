package com.jhzlo.pretest.auth.controller

import com.jhzlo.pretest.auth.dto.LoginRequest
import com.jhzlo.pretest.auth.dto.SignUpRequest
import com.jhzlo.pretest.common.web.response.ApiResponse
import com.jhzlo.pretest.auth.application.AuthFacade
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authFacade: AuthFacade
) {

    @PostMapping("/register")
    fun signup(@RequestBody request: SignUpRequest): ApiResponse<Boolean> {
        authFacade.signUp(request)
        return ApiResponse.success(true)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest, response: HttpServletResponse): ApiResponse<Boolean> {
        authFacade.login(request, response)
        return ApiResponse.success(true)
    }

    @PostMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse): ApiResponse<Boolean> {
        authFacade.logout(request, response)
        return ApiResponse.success(true)
    }
}
