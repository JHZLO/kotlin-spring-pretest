package com.jhzlo.pretest.common.configuration

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAiConfig {
    private val logger = KotlinLogging.logger {}

    @Value("\${spring.ai.openai.api-key}")
    private lateinit var apiKey: String

    @Bean
    fun openAiApi(): OpenAiApi {
        logger.debug { "open ai 초기화" }
        return OpenAiApi.builder()
            .apiKey(apiKey)
            .build()
    }
}
