package com.jhzlo.pretest.auth.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service

@Service
class AuthService {
    companion object {
        private const val AT_PREFIX = "access_token"
        private const val RT_PREFIX = "refresh_token"
    }

    fun login(at: String, rt: String, response: HttpServletResponse) {

        response.addHeader(
            "Set-Cookie",
            "access_token=${at}; Path=/; HttpOnly; SameSite=Lax; Max-Age=1800"
        )
        response.addHeader(
            "Set-Cookie",
            "refresh_token=${rt}; Path=/; HttpOnly; SameSite=Lax; Max-Age=604800"
        )
    }

    fun logout(response: HttpServletResponse) {

        val expiredAccess = Cookie(AT_PREFIX, "").apply {
            path = "/"
            isHttpOnly = true
            maxAge = 0
        }

        val expiredRefresh = Cookie(RT_PREFIX, "").apply {
            path = "/"
            isHttpOnly = true
            maxAge = 0
        }

        response.addCookie(expiredAccess)
        response.addCookie(expiredRefresh)
    }
}
