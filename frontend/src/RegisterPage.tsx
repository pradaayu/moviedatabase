import React, { useState, useEffect, useMemo, useRef } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuthContext } from "./auth/AuthProvider";
import zxcvbn from "zxcvbn"; // TODO!

import Predicates, { Predicate, yearsDiffFromToday } from "./validation/Predicates";
import { DataEntryElement, validateFormField } from "./validation/Validation";
import Validator, { Rule } from "./validation/Validator";
import Hint from "./validation/Hint";
import { hintMapForConstraintAPI } from "./validation/hintsMapForConstraintAPI";

import emojis from "./assets/emojis.json";


type RegStatus = "idle" | "pending" | "success" | "error";
export type User = {
	avatar: string,
	name: string,
	dateOfBirth?: string
}
export type UserCredential = {
	email: string,
	password: string,
}
type RegForm = User & UserCredential;

type RegFormRule = (Rule & { field: keyof RegForm });
type FieldError = Partial<Record<keyof RegForm, string[] | undefined>>;
type ErrForm = Map<DataEntryElement, string[]>;

const regFormRules: RegFormRule[] = [
	{ 
		field: "name", 
		predicate: Predicates.maxLength(50), 
		hint: (e) => "Name too long", 
		validityType: "tooLong" 
	},
	{ 
		field: "name", 
		predicate: Predicates.notBlank(), 
		hint: (e) => "Enter a valid, non-blank name" 
	},
	{ 
		field: "name", 
		predicate: (v: string) => Predicates.notNull()(v) && Predicates.notEmpty()(v), 
		hint: (e) => "Name is required", 
		validityType: "valueMissing" 
	},
	{ 
		field: "avatar", 
		predicate: (v: string) => Predicates.notNull()(v) && Predicates.notEmpty()(v), 
		hint: (e) => "Avatar is required", 
		validityType: "valueMissing" 
	},
	{ 
		field: "email", 
		predicate: Predicates.notEmpty(), 
		hint: (e) => "Email is required", 
		validityType: "valueMissing" 
	},
	{ 
		field: "email", 
		predicate: Predicates.matches(/^[^\s@]+@[^\s@]+\.[^\s@]+$/), 
		hint: (e) => "Invalid format [Valid example: example@example.com]",
		validityType: "typeMismatch" 
	},
	{ 
		field: "dateOfBirth", 
		predicate: function(v: string) {
			this.hint = (e) => "";
			if (!v) {
				return true;
			}
			if (!Predicates.inPast()(v)) {
				this.hint = (e) => "Invalid date";
				return false;
			}
			if (!Predicates.minAge(13)(v)) {
				this.hint = (e) => "Minimum age 13 y.o. (Your age: " + yearsDiffFromToday(new Date(v)) + ")";
				return false;
			}
			if (!Predicates.maxAge(120)(v)) {
				this.hint = (e) => "Maximum age 120 y.o. (Your age: " + yearsDiffFromToday(new Date(v)) + ")";
				return false;
			}
			return true;
		}, 
		hint: (e) => "" 
	},
	{ 
		field: "password",
		predicate: Predicates.notEmpty(), 
		hint: (e) => "Password is required",
		validityType: "valueMissing"
	},
	{
		field: "password",
		predicate: Predicates.minLength(4),
		hint: (e) => "Password must be at least 4 characters long",
		validityType: "tooShort"
	},
	{
		field: "password",
		predicate: Predicates.maxLength(18),
		hint: (e) => "Password must not exceed 20 characters",
		validityType: "tooLong"
	}
	 
];

export function RegisterPage() {
	const { login } = useAuthContext();
	const navigate = useNavigate();
	const [showPassword, setShowPassword] = useState(false);
	const [fieldErrors, setFieldErrors] = useState({} as Partial<Record<keyof RegForm, string[]>>);
	const [queryError, setQueryError] = useState("");
	const [status, setStatus] = useState("idle" as RegStatus);

	const form: React.MutableRefObject<HTMLFormElement|null> = useRef(null);
	const formElements: React.MutableRefObject<DataEntryElement[]> = useRef([]);
	const [formData, setFormData] = useState({
		avatar: "",
		email: "",
		name: "",
		password: "",
		dateOfBirth: ""
	} as RegForm);
	useEffect(() => {
		for (let el of form.current?.elements || []) {
			el.hasAttribute("name") && formElements.current.push(el as DataEntryElement);
		}
	}, []);
	
	const validator: Validator = useMemo(() => {
		let v = new Validator();
		for (let r of regFormRules) {
			try {
				v.addRule(r);
			} catch (err) {
				console.error(err);
			}
		}
		return v;
	}, []);

	const validate = (els: DataEntryElement[]): boolean => {
		let isValid = true;
		let errors = { ...fieldErrors };

		for (let el of els) {
			let [validationResult, hints] = validateFormField(el, validator, hintMapForConstraintAPI);
			isValid &&= validationResult;
			errors[el.name as keyof RegForm] = hints;
		}

		setFieldErrors(errors);
		return isValid;
	}

	const handleRegister = async (e: React.FormEvent) => {
		e.preventDefault();
		if (status == "pending") {
			return;
		}

		let isValid = validate(formElements.current);
		if (!isValid) {
			return;
		}

		setStatus("pending");
		setQueryError("");
		setFieldErrors({});

		try {
			let res = await fetch("/api/auth/register", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify(formData),
			});

			if (res.status === 201) {
				// Registered successfully, now auto-login
				try {
					// Automatically login the user after registration
					await login(formData.email, formData.password);
					setStatus("success");
					navigate("/");
				} catch (loginErr) {
					setQueryError("Registered, but auto-login failed. Please log in manually.");
				}
			} else {
				// Handle field-specific or general errors
				let data = await res.json();
				setStatus("error");
				if (data.code === "USER_EXISTS") {
					setFieldErrors({ email: ["This email is already registered."] });
				} else {
					setQueryError(data.message || "Registration failed.");
				}
			}
		} catch (err) {
			setStatus("error");
			setQueryError("⚠️ Network error.");
		}

	};
	
	console.log((formData));

	return (
		<div className="min-h-screen bg-black flex items-center justify-center px-4">
			<div className="w-full max-w-md bg-[#1a1a1a] rounded-lg shadow-lg p-8">
				<div className="flex justify-center mb-6">
					<h1 className="text-3xl font-bold text-white">MovieDB</h1>
				</div>

				<h2 className="text-2xl font-semibold text-white text-center mb-6">Sign Up</h2>

				<form className="space-y-4" ref={form} onSubmit={handleRegister} noValidate>
					<div>
						<label className="block text-sm font-medium text-gray-300 mb-2" htmlFor="avatar">
							Select your profile avatar
						</label>
						<div className="grid grid-cols-5 gap-3 mb-4">
							{emojis.map((emoji) => (
								<label
									key={emoji}
									className={
										`text-4xl w-12 h-12 rounded-full flex items-center 
										justify-center cursor-pointer
				                  		${formData.avatar== emoji ? 'ring-2 ring-lime-500' : ''} hover:bg-gray-700`
									}
								>
									<input
										type="radio"
										name="avatar"
										className="sr-only"
										value={emoji}
										onChange={e => { setFormData({...formData, avatar: emoji }); validate([e.target]) }}
									/>
									{emoji}
								</label>
							))}
						</div>
						{(fieldErrors.avatar || []).map((err) => <div><Hint msg={err} /></div>)}
					</div>

					<div>
						<label htmlFor="name" className="block text-sm font-medium text-gray-300 mb-2">
							Name
						</label>
						<div className="relative">
							<input
								type="text"
								id="name"
								name="name"
								value={formData.name}
								className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
								placeholder="Enter your name"
								onChange={e => { setFormData({ ...formData, name: e.target.value }); validate([e.target]) }}
							/>
							<svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 absolute right-3 top-1/2 -translate-y-1/2 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
								<path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a8 8 0 00-8 8h16a8 8 0 00-8-8z" />
							</svg>
						</div>
						{(fieldErrors.name || []).map(err => <div><Hint msg={err} /></div>)}
					</div>

					<div>
						<label htmlFor="dateOfBirth" className="block text-sm font-medium text-gray-300 mb-2">
							Date of birth
						</label>
						<div className="relative">
							<input
								type="date"
								id="dateOfBirth"
								name="dateOfBirth"
								value={formData.dateOfBirth}
								onChange={e => { 
									setFormData({...formData, dateOfBirth: e.target.value }); 
									validate([e.target]) 
								}}
								className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
							/>
						</div>
						{(fieldErrors.dateOfBirth || []).map(err => <div><Hint msg={err} /></div>)}
					</div>

					<div>
						<label htmlFor="email" className="block text-sm font-medium text-gray-300 mb-2">
							Email
						</label>
						<div className="relative">
							<input
								type="email"
								id="email"
								name="email"
								value={formData.email}
								onChange={e => {
									setFormData({ ...formData, email: e.target.value });
									fieldErrors.email && validate([e.target]) 
								}}
								onBlur={e => validate([e.target])}
								className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"

								placeholder="Enter your email"
								autoComplete="email"
							/>
							<svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 absolute right-3 top-1/2 -translate-y-1/2 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
								<path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m9-9a9 9 0 00-9-9" />
							</svg>
						</div>
						{(fieldErrors.email || []).map(err => <div><Hint msg={err} /></div>)}
					</div>

					{/* We opt for a 'mask/unmask password' strategy. See:
						https://uxmovement.com/forms/why-the-confirm-password-field-must-die/
						https://security.stackexchange.com/questions/29890/from-a-security-standpoint-should-users-be-asked-to-confirm-their-password-when
						https://uxpatterns.dev/en/patterns/forms/password?utm_source=chatgpt.com
					*/}
					<div>
						<label htmlFor="password" className="block text-sm font-medium text-gray-300 mb-2">
							Password
						</label>
						<div className="relative">
							<input className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
								type={showPassword ? "text" : "password"}
								id="password"
								name="password"
								value={formData.password}
								placeholder="Enter your password"
								onChange={e => { setFormData({ ...formData, password: e.target.value }); validate([e.target]) }}
							/>
							<button type="button" className="h-5 w-5 absolute right-3 top-1/2 -translate-y-1/2 text-gray-400"
								onClick={() => setShowPassword(!showPassword)}>
								<span className="material-symbols-outlined">
									{showPassword ? "visibility" : "visibility_off"}
								</span>
							</button>
						</div>
						{(fieldErrors.password || []).map(err => <div><Hint msg={err} /></div>)}
					</div>

					<button
						type="submit"
						className="w-full bg-lime-500 text-black py-3 rounded-md font-semibold hover:bg-lime-600 transition duration-300"
						aria-label="Submit registration form"
					>
						SIGN UP
					</button>
				</form>

				{queryError && (
					<div style={{ marginTop: "1rem", color: status == "error" ? "red" : "green" }}>
						{queryError}
					</div>
				)}

				<div className="text-center mt-6">
					<Link
						to="/login"
						className="text-gray-400 hover:text-white transition duration-300"
					>
						ALREADY HAVE AN ACCOUNT?
					</Link>
				</div>
			</div>
		</div>
	);
}
