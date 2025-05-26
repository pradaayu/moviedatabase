import Validator, { Rule } from "./Validator";

const validityProperties: Array<keyof ValidityState> = [
	"badInput",
	"patternMismatch",
	"rangeOverflow",
	"rangeUnderflow",
	"tooLong",
	"tooShort",
	"typeMismatch",
	"valueMissing"
];

export type DataEntryElement = HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement;
type ValidationResult = Map<keyof ValidityState, boolean>;

export const validateWithBuiltInApi = (el: DataEntryElement): ValidationResult => {
	let result: ValidationResult = new Map();
	validityProperties.forEach(prop => {
		result.set(prop, el.validity[prop] ?? false);
	});
	return result;
}

type FieldValidationResult = Map<Rule | keyof ValidityState, boolean>;

export const getValidationStateForFormField = (el: DataEntryElement, validator: Validator): FieldValidationResult => {
	let result: FieldValidationResult = new Map();

	let form = el.closest("form") as HTMLFormElement;
	let value = form ? new FormData(form).get(el.name) : el.value;

	let fail = validator.findNonMatchingRules(el.name, value);
	let ok = validator.findMatchingRules(el.name, value);
	let resultFromApi = validateWithBuiltInApi(el);

	// Build FieldValidationResult
	for (let r of fail) {
		r.validityType && resultFromApi.delete(r.validityType); // override error result from API
		result.set(r, false);
	}
	for (let [k, v] of resultFromApi) {
		result.set(k, v);
	}
	for (let r of ok) {
		if (!r.validityType || !result.has(r.validityType)) {
			result.set(r, true);
		}
	}

	return result;
}

type FieldValidationResultWithHints = [isValid: boolean, hints: string[]];
type ApiHintMap = Map<keyof ValidityState, (el: DataEntryElement) => string>;

export const validateFormField = (el: DataEntryElement, validator: Validator, hintMap: ApiHintMap): FieldValidationResultWithHints => {
	let hints = [];
	let form = el.closest("form") as HTMLFormElement;
	let value = form ? new FormData(form).get(el.name) : el.value;

	let resultFromApi = validateWithBuiltInApi(el);
	let fail = validator.findNonMatchingRules(el.name, value);
	let isValid = fail.length == 0;

	// Gather error hints from both results
	for (let r of fail) {
		r.validityType && resultFromApi.delete(r.validityType); // override error result from API
		hints.push(r.hint.call(null,el));
	}
	for (let [k, v] of resultFromApi) {
		isValid &&= !v;
		v && hints.push(hintMap.get(k)?.call(null,el) ?? k);
	}
	
	return [isValid, [...new Set(hints)]]; // Ensure all hints are unique
}

export default validateWithBuiltInApi;