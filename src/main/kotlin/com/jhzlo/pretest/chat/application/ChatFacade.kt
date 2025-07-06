package com.jhzlo.pretest.chat.application

import com.jhzlo.pretest.chat.domain.entity.Chat
import com.jhzlo.pretest.chat.domain.service.ChatService
import com.jhzlo.pretest.chat.domain.service.ThreadService
import com.jhzlo.pretest.chat.infrastructure.OpenAiService
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage


@Component
class ChatFacade(
    private val threadService: ThreadService,
    private val chatService: ChatService,
    private val openAiService: OpenAiService,
) {
    @Transactional
    fun handleChat(
        userId: Long,
        question: String,
        isStreaming: Boolean?,
        model: String?
    ): Chat {
        val thread = threadService.findOrCreateThread(userId)
        val previousChats = chatService.getThreadHistory(thread.id)

        val messages = mutableListOf<Message>().apply {
            add(SystemMessage("You are a helpful AI assistant."))
            previousChats.forEach {
                add(UserMessage(it.question))
                add(AssistantMessage(it.answer))
            }
            add(UserMessage(question))
        }

        val answer = openAiService.createAnswer(
            messages,
            model ?: "gpt-3.5-turbo",
            isStreaming ?: false
        ) ?: throw IllegalStateException("AI 응답 실패")

        return chatService.saveChat(thread, question, answer)
    }

    fun getThreadChats(userId: Long, threadId: Long, pageable: Pageable, userRole: UserRole): Page<Chat> {
        val thread = threadService.getThreadById(threadId)
        return chatService.getThreadChats(userId, thread, pageable, userRole)
    }

    fun deleteThread(userId: Long, threadId: Long, userRole: UserRole) {
        threadService.deleteThread(userId, threadId, userRole)
    }
}
