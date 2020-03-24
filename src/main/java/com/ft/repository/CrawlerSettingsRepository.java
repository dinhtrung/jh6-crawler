package com.ft.repository;

import com.ft.domain.CrawlerSettings;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the CrawlerSettings entity.
 */
@Repository
public interface CrawlerSettingsRepository extends MongoRepository<CrawlerSettings, String>, QuerydslPredicateExecutor<CrawlerSettings> {

}
