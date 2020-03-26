package com.ft.service.crawler;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.domain.CrawlerSettings;
import com.ft.repository.CrawlerSettingsRepository;
import com.ft.service.mapper.ArticleParserMapper;
import com.ft.service.mapper.CrawlConfigMapper;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


@Service
public class CrawlerService {

	protected static final Logger logger = LoggerFactory.getLogger(CrawlerService.class);
	
	@Autowired
    CrawlConfigMapper crawlerMapper;
	
	@Autowired
	ArticleParserMapper articleParserMapper;
	
	@Autowired
	CrawlerSettingsRepository crawlerRepository;
	
	@Autowired
	ApplicationContext appCtx;
	
	/**
	 * Run all the crawler in every 1 hours
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0 * * * *")
	public void init() throws Exception {
		for (CrawlerSettings setting : crawlerRepository.findAll(Example.of(new CrawlerSettings().state(200)))) {
			doStartCrawler(setting);
		}
	}

	public static ConcurrentHashMap<String, CrawlController> activeCrawlers = new ConcurrentHashMap<>();
	
	/**
	 * Convert a Crawler Settings from DB to object and run it as will
	 * @param setting
	 * @throws Exception
	 */
	@Async
	public void doStartCrawler(CrawlerSettings setting) throws Exception {
			CrawlConfig config = crawlerMapper.toDto(setting);
			
			ArticleParser articleParser = articleParserMapper.toDto(setting);
			
			logger.debug("Prepare to run crawler with config {} and settings {}", config, articleParser);

			/*
			 * Instantiate the controller for this crawl.
			 */
			PageFetcher pageFetcher = new PageFetcher(config);
			RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
			RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
			CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

			/*
			 * For each crawl, you need to add some seed urls. These are the first
			 * URLs that are fetched and then the crawler starts following links
			 * which are found in these pages
			 */
			// controller.addSeed("http://www.ics.uci.edu/");
			// controller.addSeed("http://www.ics.uci.edu/~lopes/");
			// controller.addSeed("http://www.ics.uci.edu/~welling/");
			for (String page: setting.getSeedPages()){
				controller.addSeed(page);
			}
			ArticleParserFactory factory = appCtx.getBean(ArticleParserFactory.class);
			factory.setSettings(setting);
			/*
			 * Start the crawl. This is a blocking operation, meaning that your code
			 * will reach the line after this only when crawling is finished.
			 */
			controller.startNonBlocking(factory, setting.getNumberOfCrawlers());
			
			activeCrawlers.put(setting.getId(), controller);
	}
	
	@Async
	public void doStopCrawler(String id) {
		activeCrawlers.get(id).shutdown();
	}
	
	@PreDestroy
	public void shutdown() {
		for (Iterator<Entry<String, CrawlController>> it = activeCrawlers.entrySet().iterator(); it.hasNext(); ) {
			Entry<String, CrawlController> entry = it.next();
			entry.getValue().shutdown();
			activeCrawlers.remove(entry.getKey());
		}
	}
}
