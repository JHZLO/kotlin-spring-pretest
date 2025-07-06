package com.jhzlo.pretest.user.dto

import com.jhzlo.pretest.user.domain.entity.User

data class UserResponse(
    val email: String,
    val userName: String,
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                email = user.email,
                userName = user.name
            )
        }
    }
}
