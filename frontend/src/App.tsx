import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route} from 'react-router-dom';

import { MainPage } from "./MainPage";
import { SearchResultPage } from "./SearchResultPage";
import { DetailPage } from "./DetailPage";
import { ProfilePage } from "./ProfilePage";
import { WatchlistPage } from "./WatchlistPage";
import { LoginPage } from "./LoginPage";
import { RegisterPage } from "./RegisterPage";
import { useAuthContext, AuthProvider } from './auth/AuthProvider';
import { UserContextProvider } from './user-profile/UserContextProvider';
import { setupInterceptors } from "./auth/apiAxios";
import PrivateRoute from './auth/PrivateRoute';

import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { Header } from "./components/Header";

const AppWrapper = () => {
	return (
		<AuthProvider>
			<UserContextProvider>
				<App />
			</UserContextProvider>
		</AuthProvider>
	);
};


const App = () => {
	const auth = useAuthContext();

	useEffect(() => {
		setupInterceptors(auth);
	}, [auth]);
	
	if (auth.isLoading) {
		return <div className="text-white">Loading session...</div>
	}

	return (
		<BrowserRouter>
			<div className="app">
				<Header />
				<Routes>
					<Route path="/" element={<MainPage />} />
					<Route path="/search" element={<SearchResultPage />} />
					<Route path="/movie/:id" element={<DetailPage type="movie" />} />
					<Route path="/tv/:id" element={<DetailPage type="series" />} />
					<Route path="/profile" element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
					<Route path="/watchlist" element={<PrivateRoute><WatchlistPage /></PrivateRoute>} />
					<Route path="/login" element={<LoginPage />} />
					<Route path="/register" element={<RegisterPage />} />
				</Routes>
				<ToastContainer 
					position="bottom-right"
					autoClose={4000}
					hideProgressBar={false}
					newestOnTop={false}
					closeOnClick
					rtl={false}
					pauseOnFocusLoss
					draggable
					pauseOnHover
					theme="dark"
				/>
			</div>
		</BrowserRouter>
	);
};

export default AppWrapper;