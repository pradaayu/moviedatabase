// UserProvider.tsx
import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useAuthContext } from "../auth/AuthProvider";
import { User } from "../RegisterPage";

type UserContextType = {
	user: User | null
	updateUser: (user: User) => void
}

const UserContext = createContext<UserContextType | null>(null);

type UserProfileProviderProps = {
	children: ReactNode;
}

export const UserContextProvider: React.FC<UserProfileProviderProps> = ({ children }) => {
	const { accessToken } = useAuthContext();
	const [user, setUser] = useState(null);
	
	const updateUser = async(user: User) => {
		setUser(null);
	}

	useEffect(() => {
		(async () => {
			if (accessToken) {
				console.log("fetching user...");
				try {
					const res = await fetch('/api/user/profile', {
						headers: {
							Authorization: `Bearer ${accessToken}`
						}
					});
					const data = await res.json();
					console.log("user data", data);
					setUser(data.data);
				} catch (err) {
					console.error("Failed to fetch user profile:", err);
					setUser(null);
				}
			} else {
				setUser(null);
			}
		})();
	}, [accessToken]);

	return (
		<UserContext.Provider value={{ user, updateUser }}>
			{children}
		</UserContext.Provider>
	);
};

export const useUserContext = (): UserContextType => {
	const context = useContext(UserContext);
	if (!context) {
		throw new Error('useUserContext must be used within an UserProfileProvider');
	}
	return context;
}
