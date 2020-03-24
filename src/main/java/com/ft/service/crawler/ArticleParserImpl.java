package com.ft.service.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ft.domain.Article;

@Service
public class ArticleParserImpl {

	protected static final Logger logger = LoggerFactory.getLogger(ArticleParserImpl.class);

	public static List<String> visitedUrl = Collections.synchronizedList(new ArrayList<String>());

	@PostConstruct
	private void load() {
		articleParser = this;
		logger.info("Article Parser Started...");
		logger.info("===== REGEXP: " + urlRegexp + " ==== CATE: " + category);
		logger.info("== Gotta grep Title attribute from " + titleSelector);
		logger.info("== Gotta grep Body attribute from " + fullcontentSelector);
		logger.info("== Gotta grep FeatureImgUrl attribute from " + imgSelector);
		logger.info("== Gotta grep PublishAt attribute from " + timeSelector);
		logger.info("== Gotta grep Tags attribute from " + tagSelector);

		datePattern = Pattern.compile(dateFmt.replaceAll("[dMy]", "\\\\d?"));
		logger.info("== Date format: " + dateFmt + " == Pattern " + datePattern.pattern());

		uriPattern = Pattern.compile(uriRegexp);
		logger.info("== Article URI format: " + uriRegexp + " == Pattern " + uriPattern.pattern());
	}

	@Value("${parser.title-selector: .leftCol h1}")
	private String titleSelector;

	@Value("${parser.body-selector: .fulltext_content}")
	private String fullcontentSelector;

	@Value("${parser.time-selector:.cate_time}")
	private String timeSelector;

	@Value("${parser.img-selector:.dtBoxl img}")
	private String imgSelector;

	@Value("${parser.urlRegexp}")
	private String urlRegexp;

	@Value("${parser.main-category:Article}")
	private String category;

	@Value("${parser.tag-selector:.tag}")
	private String tagSelector;

	@Value("${parser.uri:\\/([\\w_\\-]+)}")
	private String uriRegexp;
	private Pattern uriPattern;

	private Pattern hourPattern = Pattern.compile("\\d{2}:\\d{2}");

	@Value("${parser.dateFmt:dd/MM/yyyy}")
	private String dateFmt;
	private Pattern datePattern;

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
		visitedUrl.add(srcUrl);
		// Extract the title
		Elements titles = doc.select(titleSelector);
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
			entity.setCate(category);

			// Set title
			String title = titles.get(0).text();
			entity.setTitle(title);

			// Set slug
			Matcher m = uriPattern.matcher(srcUrl);
			if (m.find()) {
				entity.setSlug(m.group(m.groupCount()));
			} else {
				entity.setSlug(title.toLowerCase().replaceAll("^[a-z0-9\\-]", ""));
			}

			// Full content
			Elements fulltext_content = doc.select(fullcontentSelector);
			if (!fulltext_content.isEmpty()) {
				String fulltext_html = "";
				for (Element e : fulltext_content) {
					fulltext_html += e.html();
				}
				entity.setFullcontent(fulltext_html);
			}

			// Set publish time
			Elements time = doc.select(timeSelector);
			if (!time.isEmpty()) {
				String timeString = time.get(0).html();
				logger.info("Found a time string: " + timeString);
				SimpleDateFormat fmt = new SimpleDateFormat(dateFmt + " hh:mm");
				try {
					Matcher hourMatcher = hourPattern.matcher(timeString);
					Matcher dateMatcher = datePattern.matcher(timeString);

					if (dateMatcher.find()) {
						if (hourMatcher.find()) {
							String normalizedTimeString = dateMatcher.group() + " " + hourMatcher.group();
							logger.info("Found normallized timeString: " + normalizedTimeString
									+ " -- parse with format :" + dateFmt + " hh:mm");
							entity.setPublishAt(fmt.parse(normalizedTimeString));
						} else {
							String normalizedTimeString = dateMatcher.group() + " 00:00";
							logger.info("Found normallized timeString: " + normalizedTimeString
									+ " -- parse with format :" + dateFmt + " hh:mm");
							entity.setPublishAt(fmt.parse(dateMatcher.group() + " 00:00"));
						}
					} else {
						logger.error(
								"Cannot find any date time value with timeString. Please check your configuration..");
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("Cannot parse date string" + timeString);
					entity.setPublishAt(new Date());
				}
			}
			// Feature Images
			Elements img = doc.select(imgSelector);
			if (!img.isEmpty()) {
				String imgUrl = img.get(0).attr("src");
				entity.setFeatureImgUrl(imgUrl);
			}
			// Tags
			Elements tags = doc.select(tagSelector);
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
			logger.debug("Saving data crawled for URL: " + entity.getSrcUrl() + " --> " + entity.getTitle());
			logger.info(entity.toString());
		}
		return false;
	}

	public boolean parseAndSaveHtml(String html, String srcUrl) {
		Document doc = Jsoup.parse(html);
		return parseAndSaveHtml(doc, srcUrl);
	}

	public boolean shouldGrab(String url) {
		return (url.startsWith(urlRegexp) && (!visitedUrl.contains(url)));
	}

	public String getUrlRegexp() {
		return urlRegexp;
	}

	public void setUrlRegexp(String urlRegexp) {
		this.urlRegexp = urlRegexp;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Pattern getHourPattern() {
		return hourPattern;
	}

	public void setHourPattern(Pattern hourPattern) {
		this.hourPattern = hourPattern;
	}

	public String getDateFmt() {
		return dateFmt;
	}

	public void setDateFmt(String dateFmt) {
		this.dateFmt = dateFmt;
	}

	public Pattern getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(Pattern datePattern) {
		this.datePattern = datePattern;
	}

	public String getTagSelector() {
		return tagSelector;
	}

	public void setTagSelector(String tagSelector) {
		this.tagSelector = tagSelector;
	}

	private static ArticleParserImpl articleParser;
	public static ArticleParserImpl getInstance() {
		return articleParser;
	}
}
