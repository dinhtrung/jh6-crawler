package com.ft.repository;

import com.ft.domain.Article;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Article entity.
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, String>, QuerydslPredicateExecutor<Article> {

}
