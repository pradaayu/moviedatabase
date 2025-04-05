import React, { useState } from "react";
import { useNavigate, Link } from 'react-router-dom';

// Profile picture options
const profilePics = [
  '😎', '😄', '🤪', '🐱', '🦸', '🤢',
  '❤️', '😍', '👧', '💀'
];

export function RegisterPage() {
  const navigate = useNavigate();
  const [selectedPic, setSelectedPic] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.id]: e.target.value
    });
  };

  const handleRegister = (e: React.FormEvent) => {
    e.preventDefault();

    // Validate inputs
    if (formData.password !== formData.confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    if (!selectedPic) {
      alert('Please choose a profile picture');
      return;
    }

    // Mock registration
    localStorage.setItem('user', JSON.stringify({
      name: formData.name,
      email: formData.email,
      profilePic: selectedPic
    }));

    navigate('/');
  };

  return (
    <div className="min-h-screen bg-black flex items-center justify-center px-4">
      <div className="w-full max-w-md bg-[#1a1a1a] rounded-lg shadow-lg p-8">
        <div className="flex justify-center mb-6">
          <h1 className="text-3xl font-bold text-white">MovieDB</h1>
        </div>

        <h2 className="text-2xl font-semibold text-white text-center mb-6">Sign Up</h2>

        {/* Profile Picture Selection */}
        <div className="flex flex-wrap justify-center gap-3 mb-6">
          {profilePics.map((pic) => (
            <button
              key={pic}
              type="button"
              onClick={() => setSelectedPic(pic)}
              className={`text-4xl w-12 h-12 rounded-full flex items-center justify-center
                ${selectedPic === pic ? 'ring-2 ring-lime-500' : 'hover:bg-gray-700'}`}
            >
              {pic}
            </button>
          ))}
        </div>

        <form onSubmit={handleRegister} className="space-y-4">
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-300 mb-2">
              Name
            </label>
            <div className="relative">
              <input
                type="text"
                id="name"
                value={formData.name}
                onChange={handleChange}
                className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
                required
                placeholder="Enter your name"
              />
              <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 absolute right-3 top-1/2 -translate-y-1/2 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a8 8 0 00-8 8h16a8 8 0 00-8-8z" />
              </svg>
            </div>
          </div>

          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-300 mb-2">
              Email
            </label>
            <div className="relative">
              <input
                type="email"
                id="email"
                value={formData.email}
                onChange={handleChange}
                className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
                required
                placeholder="Enter your email"
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
                value={formData.password}
                onChange={handleChange}
                className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
                required
                placeholder="Enter your password"
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2"
              >
                {showPassword ? (
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                    <path fillRule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clipRule="evenodd" />
                  </svg>
                ) : (
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.781-1.78zM10 5a5 5 0 014.778 3.472l-1.657-1.656A3 3 0 0010 7a3 3 0 00-2.823 4.118l-1.625-1.624A5 5 0 0110 5z" clipRule="evenodd" />
                    <path d="M12.5 10a2.5 2.5 0 01-1.5 2.298V10.5a.5.5 0 011 0zM7.5 10a.5.5 0 01.25-.433V10.5a.5.5 0 01-1 0V9.567A.5.5 0 017.5 10z" />
                  </svg>
                )}
              </button>
            </div>
          </div>

          <div>
            <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-300 mb-2">
              Confirm Password
            </label>
            <div className="relative">
              <input
                type={showConfirmPassword ? "text" : "password"}
                id="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                className="w-full px-4 py-3 bg-gray-800 border border-gray-700 rounded-md text-white focus:outline-none focus:ring-2 focus:ring-lime-500"
                required
                placeholder="Confirm your password"
              />
              <button
                type="button"
                onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                className="absolute right-3 top-1/2 -translate-y-1/2"
              >
                {showConfirmPassword ? (
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                    <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                    <path fillRule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clipRule="evenodd" />
                  </svg>
                ) : (
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                    <path fillRule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.781-1.78zM10 5a5 5 0 014.778 3.472l-1.657-1.656A3 3 0 0010 7a3 3 0 00-2.823 4.118l-1.625-1.624A5 5 0 0110 5z" clipRule="evenodd" />
                    <path d="M12.5 10a2.5 2.5 0 01-1.5 2.298V10.5a.5.5 0 011 0zM7.5 10a.5.5 0 01.25-.433V10.5a.5.5 0 01-1 0V9.567A.5.5 0 017.5 10z" />
                  </svg>
                )}
              </button>
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-lime-500 text-black py-3 rounded-md font-semibold hover:bg-lime-600 transition duration-300"
          >
            SIGN UP
          </button>
        </form>

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