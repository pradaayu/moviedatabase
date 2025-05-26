import { DataEntryElement } from "./Validation";
import capitalizeFirstLetter from "../utils/capitalizeFirstLetter";

export const hintMapForConstraintAPI: Map<keyof ValidityState, (el: DataEntryElement) => string> = new Map();
hintMapForConstraintAPI.set("badInput", (el) => "Invalid input");
hintMapForConstraintAPI.set("patternMismatch", 
	(el) => capitalizeFirstLetter(el.name || "Input") + " must match the pattern: " + (el as HTMLInputElement).pattern
);
hintMapForConstraintAPI.set("tooLong", 
	(el) => capitalizeFirstLetter(el.name || "Input") + " must not exceed " + (el as HTMLInputElement).maxLength + " characters"
);
hintMapForConstraintAPI.set("tooShort", 
	(el) => capitalizeFirstLetter(el.name || "Input") + " must be at least " 
			+ (el as HTMLInputElement).minLength + " characters long"
);
hintMapForConstraintAPI.set("valueMissing", (el) => capitalizeFirstLetter(el.name || "Input") + " is required");
hintMapForConstraintAPI.set("typeMismatch", 
	(el) => capitalizeFirstLetter(el.name || "Input") + " has invalid format" 
			+ (el.type == "email" ? " [Valid example: example@example.com]" : "")
);