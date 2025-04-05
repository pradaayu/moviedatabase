import React, { useState, useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';

export function ProfilePage() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Check if user is logged in
    const userData =  JSON.stringify({
		username: "meowdynasty",
		email: "abc@gmail.com",
	}) // localStorage.getItem('user');
    if (!userData) {
      navigate('/login');
      return;
    }

    setUser(JSON.parse(userData));
  }, [navigate]);

  if (!user) return null;

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">My Profile</h1>

      <div className="bg-dark-light p-6 rounded-lg mb-8">
        <div className="flex items-center gap-6">
          <div className="w-24 h-24 rounded-full bg-gray-700 flex items-center justify-center text-3xl">
            {user.username ? user.username[0].toUpperCase() : 'U'}
          </div>

          <div>
            <h2 className="text-2xl font-semibold">{user.username || 'User'}</h2>
            <p className="text-gray-400">{user.email}</p>
          </div>
        </div>
      </div>

      <div className="mb-8">
        <h2 className="text-2xl font-bold mb-4">My Watchlist</h2>
        <div className="bg-dark-light p-6 rounded-lg text-center py-12">
          <p className="text-gray-400">Your watchlist is empty.</p>
          <Link to="/" className="text-primary hover:underline block mt-4">
            Browse movies and TV shows
          </Link>
        </div>
      </div>
    </div>
  );
}