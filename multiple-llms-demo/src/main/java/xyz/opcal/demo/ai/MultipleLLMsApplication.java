/*
 * Copyright 2020-2025 Opcal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.opcal.demo.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MultipleLLMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleLLMsApplication.class, args);
	}

	@Bean("openAiChatClient")
	ChatClient openAiChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
		return ChatClient
				.builder(model)
				.defaultAdvisors(
						MessageChatMemoryAdvisor.builder(chatMemory).build(),
						PromptChatMemoryAdvisor.builder(chatMemory).build())
				.build();
	}

	@Bean("ollamaChatClient")
	ChatClient ollamaChatClient(OllamaChatModel model, ChatMemory chatMemory) {
		return ChatClient
				.builder(model)
				.defaultAdvisors(
						MessageChatMemoryAdvisor.builder(chatMemory).build(),
						PromptChatMemoryAdvisor.builder(chatMemory).build())
				.build();
	}

}
