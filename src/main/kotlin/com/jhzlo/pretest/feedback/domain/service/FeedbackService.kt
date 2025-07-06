package com.jhzlo.pretest.feedback.domain.service

import com.jhzlo.pretest.chat.domain.entity.Chat
import com.jhzlo.pretest.common.infrastructure.exception.BadRequestException
import com.jhzlo.pretest.feedback.domain.entity.Feedback
import com.jhzlo.pretest.feedback.domain.entity.vo.FeedbackStatus
import com.jhzlo.pretest.feedback.domain.repository.FeedbackRepository
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class FeedbackService(
    private val feedbackRepository: FeedbackRepository
) {
    @Transactional
    fun createFeedback(userId: String, chat: Chat, isPositive: Boolean): Feedback {
        // 같은 유저가 같은 대화에 이미 피드백했는지 확인
        val exist = feedbackRepository.findAll()
            .any { it.userId == userId && it.chat.id == chat.id }
        if (exist) {
            throw BadRequestException("pretest.feedback.duplicate")
        }

        return feedbackRepository.save(
            Feedback(
                userId = userId,
                chat = chat,
                isPositive = isPositive
            )
        )
    }

    @Transactional(readOnly = true)
    fun getFeedbackList(
        userId: String?,
        userRole: UserRole,
        isPositive: Boolean?,
        pageable: Pageable
    ): Page<Feedback> {
        return if (userRole == UserRole.ADMIN) {
            if (isPositive != null) {
                feedbackRepository.findByIsPositive(isPositive, pageable)
            } else {
                feedbackRepository.findAll(pageable)
            }
        } else {
            if (isPositive != null) {
                feedbackRepository.findAll()
                    .filter { it.userId == userId && it.isPositive == isPositive }
                    .let { PageImpl(it, pageable, it.size.toLong()) }
            } else {
                feedbackRepository.findByUserId(userId!!, pageable)
            }
        }
    }

    @Transactional
    fun updateStatus(feedbackId: Long, status: FeedbackStatus) {
        val feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow { BadRequestException("pretest.feedback.not-found") }
        feedback.status = status
        feedbackRepository.save(feedback)
    }
}
