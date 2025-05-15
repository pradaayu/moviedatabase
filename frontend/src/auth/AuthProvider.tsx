import React, { createContext, useState, useEffect, useContext, useRef, ReactNode } from 'react';
import { useNotifier } from '../hooks/useNotifier';
import axios, { AxiosError } from 'axios';
import SessionTimeoutModal from '../components/SessionTimeoutModal';

// For monitoring user activity => impose an idle timeout. See also the complete guide on requirements:
// https://cheatsheetseries.owasp.org/cheatsheets/Session_Management_Cheat_Sheet.html
const IDLE_WARNING_TIME = 4 * 60 * 1000; // 4 min
const TIMEOUT_TIME = 30 * 1000;          // 30 sec countdown
const activityEvents = ['mousemove', 'mousedown', 'keydown', 'touchstart'];

type AuthContextType = {
	accessToken: string | null;
	login: (email: string, password: string) => Promise<void>;
	logout: () => Promise<void>;
	refresh: () => Promise<string>;
	isLoading: boolean;
	isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

type AuthProviderProps = {
	children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
	let notify = useNotifier();

	let [accessToken, setAccessToken] = useState<string | null>(null);
	let [isLoading, setIsLoading] = useState(true); // for auto-refresh on app (re-)load

	let [showModal, setShowModal] = useState(false);
	let [countdown, setCountdown] = useState(TIMEOUT_TIME / 1000);
	let lastActivityRef = useRef(Date.now());
	let countdownIntervalRef = useRef<NodeJS.Timeout | null>(null);
	let idleCheckIntervalRef = useRef<number | null>(null);

	// Setup Axios instance with token
	useEffect(() => {
		axios.defaults.headers.common['Authorization'] = accessToken ? `Bearer ${accessToken}` : '';
	}, [accessToken]);

	let login = async (email: string, password: string) => {
		try {
			let res = await axios.post('/api/auth/login', { email, password });
			setAccessToken(res.data.data.accessToken);
			notify.success("Logged in successfully");
		} catch (err) {
			notify.error("Login failed");
			throw err;
		}
	};

	let logout = async () => {
		try {
			await axios.post('/api/auth/logout', {}, { withCredentials: true });
		} finally {
			setAccessToken(null);
			notify.info("Logged out");
		}
	};

	const refresh = async () => {
		try {
			let res = await axios.post('/api/auth/refresh', {}, { withCredentials: true });
			setAccessToken(res.data.data.accessToken);
			return res.data.data.accessToken;
		} catch (err) {
			let axiosError = err as AxiosError<{ message: string }>;
			let status = axiosError.response?.status;
			let errorMessage = axiosError.response?.data?.message;

			// Only show session expired toast if there was a real session (i.e. refresh token existed)
			if (status == 401 && errorMessage == "Invalid or expired refresh token") {
				notify.error("Session expired. Please log in again.");
			}
			
			if (!status) {
				notify.error("Unable to connect to the server.");
			}

			setAccessToken(null);
			throw err;
		}
	};

	// Try to refresh on app (re-)load
	useEffect(() => {
		let tryAutoRefresh = async () => {
			try {
				await refresh();
			} catch (err) {
				setAccessToken(null); // not authenticated
			} finally {
				setIsLoading(false);
			}
		};

		tryAutoRefresh();
	}, []);

	// Reset activity and optionally ping backend
	const resetActivity = async (ping = true) => {
		lastActivityRef.current = Date.now();
		if (showModal) {
			!showModal && stopCountdown();
			if (ping) {
				try {
					await axios.post('/api/auth/ping', {}, { withCredentials: true });
				} catch (err) {
					console.error('Ping failed', err);
				}
			}	
		}
	};

	const startCountdown = () => {
		let timeLeft = TIMEOUT_TIME / 1000;
		setCountdown(timeLeft);
		setShowModal(true);

		countdownIntervalRef.current = setInterval(() => {
			timeLeft--;
			setCountdown(timeLeft);
			if (timeLeft <= 0) {
				stopCountdown();
				logout();
			}
		}, 1000);
	};

	const stopCountdown = () => {
		setShowModal(false);
		if (countdownIntervalRef.current) {
			clearInterval(countdownIntervalRef.current);
			countdownIntervalRef.current = null;
		}
		setCountdown(TIMEOUT_TIME / 1000);
	};

	// Track inactivity and show a modal after a configurable time
	// Ping the backend (/api/auth/ping) to extend session if the user clicks "Stay Logged In"
	// Log the user out automatically if they don't respond in time
	useEffect(() => {
		let checkInactivity = () => {
			if (!accessToken) return;
			let now = Date.now();
			if (!showModal && now - lastActivityRef.current > IDLE_WARNING_TIME) {
				startCountdown();
			}
		};

		idleCheckIntervalRef.current = window.setInterval(checkInactivity, 1000);

		activityEvents.forEach((event) => {
			window.addEventListener(event, () => resetActivity(false));
		});

		return () => {
			if (idleCheckIntervalRef.current) {
				clearInterval(idleCheckIntervalRef.current);
			} 
			activityEvents.forEach((event) =>
				window.removeEventListener(event, () => resetActivity(false))
			);
		};
	}, [accessToken, showModal]);

	return (
		<AuthContext.Provider value={{ accessToken, login, logout, refresh, isLoading, isAuthenticated: !!accessToken }}>
			{children}
			<SessionTimeoutModal
				visible={showModal}
				countdown={countdown}
				onStayLoggedIn={() => { resetActivity(true); setShowModal(false); } }
				onLogout={() => logout().then(() => setShowModal(false))}
			/>
		</AuthContext.Provider>
	);
};

export const useAuthContext = (): AuthContextType => {
	let context = useContext(AuthContext);
	if (!context) {
		throw new Error('useAuthContext must be used within an AuthProvider');
	}
	return context;
};
