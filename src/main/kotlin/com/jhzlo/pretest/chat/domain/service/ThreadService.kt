package com.jhzlo.pretest.chat.domain.service

import com.jhzlo.pretest.chat.domain.repository.ChatThreadRepository
import com.jhzlo.pretest.chat.domain.entity.ChatThread
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ThreadService(
    private val chatThreadRepository: ChatThreadRepository
) {
    fun findOrCreateThread(userId: Long): ChatThread {
        val now = LocalDateTime.now()
        val threads = chatThreadRepository.findByUserId(userId)
        val latest = threads.maxByOrNull { it.updatedAt ?: LocalDateTime.MIN }

        return if (latest == null || latest.updatedAt?.plusMinutes(30)?.isBefore(now) != false) {
            chatThreadRepository.save(ChatThread(userId = userId))
        } else {
            latest
        }
    }

    fun getThreadById(threadId: Long): ChatThread {
        return chatThreadRepository.findById(threadId)
            .orElseThrow { IllegalArgumentException("스레드가 존재하지 않습니다.") }
    }

    fun deleteThread(userId: Long, threadId: Long, userRole: UserRole) {
        val thread = getThreadById(threadId)

        if (userRole != UserRole.ADMIN && thread.userId != userId) {
            throw IllegalAccessException("권한이 없습니다.")
        }
        chatThreadRepository.delete(thread)
    }
}
