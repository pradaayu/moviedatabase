import React, { useRef, useState, useEffect } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import { useAuthContext } from "./auth/AuthProvider";
import { AxiosError } from "axios";
import { ApiResponse } from "./types";


export function LoginPage() {
	const isMounted = useRef(true);
	useEffect(() => {
		isMounted.current = true;
		return () => {
			isMounted.current = false;
		};
	}, []);
	
	const { login } = useAuthContext();
	const navigate = useNavigate();
	const location = useLocation();
	
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [showPassword, setShowPassword] = useState(false);
	const [authError, setAuthError] = useState("");
	const [idle, setIdle] = useState(true);

	const handleLogin = async () => {
		setAuthError("");

		try {
			await login(email, password);
			if (isMounted.current) {
				let url = location.state?.from ?? "/";
				if (url.endsWith("register") || url.endsWith("login")) {
					url = "/"
				}
				navigate(url); // After successful login, navigate to the previous page (if not '/register' nor '/login')	
			}
		} catch (err) {
			if (isMounted.current) {
				let loginErr = err as AxiosError & { response: { data: string | ApiResponse } };
				let msg = typeof loginErr.response.data != "string" ? loginErr.response.data.message : "⚠️ Network error.";
				setAuthError(msg);	
			}
		}
	};

	return (
		<div className="min-h-screen bg-black flex items-center justify-center px-4">
			<div className="w-full max-w-md bg-[#1a1a1a] rounded-lg shadow-lg p-8">
				<div className="flex justify-center mb-6">
					<h1 className="text-3xl font-bold text-white">MovieDB</h1>
				</div>

				<h2 className="text-2xl font-semibold text-white text-center mb-6">Sign In</h2>

				<form onSubmit={handleLogin} className="space-y-4" noValidate>
					<div>
						<label htmlFor="email" className="block text-sm font-medium text-gray-300 mb-2">
							Email
						</label>
						<div className="relative">
							<input
								type="email"
								id="email"
								name="name"
								value={email}
								className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
								required
								placeholder="Enter your email"
								onChange={e => setEmail(e.target.value)}
							/>
							<svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 absolute right-3 top-1/2 -translate-y-1/2 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
								<path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m9-9a9 9 0 00-9-9" />
							</svg>
						</div>
					</div>

					<div>
						<label htmlFor="password" className="block text-sm font-medium text-gray-300 mb-2">
							Password
						</label>
						<div className="relative">
							<input
								type={showPassword ? "text" : "password"}
								id="password"
								name="password"
								value={password}
								className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
								required
								placeholder="Enter your password"
								onChange={e => setPassword(e.target.value)}
							/>
							<button type="button" className="h-5 w-5 absolute right-3 top-1/2 -translate-y-1/2 text-gray-400"
								onClick={() => setShowPassword(!showPassword)}>
								<span className="material-symbols-outlined">
									{showPassword ? "visibility" : "visibility_off"}
								</span>
							</button>
						</div>
					</div>

					<button type="button" aria-label="login" disabled={!idle}
						className="w-full bg-lime-500 text-black py-3 rounded-md font-semibold hover:bg-lime-600 transition duration-300"
						onClick={e => {
							setIdle(false);
							handleLogin().finally(() => isMounted.current && setIdle(true));
						}}>
					SIGN IN </button>
				</form>

				{authError && (
					<div style={{ marginTop: "1rem", color: "#f00" }}>
						{authError}
					</div>
				)}

				<div className="text-center mt-6">
					<Link
						to="/register"
						className="text-gray-400 hover:text-white transition duration-300"
					>
						DON'T HAVE AN ACCOUNT?
					</Link>
				</div>
			</div>
		</div>
	);
}