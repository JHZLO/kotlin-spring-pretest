package com.jhzlo.pretest.report.domain.service

import com.jhzlo.pretest.report.domain.entity.LoginHistory
import com.jhzlo.pretest.report.domain.repository.LoginHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class LoginHistoryService(
    private val loginHistoryRepository: LoginHistoryRepository
) {
    @Transactional(readOnly = true)
    fun countLoginBetween(start: LocalDateTime, end: LocalDateTime): Long {
        return loginHistoryRepository.countByLoginAtBetween(start, end)
    }

    @Transactional
    fun save(userId: Long) {
        loginHistoryRepository.save(LoginHistory(userId = userId))
    }
}
