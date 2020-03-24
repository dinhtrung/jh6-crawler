package com.ft.domain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("article")
public class Article {
	
	@Id
	private String id;
	
	private String title;
	private String srcUrl;
	private String baseUrl;
	private String slug;


	private String fullcontent;
	private Date publishAt;
	private String featureImgUrl;
	private String cate;
	private List<String> tags;

	public Article() {
		tags = new ArrayList<String>();
	}

	@Override
	public String toString() {
		return String.format("Article[srcUrl='%s', title='%s']", srcUrl, title);
	}

	public String getFullcontent() {
		return fullcontent;
	}

	public void setFullcontent(String fullcontent) {
		this.fullcontent = fullcontent;
	}

	public String getSrcUrl() {
		return srcUrl;
	}

	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public String getFeatureImgUrl() {
		return featureImgUrl;
	}

	public void setFeatureImgUrl(String featureImgUrl) {
		this.featureImgUrl = featureImgUrl;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
