package com.jhzlo.pretest.chat.infrastructure

import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.stereotype.Component

@Component
class OpenAiService(
    private val openAiApi: OpenAiApi
) {
    fun createAnswer(messages: List<Message>, model: String, isStreaming: Boolean): String? {
        val chatOptions = ChatOptions.builder()
            .model(model)
            .build()

        val prompt = Prompt(messages, chatOptions)

        val chatModel = OpenAiChatModel.builder()
            .openAiApi(openAiApi)
            .build()

        val result = chatModel.call(prompt)
        return result?.result?.output?.text
    }
}
