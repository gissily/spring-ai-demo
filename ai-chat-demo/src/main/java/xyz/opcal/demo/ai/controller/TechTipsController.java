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

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tech-tips")
public class TechTipsController {

	private final ChatClient chatClient;

	public TechTipsController(ChatClient.Builder builder) {
		this.chatClient = builder
				.defaultSystem("Please respond to any questions about IT Tech Tips")
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.build();
	}


	@GetMapping("/java")
	public String javaTips(@RequestParam(defaultValue = "Java") String tech) {

		var tipsTemplate = """
				Please tell me a tips about {tech}
				""";

		return chatClient.prompt()
				.system("Please only respond to tech tips about java")
				.user(spec -> spec.text(tipsTemplate).params(Map.of("tech", tech)))
				.call()
				.content();
	}

}
