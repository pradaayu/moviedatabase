import React, { useState, useEffect } from 'react';
import { BrowserRouter, Routes, Route, Link, useNavigate } from 'react-router-dom';

import { MainPage } from "./MainPage";
import { SearchResultPage } from "./SearchResultPage";
import { DetailPage } from "./DetailPage";
import { ProfilePage } from "./ProfilePage";
import { WatchlistPage } from "./WatchlistPage";
import { LoginPage } from "./LoginPage";
import { RegisterPage } from "./RegisterPage";

import { Header } from "./components/Header";

const App = () => {
  const [message, setMessage] = useState('Loading...');

  useEffect(() => {
    // Fetch message from backend
//    fetch('/api/hello')
//      .then(response => response.json())
//      .then(data => setMessage(data.message))
//      .catch(error => setMessage('Error connecting to backend'));
	  // If you want to load initial data when the component mounts
  }, []);

	return (
		<BrowserRouter>
		  <div className="app">
		    <Header />
		    <Routes>
		      <Route path="/" element={<MainPage />} />
		      <Route path="/search" element={<SearchResultPage />} />
		      <Route path="/movie/:id" element={<DetailPage type="movie" />} />
		      <Route path="/tv/:id" element={<DetailPage type="series" />} />
		      <Route path="/profile" element={<ProfilePage />} />
		      <Route path="/watchlist" element={<WatchlistPage />} />
		      <Route path="/login" element={<LoginPage />} />
		      <Route path="/register" element={<RegisterPage />} />
		    </Routes>
		  </div>
		</BrowserRouter>
	);
};

export default App;