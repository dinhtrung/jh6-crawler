package com.ft.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final Logger log = LoggerFactory.getLogger(CrawlerManagementResource.class);
	
	@Autowired
	CrawlerService crawlerService;
	
	@Autowired
	CrawlerSettingsRepository crawlerSettingsRepo;
	
	@GetMapping("/start-crawler")
	public ResponseEntity<String> startCrawler() throws Exception {
		crawlerService.startCrawler();
		return ResponseEntity.accepted().body("OK");
	}
	
	@GetMapping("/start-crawler/{id}")
	public ResponseEntity<String> startCrawlerById(@PathVariable String id) throws Exception {
		CrawlerSettings settings = crawlerSettingsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		crawlerService.doStartCrawler(settings);
		return ResponseEntity.accepted().body("OK");
	}
	
	@GetMapping("/metrics/increase/{id}")
	public ResponseEntity<Void> importCampaign() throws Exception {
		
		return ResponseEntity.accepted().body(null);
	}
//	
//	@GetMapping("/archive-campaign/{id}")
//	public ResponseEntity<Void> archiveCampaign(@PathVariable String id) throws Exception {
//		Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//		campaignProcessorService.performArchive(campaign);
//		return ResponseEntity.accepted().body(null);
//	}
//	
//	@GetMapping("/schedulers")
//	public ResponseEntity<Void> startScheduler() {
//		schedulerService.activateScheduler();
//		return ResponseEntity.ok(null);
//	}
//	
//	@DeleteMapping("/schedulers")
//	public ResponseEntity<Void> stopScheduler() {
//		schedulerService.cancelAllTasks(true);
//		return ResponseEntity.ok(null);
//	}
//	

//	
//	@GetMapping("/stop-campaign/{id}")
//	public ResponseEntity<Campaign> stopCampaign(@PathVariable String id) throws Exception {
//		Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//		return ResponseEntity.accepted().body(campaignProcessorService.stopCampaign(campaign));
//	}
//	
//	@GetMapping("/pause-campaign/{id}")
//	public ResponseEntity<Campaign> Campaign(@PathVariable String id) throws Exception {
//		Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//		return ResponseEntity.accepted().body(campaignProcessorService.pauseCampaign(campaign));
//	}
//	
//	@GetMapping("/resume-campaign/{id}")
//	public ResponseEntity<Campaign> resumeCampaign(@PathVariable String id) throws Exception {
//		Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//		return ResponseEntity.accepted().body(campaignProcessorService.resumeCampaign(campaign));
//	}
//	
//	@GetMapping("/stats-campaign/{id}")
//	public ResponseEntity<Void> statsCampaign(@PathVariable String id) throws Exception {
//		Campaign campaign = campaignRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//		campaignProcessorService.campaignStatistics(campaign);
//		return ResponseEntity.accepted().body(null);
//	}

}
