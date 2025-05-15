export type Predicate = {
	(value: any): boolean;
};

export const yearsDiffFromToday = (date: Date): number => {
	let today = new Date();
	if (date >= today) {
		return 0;
	}
	
	let yeardiff = today.getFullYear() - date.getFullYear();
	let monthDiff = today.getMonth() - date.getMonth();
	let dayDiff = today.getDate() - date.getDate();
	if (monthDiff < 0 || (monthDiff == 0 && dayDiff < 0))
		yeardiff--;
	return yeardiff;
}

const isIsoDateStringValid = (date: string) => !isNaN(new Date(date).getTime());

/**
 * Helper functions to create common validation Predicates
 */
const Predicates = {
	/**
	 * Creates a Predicate that checks if a value is not null or undefined
	 */
	notNull: (): Predicate => (value: any) => value != null,

	/**
	 * Creates a Predicate that checks if a string is not empty
	 */
	notEmpty: (): Predicate => (value: string) => value !== "",
	
	/**
	 * Creates a Predicate that checks if a string is not blank
	 */
	notBlank: (): Predicate => (value: string) => 
		value != null && Predicates.notEmpty()(value.trim()),

	/**
	 * Creates a Predicate that checks if a value is greater than the target
	 */
	greaterThan: (target: number): Predicate => (value: any) =>
		value != null && Number(value) > target,

	/**
	 * Creates a Predicate that checks if a value is less than the target
	 */
	lessThan: (target: number): Predicate => (value: any) =>
		value != null && Number(value) < target,

	/**
	 * Creates a Predicate that checks if a string matches a regex pattern
	 */
	matches: (pattern: RegExp): Predicate => (value: any) =>
		value != null && pattern.test(value.toString()),

	/**
	 * Creates a Predicate that checks if a value is one of the allowed values
	 */
	oneOf: (allowedValues: any[]): Predicate => (value: any) =>
		allowedValues.includes(value),

	/**
	 * Creates a Predicate that checks if a value is none of the disallowed values
	 */
	noneOf: (disallowedValues: any[]): Predicate => (value: any) =>
		!disallowedValues.includes(value),

	/**
	 * Creates a Predicate that checks if a string has minimum length
	 */
	minLength: (length: number): Predicate => (value: string) =>
		value != null && value.toString().length >= length,

	/**
	 * Creates a Predicate that checks if a string has maximum length
	 */
	maxLength: (length: number): Predicate => (value: string) =>
		value != null && value.toString().length <= length,

	/**
	 * Creates a Predicate that checks if the date represented as ISODateString is in the past
	 * a given minimum number of years from the present moment
	 */
	minAge: (years: number): Predicate => (isoDateString: string) =>
		isIsoDateStringValid(isoDateString) && yearsDiffFromToday(new Date(isoDateString)) >= years,
	
	/**
	 * Creates a Predicate that checks if the date represented as ISODateString is in the past
	 * a given maximum number of years from the present moment
	 */
	maxAge: (years: number): Predicate => (isoDateString: string) => 
		Predicates.isValidIsoDate()(isoDateString) && yearsDiffFromToday(new Date(isoDateString)) <= years,
	
	/**
	 * Creates a Predicate that checks if the date represented by the input ISODateString is in the past
	 */
	inPast: (): Predicate => (value: any) => Predicates.isValidIsoDate()(value) && new Date(value) < new Date(),
	
	/**
	 * Creates a Predicate that checks if a value is a valid ISODateString
	 */
	isValidIsoDate: (): Predicate => (value: string) => !isNaN(new Date(value).getTime()),
	
	/**
	 * Creates a Predicate that checks if a value equals the target
	 */
	equals: (target: any): Predicate => (value: any) => value == target,
	
	/**
	 * Creates a Predicate that checks if a value does not equal the target
	 */
	notEquals: (target: any): Predicate => (value: any) => value == target,
	
};

export default Predicates;