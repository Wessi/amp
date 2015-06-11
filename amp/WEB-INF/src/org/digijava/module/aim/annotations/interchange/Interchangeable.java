package org.digijava.module.aim.annotations.interchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Interchangeable {
	String fieldTitle();
	boolean multipleValues() default false;
	boolean importable() default true;
	boolean recursive() default false;
}
