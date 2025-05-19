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
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fc/community")
public class FcCommunityController {

	private final ChatClient chatClient;

	public FcCommunityController(ChatClient.Builder builder, ToolCallbackProvider callbackProvider) {
		this.chatClient = builder
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.defaultToolCallbacks(callbackProvider)
				.defaultSystem("You are the assistant about the Demo community football, and you need to initialize both football clubs and faq data first, then analyze them for each question")
				.build();
	}

	@GetMapping("/query")
	public String query(@RequestParam(required = false, defaultValue = "the top 10 clubs in the demo community") String message) {
		return chatClient
				.prompt()
				.user(message)
				.call()
				.content();
	}

}
