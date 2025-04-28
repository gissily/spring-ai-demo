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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FcCommunityRagConfiguration {

	@Value("vectorstore.json")
	private String vectorStoreName;

	@Value("classpath:/docs/community-fc-faq.txt")
	private Resource faq;

	@Bean
	SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
		var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
		var vectorStoreFile = getVectorStoreFile();
		if (vectorStoreFile.exists()) {
			log.info("Vector Store File Exists,");
			simpleVectorStore.load(vectorStoreFile);
		} else {
			log.info("Vector Store File Does Not Exist, loading documents");
			TextReader textReader = new TextReader(faq);
			textReader.getCustomMetadata().put("filename", "community-fc-faq.txt");
			List<Document> documents = textReader.get();
			TextSplitter textSplitter = new TokenTextSplitter();
			List<Document> splitDocuments = textSplitter.apply(documents);
			simpleVectorStore.add(splitDocuments);
			simpleVectorStore.save(vectorStoreFile);
		}
		return simpleVectorStore;
	}

	private File getVectorStoreFile() {
		Path path = Paths.get("src", "main", "resources", "data", vectorStoreName);
		return new File(path.toFile().getAbsolutePath());
	}
}
