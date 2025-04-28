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

import java.io.IOException;
import java.nio.charset.Charset;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fc/community")
public class FcCommunityController {

	private final ChatClient chatClient;

	@Value("classpath:prompts/fc-community.st")
	private Resource fcPrompt;

	@Value("classpath:docs/community-fc-info.txt")
	private Resource fcInfoResource;

	public FcCommunityController(ChatClient.Builder builder) {
		this.chatClient = builder
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.build();
	}

	@GetMapping("/top10")
	public String top10(@RequestParam(defaultValue = "demo") String community, @RequestParam(defaultValue = "false") boolean stuffit) throws IOException {
		String fcInfo = fcInfoResource.getContentAsString(Charset.defaultCharset());
		return chatClient.prompt().user(u -> {
			u.text(fcPrompt);
			u.param("context", stuffit ? fcInfo : "no data").param("community", community);
		}).call().content();
	}

}
