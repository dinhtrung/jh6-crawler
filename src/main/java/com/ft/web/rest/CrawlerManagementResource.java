package com.ft.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.ft.domain.CrawlerSettings;
import com.ft.repository.CrawlerSettingsRepository;
import com.ft.service.crawler.CrawlerService;


@Controller
@RequestMapping("/api/management")
public class CrawlerManagementResource {

	@Autowired
	CrawlerService crawlerService;
	
	@Autowired
	CrawlerSettingsRepository crawlerSettingsRepo;
	
	@GetMapping("/stop-crawler/{id}")
	public ResponseEntity<CrawlerSettings> startCrawler(@PathVariable String id) throws Exception {
		CrawlerSettings settings = crawlerSettingsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		crawlerService.doStopCrawler(id);
		return ResponseEntity.accepted().body(settings);
	}
	
	@GetMapping("/start-crawler/{id}")
	public ResponseEntity<CrawlerSettings> startCrawlerById(@PathVariable String id) throws Exception {
		CrawlerSettings settings = crawlerSettingsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		crawlerService.doStartCrawler(settings);
		return ResponseEntity.accepted().body(settings);
	}
}
