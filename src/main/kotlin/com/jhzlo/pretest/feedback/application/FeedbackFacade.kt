package com.jhzlo.pretest.feedback.application

import com.jhzlo.pretest.chat.domain.service.ChatService
import com.jhzlo.pretest.feedback.domain.entity.Feedback
import com.jhzlo.pretest.feedback.domain.entity.vo.FeedbackStatus
import com.jhzlo.pretest.feedback.domain.service.FeedbackService
import com.jhzlo.pretest.user.domain.service.UserService
import org.apache.coyote.BadRequestException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class FeedbackFacade(
    private val feedbackService: FeedbackService,
    private val chatService: ChatService,
    private val userService: UserService
) {
    @Transactional
    fun createFeedback(userId: String, chatId: Long, isPositive: Boolean): Feedback {
        val chat = chatService.getChatById(chatId)
        if (chat.chatThread.userId.toString() != userId) {
            throw BadRequestException("pretest.feedback.forbidden")
        }
        return feedbackService.createFeedback(userId, chat, isPositive)
    }

    fun getFeedbacks(
        userId: String,
        isPositive: Boolean?,
        pageable: Pageable
    ): Page<Feedback> {
        val userRole = userService.getRoleById(userId.toLong())
        return feedbackService.getFeedbackList(userId, userRole, isPositive, pageable)
    }

    @Transactional
    fun updateFeedbackStatus(feedbackId: Long, status: FeedbackStatus) {
        feedbackService.updateStatus(feedbackId, status)
    }
}
