package io.spring.application.user;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Constraint(validatedBy = UpdateUserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UpdateUserConstraint {
	String message() default "invalid update param";

	Class<?>[] groups() default {};

	Class<?>[] payload() default {};
}
