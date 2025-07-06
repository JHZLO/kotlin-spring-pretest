package com.jhzlo.pretest.user.controller

import com.jhzlo.pretest.common.annotation.loginUser.LoginUser
import com.jhzlo.pretest.common.web.response.ApiResponse
import com.jhzlo.pretest.user.application.UserFacade
import com.jhzlo.pretest.user.dto.UserResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController (
    private val userFacade: UserFacade
){
    @GetMapping("/info")
    fun getMyInfo(@LoginUser userId: Long): ApiResponse<UserResponse> {
        val response: UserResponse = userFacade.getUserInfo(userId)
        return ApiResponse.success(response)
    }
}
