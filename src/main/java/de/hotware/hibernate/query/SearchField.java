package de.hotware.hibernate.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.hibernate.search.annotations.Analyzer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
/**
 * a method annotated with this will
 * be considered as a value to be searched
 * for when used with Searcher.search(...)
 * 
 * @author Martin Braun
 */
public @interface SearchField {

	public static final float DEFAULT_BOOST = Float.MIN_VALUE;
	public static final String DEFAULT_PROFILE = "DEFAULT_PROFILE";
	
	String profile() default "DEFAULT_PROFILE";

	/**
	 * the names of the fields to query from
	 */
	String[] fieldNames();

	/**
	 * will only be applied if the chosen queryType requires a needs Strings as
	 * values (i.e. StockQueryTypes.Phrase or StockQueryTypes.WildCardStarEnd)
	 */
	Class<?> stringBridge() default void.class;

	/**
	 * will only be applied if the chosen queryType requires a needs Strings as
	 * values (i.e. StockQueryTypes.Phrase or StockQueryTypes.WildCardStarEnd)
	 */
	Analyzer queryAnalyzer() default @Analyzer;

	/**
	 * how this SearchField will be combined with its parent query
	 */
	Junction topLevel() default Junction.MUST;

	/**
	 * how this SearchField will combine multiple values in the query (if there
	 * are any being returned from the queryBean or the Analyzer splits the
	 * original value into more)
	 */
	Junction betweenValues() default Junction.MUST;

	/**
	 * how this SeachField will combine multiple fields in the query
	 */
	Junction betweenFields() default Junction.MUST;

	/**
	 * the boost to apply
	 */
	float boost() default DEFAULT_BOOST;

	/**
	 * additional parameters to be passed to the QueryTypes handling function
	 */
	Parameter[] queryParameters() default {};

	/**
	 * the queryType to use
	 */
	Class<? extends QueryType> queryType() default StockQueryTypes.Term.class;

}