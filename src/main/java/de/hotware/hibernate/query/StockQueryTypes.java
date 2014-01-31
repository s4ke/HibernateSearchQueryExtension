/*  
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.*
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 *   (C) Martin Braun 2014
 */
package de.hotware.hibernate.query;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.PhraseMatchingContext;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermMatchingContext;

public class StockQueryTypes {

	private StockQueryTypes() {
		throw new AssertionError("can't touch this!");
	}

	public static class Term implements QueryType {

		@SuppressWarnings("rawtypes")
		@Override
		public Query query(QueryBuilder queryBuilder, String pathToField,
				SearchField searchField, Object value) {
			BooleanJunction<BooleanJunction> junction = queryBuilder.bool();
			for (String fieldName : searchField.fieldNames()) {
				TermMatchingContext context = queryBuilder.keyword().onField(
						pathToField + fieldName);
				if (searchField.boost() != SearchField.DEFAULT_BOOST) {
					context = context.boostedTo(searchField.boost());
				}
				Query query = context.matching(value).createQuery();
				searchField.betweenFields().combine(junction, query);
			}
			return junction.createQuery();
		}

		@Override
		public boolean valueAsString() {
			return false;
		}

	}

	public static class WildcardStarEnd implements QueryType {

		@SuppressWarnings("rawtypes")
		@Override
		public Query query(QueryBuilder queryBuilder, String pathToField,
				SearchField searchField, Object value) {
			BooleanJunction<BooleanJunction> junction = queryBuilder.bool();
			for (String fieldName : searchField.fieldNames()) {
				TermMatchingContext context = queryBuilder.keyword().wildcard()
						.onField(pathToField + fieldName);
				if (searchField.boost() != SearchField.DEFAULT_BOOST) {
					context = context.boostedTo(searchField.boost());
				}
				// ignore the fieldbridge and analyzer because we already have
				// the value as a string
				context = context.ignoreFieldBridge();
				context = context.ignoreAnalyzer();
				Query query = context.matching(
						String.format("%s*", (String) value)).createQuery();
				searchField.betweenFields().combine(junction, query);
			}
			return junction.createQuery();
		}

		@Override
		public boolean valueAsString() {
			return true;
		}

	}

	public static class Phrase implements QueryType {

		public static final String SLOP_KEY = "slop";
		public static final int DEFAULT_SLOP = 0;

		@SuppressWarnings("rawtypes")
		@Override
		public Query query(QueryBuilder queryBuilder, String pathToField,
				SearchField searchField, Object value) {
			BooleanJunction<BooleanJunction> junction = queryBuilder.bool();
			for (String fieldName : searchField.fieldNames()) {
				int slop = DEFAULT_SLOP;
				for (Parameter cur : searchField.queryParameters()) {
					if (cur.name().equals(SLOP_KEY)) {
						slop = Integer.parseInt(cur.value());
						break;
					}
				}
				PhraseMatchingContext context = queryBuilder.phrase()
						.withSlop(slop).onField(pathToField + fieldName);
				if (searchField.boost() != SearchField.DEFAULT_BOOST) {
					context = context.boostedTo(searchField.boost());
				}
				// ignore the fieldbridge and analyzer because we already have
				// the value as a string
				context = context.ignoreFieldBridge();
				context = context.ignoreAnalyzer();
				Query query = context.sentence((String) value).createQuery();
				searchField.betweenFields().combine(junction, query);
			}
			return junction.createQuery();
		}

		@Override
		public boolean valueAsString() {
			return true;
		}

	};

}