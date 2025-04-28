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

package xyz.opcal.demo.ai.loader;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReferenceDocsLoader {

	private final JdbcClient jdbcClient;
	private final VectorStore vectorStore;

	@Value("classpath:/docs/BASIC-MATHEMATICS.pdf")
	private Resource pdfResource;

	public ReferenceDocsLoader(JdbcClient jdbcClient, VectorStore vectorStore) {
		this.jdbcClient = jdbcClient;
		this.vectorStore = vectorStore;
	}


	@PostConstruct
	public void init() {
		Integer count = jdbcClient.sql("select count(*) from vector_store")
				.query(Integer.class)
				.single();

		log.info("Current count of the Vector Store: {}", count);
		if (count == 0) {
			log.info("Loading Spring Boot Reference PDF into Vector Store");
			var config = PdfDocumentReaderConfig.builder()
					.withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(0)
							.withNumberOfTopPagesToSkipBeforeDelete(0)
							.build())
					.withPagesPerDocument(1)
					.build();

			var pdfReader = new PagePdfDocumentReader(pdfResource, config);
			var textSplitter = new TokenTextSplitter();
			vectorStore.accept(textSplitter.apply(pdfReader.get()));

			log.info("Application is ready");
		}
	}

}
