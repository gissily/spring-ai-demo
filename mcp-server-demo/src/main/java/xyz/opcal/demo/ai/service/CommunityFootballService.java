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

package xyz.opcal.demo.ai.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import xyz.opcal.demo.ai.dto.CommunityFcFAQ;
import xyz.opcal.demo.ai.dto.FootballClub;

@Slf4j
@Service
public class CommunityFootballService {

	@Value("classpath:/docs/community-football-club.json")
	private Resource fcClubResource;

	@Value("classpath:/docs/community-football-faq.json")
	private Resource faqResource;

	private final ObjectMapper objectMapper;

	private List<FootballClub> footballClubs;
	private List<CommunityFcFAQ> communityFcFAQs;

	public CommunityFootballService() {
		objectMapper = new ObjectMapper();
	}

	@PostConstruct
	public void init() throws IOException {
		footballClubs = Arrays.asList(objectMapper.readValue(fcClubResource.getContentAsByteArray(), FootballClub[].class));
		communityFcFAQs = Arrays.asList(objectMapper.readValue(faqResource.getContentAsByteArray(), CommunityFcFAQ[].class));
	}

	@Tool(name = "community_football_clubs", description = "Get a list of the football clubs from Demo community")
	public List<FootballClub> getFootballClubs() {
		log.info("query football clubs");
		return footballClubs;
	}

	@Tool(name = "community_football_faq", description = "Get a list of the football faq from Demo community")
	public List<CommunityFcFAQ> getFcFAQs() {
		log.info("query football faq");
		return communityFcFAQs;
	}

}
