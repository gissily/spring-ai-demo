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

package xyz.opcal.demo.ai.configuration;

import java.math.BigDecimal;
import java.util.function.Function;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import xyz.opcal.demo.ai.configuration.properties.CurrencyExchangeProperties;
import xyz.opcal.demo.ai.dto.CurrencyExchangeRateRequest;
import xyz.opcal.demo.ai.tools.CurrencyExchangeRateTool;

@EnableConfigurationProperties(CurrencyExchangeProperties.class)
@Configuration
public class CurrencyExchangeConfiguration {

	@Bean
	@Description("Get the currency exchange rate for the given source and target")
	Function<CurrencyExchangeRateRequest, BigDecimal> currencyExchangeRate(CurrencyExchangeProperties currencyExchangeProperties) {
		return new CurrencyExchangeRateTool(currencyExchangeProperties);
	}
}
