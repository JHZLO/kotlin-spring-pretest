package com.jhzlo.pretest.feedback.controller.dto

import com.jhzlo.pretest.feedback.domain.entity.Feedback
import com.jhzlo.pretest.feedback.domain.entity.vo.FeedbackStatus
import java.time.LocalDateTime

data class FeedbackResponse(
    val id: Long,
    val userId: String,
    val chatId: Long,
    val isPositive: Boolean,
    val createdAt: LocalDateTime,
    val status: FeedbackStatus
){
    companion object{
        fun Feedback.toResponse() = FeedbackResponse(
            id = this.id,
            userId = this.userId,
            chatId = this.chat.id,
            isPositive = this.isPositive,
            createdAt = this.createdAt,
            status = this.status
        )
    }
}
