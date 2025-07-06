package com.jhzlo.pretest.feedback.controller

import com.jhzlo.pretest.common.annotation.loginUser.LoginUser
import com.jhzlo.pretest.common.web.response.ApiResponse
import com.jhzlo.pretest.feedback.application.FeedbackFacade
import com.jhzlo.pretest.feedback.controller.dto.FeedbackCreateRequest
import com.jhzlo.pretest.feedback.controller.dto.FeedbackResponse
import com.jhzlo.pretest.feedback.controller.dto.FeedbackResponse.Companion.toResponse
import com.jhzlo.pretest.feedback.domain.entity.vo.FeedbackStatus
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/feedback")
class FeedbackController(
    private val feedbackFacade: FeedbackFacade
) {
    @PostMapping
    fun createFeedback(
        @LoginUser userId: String,
        @RequestBody request: FeedbackCreateRequest
    ): ApiResponse<FeedbackResponse> {
        val feedback = feedbackFacade.createFeedback(
            userId,
            request.chatId,
            request.isPositive
        )
        return ApiResponse.success(feedback.toResponse())
    }

    @GetMapping
    fun getFeedbacks(
        @LoginUser userId: String,
        @RequestParam(required = false) isPositive: Boolean?,
        pageable: Pageable
    ): ApiResponse<Page<FeedbackResponse>> {
        val result = feedbackFacade.getFeedbacks(userId, isPositive, pageable)
        return ApiResponse.success(result.map { it.toResponse() })
    }

    @PatchMapping("/{feedbackId}/status")
    fun updateStatus(
        @PathVariable feedbackId: Long,
        @RequestParam status: FeedbackStatus
    ): ApiResponse<Boolean> {
        feedbackFacade.updateFeedbackStatus(feedbackId, status)
        return ApiResponse.success(true)
    }
}
