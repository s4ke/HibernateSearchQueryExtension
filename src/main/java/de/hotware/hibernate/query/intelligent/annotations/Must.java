package de.hotware.hibernate.query.intelligent.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface Must {
	
	SearchField value() default @SearchField;
	
	String subQuery() default "";
	
	boolean not() default false;

}
