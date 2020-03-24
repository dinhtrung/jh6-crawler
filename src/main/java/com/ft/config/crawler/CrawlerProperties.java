package com.ft.config.crawler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawler", ignoreUnknownFields = true)
public class CrawlerProperties {

	private int numberOfCrawlers = 5;
	
	private String crawlStorageFolder = "/tmp/crawler";

	private int maxPages = 100;

	private int delay = 1000;

	private int depth = 2;
	
	private boolean withBinary = false;
	
	private List<String> seedPages = new ArrayList<>();
	
	private String userAgent = "Mozilla/5.0 (X11; Fedora; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36";

	
	@PostConstruct
	private void load() {
	}

	public int getNumberOfCrawlers() {
		return numberOfCrawlers;
	}

	public String getCrawlStorageFolder() {
		return crawlStorageFolder;
	}

	public int getMaxPages() {
		return maxPages;
	}

	public int getDelay() {
		return delay;
	}

	public int getDepth() {
		return depth;
	}

	public boolean isWithBinary() {
		return withBinary;
	}

	public List<String> getSeedPages() {
		return seedPages;
	}

	public void setSeedPages(List<String> seedPages) {
		this.seedPages = seedPages;
	}

	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}

	public void setCrawlStorageFolder(String crawlStorageFolder) {
		this.crawlStorageFolder = crawlStorageFolder;
	}

	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setWithBinary(boolean withBinary) {
		this.withBinary = withBinary;
	}

	public String getUserAgent() {
		return this.userAgent;
	}

}
