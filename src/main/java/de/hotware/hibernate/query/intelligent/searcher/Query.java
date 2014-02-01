package de.hotware.hibernate.query.intelligent.searcher;

import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

public interface Query {

	public boolean constructQuery(
			@SuppressWarnings("rawtypes") BooleanJunction<BooleanJunction> junction,
			QueryBuilder queryBuilder, Object bean, CachedInfo cachedInfo);

}
