package com.jhzlo.pretest.chat.controller.dto

data class ChatQueryRequest(
    val question: String,
    val isStreaming: Boolean? = false,
    val model: String? = null
)
