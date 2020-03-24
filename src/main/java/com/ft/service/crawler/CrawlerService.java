package com.ft.service.crawler;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ft.config.crawler.ArticleCrawlerController;


@Service
public class CrawlerService {

	@Autowired
	private ArticleCrawlerController crawler;
	
	@PostConstruct
	public void init() throws Exception {
		crawler.run();
	}
	
	@Async
	public void runCrawler() throws Exception {
		crawler.run();
	}
}
