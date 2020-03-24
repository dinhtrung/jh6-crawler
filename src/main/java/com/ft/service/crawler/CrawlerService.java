package com.ft.service.crawler;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.ft.config.crawler.ArticleCrawler;
import com.ft.config.crawler.ArticleCrawlerController;
import com.ft.domain.CrawlerSettings;
import com.ft.repository.CrawlerSettingsRepository;
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
	private ArticleCrawlerController crawler;
	
	@Autowired
    CrawlConfigMapper mapper;
	
	@Autowired
	CrawlerSettingsRepository crawlerRepository;
	

	
	@Scheduled(fixedDelay = 3600000)
	public void init() throws Exception {
		crawler.run();
//		for (CrawlerSettings setting : crawlerRepository.findAll(Example.of(new CrawlerSettings().state(200)))) {
//			doStartCrawler(setting);
//		}
	}

	public static ConcurrentHashMap<String, ArticleCrawlerController> activeCrawlers = new ConcurrentHashMap<>();
	
	/**
	 * Convert a Crawler Settings from DB to object and run it as will
	 * @param setting
	 * @throws Exception
	 */
	public void doStartCrawler(CrawlerSettings setting) throws Exception {
			CrawlConfig config = mapper.toDto(setting);

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

			/*
			 * Start the crawl. This is a blocking operation, meaning that your code
			 * will reach the line after this only when crawling is finished.
			 */
			controller.startNonBlocking(ArticleCrawler.class, setting.getNumberOfCrawlers());
	}

	@Async
	public void startCrawler() throws Exception {
		crawler.run();
	}
}
