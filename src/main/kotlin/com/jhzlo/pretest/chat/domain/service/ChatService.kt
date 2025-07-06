package com.jhzlo.pretest.chat.domain.service

import com.jhzlo.pretest.chat.domain.entity.Chat
import com.jhzlo.pretest.chat.domain.entity.ChatThread
import com.jhzlo.pretest.chat.domain.repository.ChatRepository
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.data.domain.Pageable


@Service
class ChatService(
    private val chatRepository: ChatRepository
) {
    fun saveChat(chatThread: ChatThread, question: String, answer: String): Chat {
        return chatRepository.save(
            Chat(
                chatThread = chatThread,
                question = question,
                answer = answer
            )
        )
    }

    fun getThreadChats(userId: Long, chatThread: ChatThread, pageable: Pageable, userRole: UserRole): Page<Chat> {
        if (userRole != UserRole.ADMIN && chatThread.userId != userId) {
            throw IllegalAccessException("권한이 없습니다.")
        }
        return chatRepository.findByChatThreadId(chatThread.id, pageable)
    }

    fun getThreadHistory(threadId: Long): List<Chat> {
        return chatRepository.findByChatThreadId(threadId, Sort.by(Sort.Direction.ASC, "createdAt"))
    }
}
