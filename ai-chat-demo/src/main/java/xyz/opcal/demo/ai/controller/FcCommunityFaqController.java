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

package xyz.opcal.demo.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fc/community/qa")
public class FcCommunityFaqController {

	private final ChatClient chatClient;

	public FcCommunityFaqController(ChatClient.Builder builder, VectorStore vectorStore, ChatMemory chatMemory) {
		this.chatClient = builder
				.defaultAdvisors(new SimpleLoggerAdvisor(),
						QuestionAnswerAdvisor.builder(vectorStore).build(),
						MessageChatMemoryAdvisor.builder(chatMemory).build())
				.build();
	}

	@GetMapping("/faq")
	public String top10(@RequestParam(defaultValue = "the top 10 clubs in the demo community") String message) {
		return chatClient
				.prompt()
				.user(message)
				.call()
				.content();
	}

}
