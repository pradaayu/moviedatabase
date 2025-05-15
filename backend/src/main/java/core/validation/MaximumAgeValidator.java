package core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MaximumAgeValidator implements ConstraintValidator<MaximumAge, LocalDate> {

	private int maxAge;

	@Override
	public void initialize(MaximumAge constraintAnnotation) {
		this.maxAge = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
		if (dob == null)
			return true;
		LocalDate today = LocalDate.now();
		return Period.between(dob, today).getYears() <= maxAge;
	}
}
