package com.jhzlo.pretest.report.controller

import com.jhzlo.pretest.common.annotation.loginUser.LoginUser
import com.jhzlo.pretest.common.web.response.ApiResponse
import com.jhzlo.pretest.report.application.ReportFacade
import com.jhzlo.pretest.report.controller.dto.ActivitySummaryResponse
import com.jhzlo.pretest.user.domain.entity.vo.UserRole
import com.jhzlo.pretest.user.domain.service.UserService
import jakarta.servlet.http.HttpServletResponse
import org.apache.coyote.BadRequestException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/report")
class ReportController(
    private val reportFacade: ReportFacade,
    private val userService: UserService
) {

    @GetMapping("/activity")
    fun getActivitySummary(
        @LoginUser userId: Long
    ): ApiResponse<ActivitySummaryResponse> {
        val userRole = userService.getById(userId).userRole
        if (userRole != UserRole.ADMIN) {
            throw BadRequestException("pretest.auth.invalid-role")
        }
        val summary = reportFacade.getActivitySummary()
        return ApiResponse.success(summary)
    }

    @GetMapping("/download")
    fun downloadReport(
        @LoginUser userId: Long,
        response: HttpServletResponse
    ) {
        val userRole = userService.getById(userId).userRole
        if (userRole != UserRole.ADMIN) {
            throw BadRequestException("pretest.auth.invalid-role")
        }
        val csv = reportFacade.generateCsvReport()
        response.contentType = "text/csv"
        response.setHeader("Content-Disposition", "attachment; filename=\"report.csv\"")
        response.outputStream.write(csv)
        response.flushBuffer()
    }
}
