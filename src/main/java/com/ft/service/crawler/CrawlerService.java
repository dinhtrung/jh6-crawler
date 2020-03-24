package com.ft.service.crawler;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.config.Constants;
import com.ft.config.crawler.ArticleCrawlerController;
import com.ft.domain.Article;
import com.ft.repository.ArticleRepository;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;


@Service
public class CrawlerService {

	protected static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);
	
	@Autowired
	private ArticleCrawlerController crawler;
	
	@Autowired
	HazelcastInstance hazelcastInstance;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ArticleRepository articleRepository;
	
	@PostConstruct
	public void init() throws Exception {
		crawler.run();
	}
	
	@Async
	public void runCrawler() throws Exception {
		crawler.run();
	}

	@Scheduled(fixedRate = 10000L)
	public void savePendingQueue() {
		logger.info("Processing pending queue...");
		IQueue<String> articleQueue = hazelcastInstance.getQueue(Constants.ARTICLE_QUEUE);
		articleQueue.iterator().forEachRemaining(json -> {
			try {
				Article entity = objectMapper.readValue(json, Article.class);
				entity = articleRepository.save(entity);
				logger.debug("Successfully save data: {}", entity);
			} catch (JsonProcessingException e) {
				logger.error("Cannot encode data: {}", e);
			} catch (Exception e1) {
				logger.error("Cannot save data: {}", e1);
			}
		});
	}
	
}
