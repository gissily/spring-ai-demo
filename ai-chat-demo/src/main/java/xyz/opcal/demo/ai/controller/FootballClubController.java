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
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xyz.opcal.demo.ai.dto.FootballClub;

@RestController
@RequestMapping("/fc")
public class FootballClubController {

	private final ChatClient chatClient;

	@Value("classpath:prompts/football-club.st")
	private Resource fcPrompt;

	@Value("classpath:prompts/football-club-output-format.st")
	private Resource fcOutputPrompt;

	public FootballClubController(ChatClient.Builder builder) {
		this.chatClient = builder
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.build();
	}

	@GetMapping("/top10")
	public String top10(@RequestParam String region) {

		var promptTemplate = new PromptTemplate(fcPrompt);
		var prompt = promptTemplate.create(Map.of("region", region));
		return chatClient.prompt(prompt).call().content();
	}

	@GetMapping("/top10/list")
	public List<String> top10List(@RequestParam String region) {
		var promptTemplate = new PromptTemplate(fcOutputPrompt);
		var outputConverter = new ListOutputConverter(new DefaultConversionService());

		var prompt = promptTemplate.create(Map.of("region", region, "format", "result in 'club name - stadium' and " + outputConverter.getFormat()));
		return outputConverter.convert(chatClient.prompt(prompt).call().content());
	}

	@GetMapping("/top10/map")
	public Map<String, Object> top10Map(@RequestParam String region) {
		var promptTemplate = new PromptTemplate(fcOutputPrompt);
		var outputConverter = new MapOutputConverter();

		var prompt = promptTemplate.create(
				Map.of("region", region, "format", outputConverter.getFormat() + " and the map structure is using the club as key and stadium as the value"));
		return outputConverter.convert(chatClient.prompt(prompt).call().content());
	}

	@GetMapping("/top10/bean")
	public List<FootballClub> top10Beans(@RequestParam String region) {
		var promptTemplate = new PromptTemplate(fcOutputPrompt);
		var outputConverter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<FootballClub>>() {
		});

		var prompt = promptTemplate.create(Map.of("region", region, "format", outputConverter.getFormat()));
		return outputConverter.convert(chatClient.prompt(prompt).call().content());
	}

	@GetMapping("/top10/entity")
	public List<FootballClub> top10Entity(@RequestParam String region) {
		var promptTemplate = new PromptTemplate(fcPrompt);

		var prompt = promptTemplate.create(Map.of("region", region));

		return chatClient
				.prompt(prompt)
				.call()
				.entity(new ParameterizedTypeReference<>() {});
	}

}
