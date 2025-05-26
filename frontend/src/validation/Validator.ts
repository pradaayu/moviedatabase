import { Predicate } from "./Validators";
import { DataEntryElement } from "./Validation";

/**
 * Simple validator class that stores rules and validates fields against them
 */
export default class Validator {
	private rules: Rule[] = [];
	private matchingRules: Rule[] = [];
	private nonMatchingRules: Rule[] = [];

	/**
	 * Add a new validation rule with the provided field, predicate and hint
	 * @param field The field name to validate
	 * @param predicate The validation function
	 * @param hint The error message to display if validation fails
	 */
	public add(field: string, predicate: Predicate, hint: Hint, validityType?: keyof ValidityState) {
		if (validityType && this.findByFieldAndValidityType(field, validityType).length > 0) {
			throw new Error("Rule already exists for field '" + field + "' and validityType '" + validityType + "'");
		}
		let r: Rule = { field, predicate, hint };
		if (validityType) {
			r.validityType = validityType;
		}
		this.addRule(r);
	}
	
	public findByFieldAndValidityType(field: string, validityType: keyof ValidityState): Rule[] {
		return this.rules.filter(r => r.field == field && r.validityType == validityType);	
	}
	
	/**
	 * Add a new validation rule
	 * @param rule Rule
	 */
	public addRule(rule: Rule) {
		let { field, validityType } = rule;
		if (validityType && this.findByFieldAndValidityType(field, validityType).length > 0) {
			throw new Error("Rule already exists for field '" + field + "' and validityType '" + validityType + "'");
		}
		this.rules.push(rule);
	}
	
//	/**
//	 * Add a list of validation rules
//	 * @param rules Array containing Rule elements
//	 */
//	public addRules(rules: Rule[]) {
//		this.rules = this.rules.concat(rules);
//	}
	
	/**
	 * Removes the first occurence of the specified validation rule
	 * @param rule Rule
	 * @returns true if rule exists, false otherwise
	 */
	public removeRule(rule: Rule): boolean {
		let idx = this.rules.indexOf(rule);
		if (idx == -1) {
			return false;
		}
		this.rules.splice(idx, 1);
		return true;
	}
	
	/**
	 * Reset all validator's properties
	 */
	public clear() {
		this.matchingRules = [];
		this.nonMatchingRules = [];
		this.rules = [];
	}

	/**
	 * Validate a field against all relevant rules
	 * @param field The field name to validate
	 * @param value The value to validate
	 */
	public validate(field: string, value: any): boolean {
		this.matchingRules = this.findMatchingRules(field, value);
		this.nonMatchingRules = this.findNonMatchingRules(field, value);
		return this.nonMatchingRules.length == 0;
	}

	/**
	 * Validates all fields in the provided object
	 * @param formData Object with fields to validate
	 */
	public validateAll(formData: { [key: Rule["field"]]: any }): boolean {
		this.matchingRules = this.rules.filter(r => formData.hasOwnProperty(r.field) && r.predicate(formData[r.field]));
		this.nonMatchingRules = this.rules.filter(r => formData.hasOwnProperty(r.field) && !r.predicate(formData[r.field]));
		return this.nonMatchingRules.length == 0;
	}
	
	/**
	 * Finds all rules for a specific field that pass validation
	 * @param field The field name to check
	 * @param value The value to validate against the rules
	 * @returns An array of rules that passed validation
	 */
	public findMatchingRules(field: string, value: any): Rule[] {
		let relevant = this.findRevelantRules(field, value);
		return relevant.filter(r => r.field == field && r.predicate(value));
	}
	
	/**
	 * Finds all rules for a specific field that fail validation
	 * @param field The field name to check
	 * @param value The value to validate against the rules
	 * @returns An array of rules that failed validation
	 */
	public findNonMatchingRules(field: string, value: any): Rule[] {
		let relevant = this.findRevelantRules(field, value);
		return relevant.filter(r => r.field == field && !r.predicate(value));
	}
	
	private findRevelantRules(field: string, value: any): Rule[] {
		return value == "" ? this.findByFieldAndValidityType(field, "valueMissing") : this.rules;
	}
	
	/**
	 * Gets rules that passed validation from the last validation operation
	 * @param field Optional field name to filter results
	 * @returns An array of matching rules, filtered by field name if provided
	 */
	public getMatchingRules(field?: string): Rule[] {
		return field ? this.matchingRules.filter(r => r.field == field) : this.matchingRules;
	}
	
	/**
	 * Gets rules that failed validation from the last validation operation
	 * @param field Optional field name to filter results
	 * @returns An array of non-matching rules, filtered by field name if provided
	 */
	public getNonMatchingRules(field?: string): Rule[] {
		return field ? this.nonMatchingRules.filter(r => r.field == field) : this.nonMatchingRules;
	}
	
	/**
	 * Gets all validation rules registered with this validator
	 * @returns The complete array of validation rules
	 */
	public getRules(): Rule[] {
		return this.rules;
	}
}

type Hint = (el: DataEntryElement) => string

export type Rule = {
	field: string,
	validityType?: keyof ValidityState,
	predicate: Predicate,
	hint: Hint,
}