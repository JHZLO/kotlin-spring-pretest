package com.jhzlo.pretest.report.domain.repository

import com.jhzlo.pretest.report.domain.entity.LoginHistory
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface LoginHistoryRepository : JpaRepository<LoginHistory, Long> {
    fun countByLoginAtBetween(start: LocalDateTime, end: LocalDateTime): Long
}
