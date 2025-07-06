package com.jhzlo.pretest.user.domain.repository

import com.jhzlo.pretest.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun countByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): Long
}
