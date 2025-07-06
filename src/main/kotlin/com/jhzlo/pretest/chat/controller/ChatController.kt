package com.jhzlo.pretest.chat.controller

import com.jhzlo.pretest.chat.application.ChatFacade
import com.jhzlo.pretest.chat.controller.dto.ChatQueryRequest
import com.jhzlo.pretest.chat.controller.dto.ChatQueryResponse
import com.jhzlo.pretest.chat.domain.entity.Chat
import com.jhzlo.pretest.common.annotation.loginUser.LoginUser
import com.jhzlo.pretest.common.web.response.ApiResponse
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/chat")
class ChatController(
    private val chatFacade: ChatFacade
) {

    @PostMapping("/query")
    fun queryChat(
        @LoginUser userId: Long,
        @RequestBody request: ChatQueryRequest
    ): ApiResponse<ChatQueryResponse> {
        val chat = chatFacade.handleChat(
            userId = userId,
            question = request.question,
            isStreaming = request.isStreaming,
            model = request.model
        )
        return ApiResponse.success(
            ChatQueryResponse(
                answer = chat.answer,
                threadId = chat.chatThread.id
            )
        )
    }

    @GetMapping("/threads/{threadId}")
    fun getThreadChats(
        @LoginUser userId: Long,
        @PathVariable threadId: Long,
        @RequestParam(required = false, defaultValue = "MEMBER") userRole: UserRole,
        pageable: Pageable
    ): ApiResponse<Page<Chat>> {
        val chats = chatFacade.getThreadChats(userId, threadId, pageable, userRole)
        return ApiResponse.success(chats)
    }

    @DeleteMapping("/threads/{threadId}")
    fun deleteThread(
        @LoginUser userId: Long,
        @PathVariable threadId: Long,
        @RequestParam(required = false, defaultValue = "MEMBER") userRole: UserRole
    ): ApiResponse<Boolean> {
        chatFacade.deleteThread(userId, threadId, userRole)
        return ApiResponse.success(true)
    }
}
