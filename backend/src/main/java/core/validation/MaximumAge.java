package core.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MaximumAgeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MaximumAge {
	String message() default "User must be at least {value} years old";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int value(); // the maximum age
}
