package com.jhzlo.pretest.chat.domain.service

import com.jhzlo.pretest.chat.domain.entity.Chat
import com.jhzlo.pretest.chat.domain.entity.ChatThread
import com.jhzlo.pretest.chat.domain.repository.ChatRepository
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import org.apache.coyote.BadRequestException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional


@Service
class ChatService(
    private val chatRepository: ChatRepository
) {
    @Transactional
    fun saveChat(chatThread: ChatThread, question: String, answer: String): Chat {
        return chatRepository.save(
            Chat(
                chatThread = chatThread,
                question = question,
                answer = answer
            )
        )
    }

    @Transactional(readOnly = true)
    fun getChatById(chatId: Long): Chat {
        return chatRepository.findById(chatId)
            .orElseThrow { BadRequestException("pretest.chat.not-found") }
    }

    @Transactional(readOnly = true)
    fun getThreadChats(userId: Long, chatThread: ChatThread, pageable: Pageable, userRole: UserRole): Page<Chat> {
        if (userRole != UserRole.ADMIN && chatThread.userId != userId) {
            throw BadRequestException("pretest.chat.not-found")
        }
        return chatRepository.findByChatThreadId(chatThread.id, pageable)
    }

    @Transactional(readOnly = true)
    fun getThreadHistory(threadId: Long): List<Chat> {
        return chatRepository.findByChatThreadId(threadId, Sort.by(Sort.Direction.ASC, "createdAt"))
    }
}
