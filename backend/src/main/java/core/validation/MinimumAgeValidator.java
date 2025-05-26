package core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class MinimumAgeValidator implements ConstraintValidator<MinimumAge, LocalDate> {

	private int minimumAge;

	@Override
	public void initialize(MinimumAge constraintAnnotation) {
		this.minimumAge = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
		if (dob == null)
			return true;
		LocalDate today = LocalDate.now();
		return Period.between(dob, today).getYears() >= minimumAge;
	}
}
