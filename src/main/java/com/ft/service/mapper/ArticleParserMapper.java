package com.ft.service.mapper;

import org.mapstruct.Mapper;

import com.ft.domain.CrawlerSettings;
import com.ft.service.crawler.ArticleParser;

@Mapper(componentModel = "spring" )
public interface ArticleParserMapper {
	
	ArticleParser toDto(CrawlerSettings settings);
	
	CrawlerSettings toEntity(ArticleParser config);
}
