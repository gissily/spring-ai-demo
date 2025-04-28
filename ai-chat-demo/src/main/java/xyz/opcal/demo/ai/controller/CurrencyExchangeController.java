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
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/currency")
public class CurrencyExchangeController {

	private final ChatClient chatClient;

	public CurrencyExchangeController(ChatClient.Builder builder) {
		this.chatClient = builder
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.defaultTools("currencyExchangeRate")
				.build();
	}

	@GetMapping("/exchange")
	public String exchangeRate(@RequestParam String from, @RequestParam String to, @RequestParam String total) {
		var template = """
				Please exchange the money total {total} from {from} to {to} by currency
				""";

		var promptsTemplate = new PromptTemplate(template);
		var prompt = promptsTemplate.create(Map.of("from", from, "to", to, "total", total));
		return chatClient
				.prompt(prompt)
				.call()
				.content();
	}

}
