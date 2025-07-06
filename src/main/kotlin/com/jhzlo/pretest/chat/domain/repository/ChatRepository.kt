package com.jhzlo.pretest.chat.domain.repository

import com.jhzlo.pretest.chat.domain.entity.Chat
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRepository : JpaRepository<Chat, Long> {
    fun findByChatThreadId(threadId: Long, sort: Sort): List<Chat>
    fun findByChatThreadId(threadId: Long, pageable: Pageable): Page<Chat>
}
