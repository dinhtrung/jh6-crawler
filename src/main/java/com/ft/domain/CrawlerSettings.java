package com.ft.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("crawler_settings")
public class CrawlerSettings {
	
	@Id
	private String id;
	
	@Field
	private String name;
	
	@Field
	private int state = 0;
	
	@Field("crawler_cnt")
	private int numberOfCrawlers = 1;
	
	@Field("seed_pages")
	private Set<String> seedPages = new HashSet<>();
	
	@Field("title_selector")
	private String titleSelector = "h1:first-child";

	@Field("content_selector")
	private String fullcontentSelector = "main";

	@Field("time_selector")
	private String timeSelector = "date";

	@Field("img_selector")
	private String imgSelector = "img:first-child";

	@Field("url_regexp")
	private String urlRegexp;

	@Field("category")
	private String category;

	@Field("tag_selector")
	private String tagSelector = ".badge";

	@Field("uri_regexp")
	private String uriRegexp;

	@Field("date_format")
	private String dateFmt = "yyyy-MM-dd";
	
	/**
     * The folder which will be used by crawler for storing the intermediate
     * crawl data. The content of this folder should not be modified manually.
     */
    private String crawlStorageFolder;

    /**
     * If this feature is enabled, you would be able to resume a previously
     * stopped/crashed crawl. However, it makes crawling slightly slower
     */
    private boolean resumableCrawling = false;

    /**
     * The lock timeout for the underlying sleepycat DB, in milliseconds
     */
    private long dbLockTimeout = 500;

    /**
     * Maximum depth of crawling For unlimited depth this parameter should be
     * set to -1
     */
    private int maxDepthOfCrawling = -1;

    /**
     * Maximum number of pages to fetch For unlimited number of pages, this
     * parameter should be set to -1
     */
    private int maxPagesToFetch = -1;

    /**
     * user-agent string that is used for representing your crawler to web
     * servers. See http://en.wikipedia.org/wiki/User_agent for more details
     */
    private String userAgentString = "crawler4j (https://github.com/yasserg/crawler4j/)";

    /**
     * Politeness delay in milliseconds (delay between sending two requests to
     * the same host).
     */
    private int politenessDelay = 200;

    /**
     * Should we also crawl https pages?
     */
    private boolean includeHttpsPages = true;

    /**
     * Should we fetch binary content such as images, audio, ...?
     */
    private boolean includeBinaryContentInCrawling = false;

    /**
     * Should we process binary content such as image, audio, ... using TIKA?
     */
    private boolean processBinaryContentInCrawling = false;

    /**
     * Maximum Connections per host
     */
    private int maxConnectionsPerHost = 100;

    /**
     * Maximum total connections
     */
    private int maxTotalConnections = 100;

    /**
     * Socket timeout in milliseconds
     */
    private int socketTimeout = 20000;

    /**
     * Connection timeout in milliseconds
     */
    private int connectionTimeout = 30000;

    /**
     * Max number of outgoing links which are processed from a page
     */
    private int maxOutgoingLinksToFollow = 5000;

    /**
     * Max allowed size of a page. Pages larger than this size will not be
     * fetched.
     */
    private int maxDownloadSize = 1048576;

    /**
     * Should we follow redirects?
     */
    private boolean followRedirects = true;

    /**
     * Should the TLD list be updated automatically on each run? Alternatively,
     * it can be loaded from the embedded tld-names.zip file that was obtained from
     * https://publicsuffix.org/list/effective_tld_names.dat
     */
    private boolean onlineTldListUpdate = false;

    /**
     * Should the crawler stop running when the queue is empty?
     */
    private boolean shutdownOnEmptyQueue = true;

    /**
     * Wait this long before checking the status of the worker threads.
     */
    private int threadMonitoringDelaySeconds = 10;

    /**
     * Wait this long to verify the craweler threads are finished working.
     */
    private int threadShutdownDelaySeconds = 10;

    /**
     * Wait this long in seconds before launching cleanup.
     */
    private int cleanupDelaySeconds = 10;

    /**
     * Whether to honor "nofollow" flag
     */
    private boolean respectNoFollow = true;

    /**
     * Whether to honor "noindex" flag
     */
    private boolean respectNoIndex = true;


    
    
	@Override
	public String toString() {
		return "CrawlerSettings [name=" + name + ", numberOfCrawlers=" + numberOfCrawlers + ", seedPages=" + seedPages
				+ ", titleSelector=" + titleSelector + ", fullcontentSelector=" + fullcontentSelector
				+ ", timeSelector=" + timeSelector + ", imgSelector=" + imgSelector + ", urlRegexp=" + urlRegexp
				+ ", category=" + category + ", tagSelector=" + tagSelector + ", uriRegexp=" + uriRegexp + ", dateFmt="
				+ dateFmt + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCrawlStorageFolder() {
		return crawlStorageFolder;
	}

	public void setCrawlStorageFolder(String crawlStorageFolder) {
		this.crawlStorageFolder = crawlStorageFolder;
	}

	public boolean isResumableCrawling() {
		return resumableCrawling;
	}

	public void setResumableCrawling(boolean resumableCrawling) {
		this.resumableCrawling = resumableCrawling;
	}

	public long getDbLockTimeout() {
		return dbLockTimeout;
	}

	public void setDbLockTimeout(long dbLockTimeout) {
		this.dbLockTimeout = dbLockTimeout;
	}

	public int getMaxDepthOfCrawling() {
		return maxDepthOfCrawling;
	}

	public void setMaxDepthOfCrawling(int maxDepthOfCrawling) {
		this.maxDepthOfCrawling = maxDepthOfCrawling;
	}

	public int getMaxPagesToFetch() {
		return maxPagesToFetch;
	}

	public void setMaxPagesToFetch(int maxPagesToFetch) {
		this.maxPagesToFetch = maxPagesToFetch;
	}

	public String getUserAgentString() {
		return userAgentString;
	}

	public void setUserAgentString(String userAgentString) {
		this.userAgentString = userAgentString;
	}

	public int getPolitenessDelay() {
		return politenessDelay;
	}

	public void setPolitenessDelay(int politenessDelay) {
		this.politenessDelay = politenessDelay;
	}

	public boolean isIncludeHttpsPages() {
		return includeHttpsPages;
	}

	public void setIncludeHttpsPages(boolean includeHttpsPages) {
		this.includeHttpsPages = includeHttpsPages;
	}

	public boolean isIncludeBinaryContentInCrawling() {
		return includeBinaryContentInCrawling;
	}

	public void setIncludeBinaryContentInCrawling(boolean includeBinaryContentInCrawling) {
		this.includeBinaryContentInCrawling = includeBinaryContentInCrawling;
	}

	public boolean isProcessBinaryContentInCrawling() {
		return processBinaryContentInCrawling;
	}

	public void setProcessBinaryContentInCrawling(boolean processBinaryContentInCrawling) {
		this.processBinaryContentInCrawling = processBinaryContentInCrawling;
	}

	public int getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxOutgoingLinksToFollow() {
		return maxOutgoingLinksToFollow;
	}

	public void setMaxOutgoingLinksToFollow(int maxOutgoingLinksToFollow) {
		this.maxOutgoingLinksToFollow = maxOutgoingLinksToFollow;
	}

	public int getMaxDownloadSize() {
		return maxDownloadSize;
	}

	public void setMaxDownloadSize(int maxDownloadSize) {
		this.maxDownloadSize = maxDownloadSize;
	}

	public boolean isFollowRedirects() {
		return followRedirects;
	}

	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	public boolean isOnlineTldListUpdate() {
		return onlineTldListUpdate;
	}

	public void setOnlineTldListUpdate(boolean onlineTldListUpdate) {
		this.onlineTldListUpdate = onlineTldListUpdate;
	}

	public boolean isShutdownOnEmptyQueue() {
		return shutdownOnEmptyQueue;
	}

	public void setShutdownOnEmptyQueue(boolean shutdownOnEmptyQueue) {
		this.shutdownOnEmptyQueue = shutdownOnEmptyQueue;
	}

	public int getThreadMonitoringDelaySeconds() {
		return threadMonitoringDelaySeconds;
	}

	public void setThreadMonitoringDelaySeconds(int threadMonitoringDelaySeconds) {
		this.threadMonitoringDelaySeconds = threadMonitoringDelaySeconds;
	}

	public int getThreadShutdownDelaySeconds() {
		return threadShutdownDelaySeconds;
	}

	public void setThreadShutdownDelaySeconds(int threadShutdownDelaySeconds) {
		this.threadShutdownDelaySeconds = threadShutdownDelaySeconds;
	}

	public int getCleanupDelaySeconds() {
		return cleanupDelaySeconds;
	}

	public void setCleanupDelaySeconds(int cleanupDelaySeconds) {
		this.cleanupDelaySeconds = cleanupDelaySeconds;
	}

	public boolean isRespectNoFollow() {
		return respectNoFollow;
	}

	public void setRespectNoFollow(boolean respectNoFollow) {
		this.respectNoFollow = respectNoFollow;
	}

	public boolean isRespectNoIndex() {
		return respectNoIndex;
	}

	public void setRespectNoIndex(boolean respectNoIndex) {
		this.respectNoIndex = respectNoIndex;
	}

	public Set<String> getSeedPages() {
		return seedPages;
	}

	public void setSeedPages(Set<String> seedPages) {
		this.seedPages = seedPages;
	}

	public CrawlerSettings state(int state) {
		this.state = state;
		return this;
	}

	public int getNumberOfCrawlers() {
		return numberOfCrawlers ;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTitleSelector() {
		return titleSelector;
	}

	public void setTitleSelector(String titleSelector) {
		this.titleSelector = titleSelector;
	}

	public String getFullcontentSelector() {
		return fullcontentSelector;
	}

	public void setFullcontentSelector(String fullcontentSelector) {
		this.fullcontentSelector = fullcontentSelector;
	}

	public String getTimeSelector() {
		return timeSelector;
	}

	public void setTimeSelector(String timeSelector) {
		this.timeSelector = timeSelector;
	}

	public String getImgSelector() {
		return imgSelector;
	}

	public void setImgSelector(String imgSelector) {
		this.imgSelector = imgSelector;
	}

	public String getUrlRegexp() {
		return urlRegexp;
	}

	public void setUrlRegexp(String urlRegexp) {
		this.urlRegexp = urlRegexp;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTagSelector() {
		return tagSelector;
	}

	public void setTagSelector(String tagSelector) {
		this.tagSelector = tagSelector;
	}

	public String getUriRegexp() {
		return uriRegexp;
	}

	public void setUriRegexp(String uriRegexp) {
		this.uriRegexp = uriRegexp;
	}

	public String getDateFmt() {
		return dateFmt;
	}

	public void setDateFmt(String dateFmt) {
		this.dateFmt = dateFmt;
	}

	public void setNumberOfCrawlers(int numberOfCrawlers) {
		this.numberOfCrawlers = numberOfCrawlers;
	}

	
	
	
}
