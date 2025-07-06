package com.jhzlo.pretest.chat.domain.service

import com.jhzlo.pretest.chat.domain.entity.ChatThread
import com.jhzlo.pretest.chat.domain.repository.ChatThreadRepository
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ThreadService(
    private val chatThreadRepository: ChatThreadRepository
) {
    @Transactional
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

    @Transactional(readOnly = true)
    fun getThreadById(threadId: Long): ChatThread {
        return chatThreadRepository.findById(threadId)
            .orElseThrow { BadRequestException("pretest.chat.not-exist-thread") }
    }

    @Transactional
    fun deleteThread(userId: Long, threadId: Long, userRole: UserRole) {
        val thread = getThreadById(threadId)

        if (userRole != UserRole.ADMIN && thread.userId != userId) {
            throw BadRequestException("pretest.chat.invalid-role")
        }
        chatThreadRepository.delete(thread)
    }
}
