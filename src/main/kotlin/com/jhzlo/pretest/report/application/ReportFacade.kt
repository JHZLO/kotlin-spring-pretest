package com.jhzlo.pretest.report.application

import com.jhzlo.pretest.chat.domain.service.ChatService
import com.jhzlo.pretest.report.controller.dto.ActivitySummaryResponse
import com.jhzlo.pretest.report.domain.service.LoginHistoryService
import com.jhzlo.pretest.user.domain.service.UserService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ReportFacade(
    private val userService: UserService,
    private val chatService: ChatService,
    private val loginHistoryService: LoginHistoryService
) {
    fun getActivitySummary(): ActivitySummaryResponse {
        val end = LocalDateTime.now()
        val start = end.minusDays(1)

        val userCount = userService.countCreatedBetween(start, end)
        val loginCount = loginHistoryService.countLoginBetween(start, end)
        val chatCount = chatService.countCreatedBetween(start, end)

        return ActivitySummaryResponse(
            userCount = userCount,
            loginCount = loginCount,
            chatCount = chatCount
        )
    }

    fun generateCsvReport(): ByteArray {
        val end = LocalDateTime.now()
        val start = end.minusDays(1)

        val chats = chatService.findCreatedBetween(start, end)

        val csvHeader = "chat_id,question,answer,user_id,created_at\n"
        val csvBody = chats.joinToString("\n") { chat ->
            "${chat.id},\"${chat.question}\",\"${chat.answer}\",${chat.chatThread.userId},${chat.createdAt}"
        }

        return (csvHeader + csvBody).toByteArray(Charsets.UTF_8)
    }
}
