package com.ft.service.mapper;

import org.mapstruct.Mapper;

import com.ft.domain.CrawlerSettings;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;

@Mapper(componentModel = "spring" )
public interface CrawlConfigMapper {
	
	CrawlConfig toDto(CrawlerSettings settings);
	
	CrawlerSettings toEntity(CrawlConfig config);
}
