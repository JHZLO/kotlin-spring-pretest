package com.jhzlo.pretest.user.application

import com.jhzlo.pretest.user.domain.entity.User
import com.jhzlo.pretest.user.domain.service.UserService
import com.jhzlo.pretest.user.dto.UserResponse
import org.springframework.stereotype.Component

@Component
class UserFacade (
    private val userService: UserService
){
    fun getUserInfo(userId: Long): UserResponse {
        val user: User = userService.getById(userId)
        return UserResponse.from(user)
    }
}
