package com.github.modlauncher.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonVal {
	JsonType type() default JsonType.Class;
	Class<?> classType() default Object.class;
	boolean required() default false;
	
	/**
	 * Tells the JsonLoader to assign the value of the key. (i.e. {"key": {"sub_key": "is something"}})
	 */
	boolean useObjectKey() default false;
}
