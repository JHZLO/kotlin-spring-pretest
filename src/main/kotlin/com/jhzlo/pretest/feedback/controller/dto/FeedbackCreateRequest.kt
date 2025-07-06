package com.jhzlo.pretest.feedback.controller.dto

data class FeedbackCreateRequest(
    val chatId: Long,
    val isPositive: Boolean
)
