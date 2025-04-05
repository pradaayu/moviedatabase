import React, { useState, useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';

export function WatchlistPage() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);
  const [watchlist, setWatchlist] = useState([]);

  useEffect(() => {
    // Check if user is logged in
    const userData = JSON.stringify({
			username: "meowdynasty",
			email: "abc@gmail.com",
		}) // localStorage.getItem('user');
    if (!userData) {
      navigate('/login');
      return;
    }

    setUser(JSON.parse(userData));

    // Mock watchlist data (in a real app, you'd fetch this from your API)
    setWatchlist([]);
  }, [navigate]);

  if (!user) return null;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">My Watchlist</h1>

      {watchlist.length === 0 ? (
        <div className="bg-dark-light p-6 rounded-lg text-center py-12">
          <p className="text-gray-400">Your watchlist is empty.</p>
          <Link to="/" className="text-primary hover:underline block mt-4">
            Browse movies and TV shows
          </Link>
        </div>
      ) : (
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6">
          {/* Map through watchlist items here */}
        </div>
      )}
    </div>
  );
}