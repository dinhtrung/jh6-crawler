package com.ft.service.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ft.domain.CrawlerSettings;

import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;

@Component
@Scope("prototype")
public class ArticleParserFactory implements WebCrawlerFactory<ArticleParser> {

	protected static final Logger log = LoggerFactory.getLogger(ArticleParserFactory.class);
	
	@Autowired 
	ApplicationContext appCtx;
	
	private CrawlerSettings settings;
	
	@Override
	public ArticleParser newInstance() throws Exception {
		log.info("Should create a new instance of web crawlewr");
		ArticleParser instance = appCtx.getBean(ArticleParser.class);
		instance.setSettings(settings);
		return instance;
	}

	public CrawlerSettings getSettings() {
		return settings;
	}

	public void setSettings(CrawlerSettings settings) {
		this.settings = settings;
	}

	
}
