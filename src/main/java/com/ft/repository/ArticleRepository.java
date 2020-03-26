package com.ft.repository;

import com.ft.domain.Article;
import com.ft.domain.QArticle;

import java.util.Optional;

import org.bitbucket.gt_tech.spring.data.querydsl.value.operators.ExpressionProviderFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Article entity.
 */
@Repository
public interface ArticleRepository extends MongoRepository<Article, String>, QuerydslPredicateExecutor<Article>, QuerydslBinderCustomizer<QArticle> {

	/**
	 * Override Querydsl handling on predicate
	 */
	@Override
	default void customize(QuerydslBindings bindings, QArticle root) {
		// All attribute of QArticle should be here
		bindings.bind(root.id).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.title).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.summary).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.tags).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
		bindings.bind(root.cate).all((path, values) -> ExpressionProviderFactory.getPredicate(path, values));
	}

	Optional<Article> findOneBySlug(String slug);
}
