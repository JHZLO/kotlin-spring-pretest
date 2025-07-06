package com.jhzlo.pretest.user.domain.service

import com.jhzlo.pretest.user.domain.entity.User
import com.jhzlo.pretest.user.domain.repository.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun save(user: User): User {
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun existByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    @Transactional(readOnly = true)
    fun getByEmail(email: String): User {
        return findByEmail(email) ?: throw BadRequestException("pretest.user.not-found")
    }

    @Transactional(readOnly = true)
    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    @Transactional(readOnly = true)
    fun getById(id: Long): User {
        return findById(id) ?: throw BadRequestException("pretest.user.not-found")
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }
}
