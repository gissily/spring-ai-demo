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

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathAssistantController {

	private final ChatClient chatClient;
	private final VectorStore vectorStore;

	@Value("classpath:/prompts/math-reference.st")
	private Resource mathPromptTemplate;

	public MathAssistantController(ChatClient.Builder builder, VectorStore vectorStore, ChatMemory chatMemory) {
		this.vectorStore = vectorStore;
		this.chatClient = builder
				.defaultAdvisors(
						MessageChatMemoryAdvisor.builder(chatMemory).build(),
						PromptChatMemoryAdvisor.builder(chatMemory).build())
				.build();
	}

	@GetMapping("/tips")
	public String tips(@RequestParam String message) {
		var promptTemplate = new PromptTemplate(mathPromptTemplate);

		var prompt = promptTemplate.create(Map.of("input", message, "documents", String.join("\n", findSimilarDocuments(message))));

		return chatClient
				.prompt(prompt)
				.call()
				.content();
	}

	private List<String> findSimilarDocuments(String message) {
		var similarDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(3).build());
		return similarDocuments.stream().map(Document::getText).toList();
	}

}
