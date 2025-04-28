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

package xyz.opcal.demo.ai.tools;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import xyz.opcal.demo.ai.configuration.properties.CurrencyExchangeProperties;
import xyz.opcal.demo.ai.dto.CurrencyExchangeRateRequest;

@Slf4j
public class CurrencyExchangeRateTool implements Function<CurrencyExchangeRateRequest, BigDecimal> {

	private final RestClient restClient;
	private final CurrencyExchangeProperties currencyExchangeProperties;

	public CurrencyExchangeRateTool(CurrencyExchangeProperties currencyExchangeProperties) {
		this.currencyExchangeProperties = currencyExchangeProperties;
		this.restClient = RestClient.create(this.currencyExchangeProperties.apiUrl());
	}

	@Override
	public BigDecimal apply(CurrencyExchangeRateRequest currencyExchangeRateRequest) {

		var response = restClient.get().uri("/currencies/{from}.json", currencyExchangeRateRequest.from().toLowerCase())
				.retrieve()
				.body(new ParameterizedTypeReference<Map<String, Object>>() {
				});

		var data = (Map<String, Object>) response.get(currencyExchangeRateRequest.from().toLowerCase());
		var rate = data.get(currencyExchangeRateRequest.to().toLowerCase());

		if (rate == null) {
			System.out.println("rate is null");
			throw new RuntimeException("Currency exchange rate not found");
		}
		log.info("rate data class is: {}", rate.getClass());
		log.info("Currency exchange from {} to {} rate: {}", currencyExchangeRateRequest.from(), currencyExchangeRateRequest.to() , rate);
		return new BigDecimal(rate.toString());
	}
}
