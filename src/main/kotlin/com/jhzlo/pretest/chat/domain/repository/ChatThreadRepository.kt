package com.jhzlo.pretest.chat.domain.repository

import com.jhzlo.pretest.chat.domain.entity.ChatThread
import org.springframework.data.jpa.repository.JpaRepository

interface ChatThreadRepository : JpaRepository<ChatThread, Long> {
    fun findByUserId(userId: Long): List<ChatThread>
}
