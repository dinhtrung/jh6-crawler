package com.ft.service.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import com.ft.domain.Article;
import com.ft.domain.CrawlerSettings;
import com.ft.repository.ArticleRepository;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

@Component
@Scope("prototype")
public class ArticleParser extends WebCrawler {

	protected static final Logger logger = LoggerFactory.getLogger(ArticleParser.class);

	private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");

	@Autowired
	ArticleRepository articleRepository;
	
	private CrawlerSettings settings;
	
	/**
	 * You should implement this function to specify whether the given url should be
	 * crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		// Ignore the url if it has an extension that matches our defined set of image
		// extensions.
		if (IMAGE_EXTENSIONS.matcher(href).matches()) {
			return false;
		}
		// Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is
		// "http".
		return this.shouldGrab(url.getURL());
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 */
	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		String path = page.getWebURL().getPath();
		String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		String anchor = page.getWebURL().getAnchor();

		logger.debug("Docid: {}", docid);
		logger.info("URL: {}", url);
		logger.debug("Domain: '{}'", domain);
		logger.debug("Sub-domain: '{}'", subDomain);
		logger.debug("Path: '{}'", path);
		logger.debug("Parent page: {}", parentUrl);
		logger.debug("Anchor text: {}", anchor);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();

			parseAndSaveHtml(html, url);
		}
	}
//
//	@Value("${parser.title-selector: .leftCol h1}")
//	private String titleSelector;
//
//	@Value("${parser.body-selector: .fulltext_content}")
//	private String fullcontentSelector;
//
//	@Value("${parser.time-selector:.cate_time}")
//	private String timeSelector;
//
//	@Value("${parser.img-selector:.dtBoxl img}")
//	private String imgSelector;
//
//	@Value("${parser.urlRegexp}")
//	private String urlRegexp;
//
//	@Value("${parser.main-category:Article}")
//	private String category;
//
//	@Value("${parser.tag-selector:.tag}")
//	private String tagSelector;
//
//	@Value("${parser.uri:\\/([\\w_\\-]+)}")
//	private String uriRegexp;
//	private Pattern uriPattern;
//
//	private Pattern hourPattern = Pattern.compile("\\d{2}:\\d{2}");
//
//	@Value("${parser.dateFmt:dd/MM/yyyy}")
//	private String dateFmt;
//	private Pattern datePattern;

	public boolean parseAndSaveHtml(String srcUrl) {
		try {
			Document doc = Jsoup.connect(srcUrl).get();
			return parseAndSaveHtml(doc, srcUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean parseAndSaveHtml(Document doc, String srcUrl) {
		// Extract the title
		Elements titles = doc.select(settings.getTitleSelector());
		if (!titles.isEmpty()) {
			Article entity = new Article();

			// Extract srcUrl
			entity.setSrcUrl(srcUrl);

			// Extract baseUrl
			try {
				URL url = new URL(srcUrl);
				String base = url.getProtocol() + "://" + url.getHost();
				entity.setBaseUrl(base);
			} catch (MalformedURLException e1) {
				logger.debug("Cannot find the base URL for target web");
			}

			// Set Category
			entity.setCate(settings.getCategory());

			// Set title
			String title = titles.get(0).text();
			entity.setTitle(title);
			
			// Set slug
			if (settings.getUriRegexp() != null) {
				Pattern uriPattern = Pattern.compile(settings.getUriRegexp());
				Matcher m = uriPattern.matcher(srcUrl);
				if (m.find()) {
					entity.setSlug(m.group(m.groupCount()));
				}
			}
			if (entity.getSlug() == null) {
				entity.setSlug(title.toLowerCase().replaceAll("^[a-z0-9\\-]", ""));
			}
			

			// Summary
			if (settings.getSummarySelector() != null) {
				Elements summary_elements = doc.select(settings.getFullcontentSelector());
				if (!summary_elements.isEmpty()) {
					entity.setSummary(summary_elements.text());
				}
			}
			// Full content
			Elements fulltext_content = doc.select(settings.getFullcontentSelector());
			if (!fulltext_content.isEmpty()) {
				String fulltext_html = "";
				for (Element e : fulltext_content) {
					fulltext_html += e.html();
				}
				entity.setFullcontent(fulltext_html);
			}

			// Set publish time
			if (settings.getTimeSelector() != null) {
				Elements time = doc.select(settings.getTimeSelector());
				Pattern datePattern = Pattern.compile(settings.getDateFmt());
				if (!time.isEmpty()) {
					String timeString = time.get(0).text();
					logger.debug("Found a time string: {}", timeString);
					SimpleDateFormat fmt = new SimpleDateFormat(settings.getDateFmt() + " hh:mm");
					try {
						Matcher dateMatcher = datePattern.matcher(timeString);
						if (dateMatcher.find()) {
							entity.setPublishAt(fmt.parse(dateMatcher.group() + " 00:00"));
						} else {
							logger.error("Cannot find any date time value with timeString. Please check your configuration..");
						}
					} catch (Exception e) {
						logger.error("Cannot parse date string {}", timeString);
						entity.setPublishAt(new Date());
					}
				}
			}
			// Feature Images
			Elements img = doc.select(settings.getImgSelector());
			if (!img.isEmpty()) {
				String imgUrl = img.get(0).attr("src");
				entity.setFeatureImgUrl(imgUrl);
			}
			// Tags
			Elements tags = doc.select(settings.getTagSelector());
			if (!tags.isEmpty()) {
				List<String> tagStrings = new ArrayList<String>();
				for (Element tag : tags) {
					tagStrings.add(tag.text());
				}
				entity.setTags(tagStrings);
			} else {
				try {
					String[] metaKeywords = doc.select("meta[name=keywords]").get(0).attr("content").split(",");
					for (String s : metaKeywords) {
						entity.getTags().add(s);
					}
				} catch (Exception e) {
					logger.debug("Cannot find associate keywords or tags to article");
				}
			}
			// Save the entity and we are done
			logger.debug("+ Saving data crawled for URL: {} | {}", entity.getSrcUrl(), entity.getTitle());
			
			try {
				articleRepository.save(entity);
				logger.debug("* Saving data crawled for URL: {} | {}", entity.getSrcUrl(), entity.getTitle());
				return true;
			} catch (Exception e) {
				logger.error("Cannot save article: {}", entity, e);
			}
		}
		return false;
	}

	public boolean parseAndSaveHtml(String html, String srcUrl) {
		Document doc = Jsoup.parse(html);
		return parseAndSaveHtml(doc, srcUrl);
	}

	public boolean shouldGrab(String url) {
		return !articleRepository.findOne(Example.of(new Article().srcUrl(url))).isPresent();
	}

	public CrawlerSettings getSettings() {
		return settings;
	}

	public void setSettings(CrawlerSettings settings) {
		this.settings = settings;
	}

	@Override
	public String toString() {
		return "ArticleParser [settings=" + settings + "]";
	}
	
	
}
