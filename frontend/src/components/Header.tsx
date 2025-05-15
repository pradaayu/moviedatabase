import React, { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from 'react-router-dom';
import { useAuthContext } from '../auth/AuthProvider';
import { UserContextProvider, useUserContext } from "../user-profile/UserContextProvider";

// Custom SVG Icons
const UserIcon = () => (
	<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
		<path strokeLinecap="round" strokeLinejoin="round" d="M15.75 6a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0zM4.501 20.118a7.5 7.5 0 0114.998 0A17.933 17.933 0 0112 21.75c-2.676 0-5.216-.584-7.499-1.632z" />
	</svg>
);

const BookmarkIcon = () => (
	<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
		<path strokeLinecap="round" strokeLinejoin="round" d="M17.593 3.322c1.1.128 1.907 1.077 1.907 2.185V21L12 17.25 4.5 21V5.507c0-1.108.806-2.057 1.907-2.185a48.507 48.507 0 0111.186 0z" />
	</svg>
);

const SearchIcon = () => (
	<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-5 h-5">
		<path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z" />
	</svg>
);

export function Header() {
	const navigate = useNavigate();
	const [searchTerm, setSearchTerm] = useState('');
	const [isProfileDropdownOpen, setIsProfileDropdownOpen] = useState(false);
	const dropdownRef = useRef<HTMLDivElement>(null);
	const userContext = useUserContext();

	// Handle click outside of dropdown to close it
	useEffect(() => {
		const handleClickOutside = (event: MouseEvent) => {
			if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
				setIsProfileDropdownOpen(false);
			}
		};

		document.addEventListener('mousedown', handleClickOutside);
		return () => {
			document.removeEventListener('mousedown', handleClickOutside);
		};
	}, []);

	const handleSearch = (e: React.FormEvent) => {
		e.preventDefault();
		if (searchTerm.trim()) {
			navigate(`/search?query=${encodeURIComponent(searchTerm)}`);
		}
	};

	const { accessToken, logout } = useAuthContext();

	const handleLogout = async () => {
		try {
			await logout();
			navigate(location.pathname);
		} catch (err) {
			console.error(err);
		}
	};
	
	const profileMenuItems = accessToken ? [
		{ label: 'Your Profile', path: '/profile' },
		{ label: 'Your Watchlist', path: '/watchlist' },
		{ label: 'Your Reviews', path: '/reviews' },
		{ label: 'Logout', action: handleLogout }
	] : [{ label: 'Login', path: '/login'}];

	return (
		<header className="bg-dark py-4 px-6 flex justify-between items-center border-b border-gray-800">
			{/* Logo */}
			<div>
				<Link to="/" className="text-primary font-bold text-2xl">MOVIEDB</Link>
			</div>

			{/* Search Bar - Centered */}
			<div className="absolute left-1/2 transform -translate-x-1/2">
				<form onSubmit={handleSearch} className="flex">
					<input
						type="text"
						value={searchTerm}
						onChange={(e) => setSearchTerm(e.target.value)}
						placeholder="Search..."
						className="bg-gray-800 text-white px-3 py-2 rounded-l w-[500px] focus:outline-none focus:ring-1 focus:ring-primary"
					/>
					<button
						type="submit"
						className="bg-primary text-black px-3 py-2 rounded-r hover:bg-opacity-90 transition-colors"
					>
						<SearchIcon />
					</button>
				</form>
			</div>

			{/* Right Side - Watchlist and Profile */}
			<div className="flex items-center space-x-4">
				{/* Watchlist */}
				<Link
					to="/watchlist"
					className="flex items-center hover:opacity-80 transition-opacity"
				>
					<div className="relative mr-2">
						<BookmarkIcon />
						<span className="absolute -top-2 -right-2 bg-primary text-black text-xs px-1 rounded-full">
							110
						</span>
					</div>
					Watchlist
				</Link>

				{/* Profile Dropdown */}
				<div className="relative" ref={dropdownRef}>
					<button
						aria-haspopup="true"
						aria-expanded={isProfileDropdownOpen}
						onClick={() => setIsProfileDropdownOpen(!isProfileDropdownOpen)}
						className="flex items-center hover:opacity-80 transition-opacity"
					>
						<UserIcon />
						<span className="ml-1">{userContext.user?.name?.toUpperCase()} ▼</span>
					</button>

					{isProfileDropdownOpen && (
						<div className="absolute right-0 mt-2 w-48 bg-gray-800 rounded-md shadow-lg z-50">
							{profileMenuItems.map((item, index) => (
								item.path ? (
									<Link
										key={index}
										to={item.path}
										state={{ from: location.pathname }}
										className="block px-4 py-2 hover:bg-gray-700 transition-colors"
										onClick={() => setIsProfileDropdownOpen(false)}
									>
										{item.label}
									</Link>
								) : (
									<button
										key={index}
										className="w-full text-left block px-4 py-2 hover:bg-gray-700 transition-colors"
										onClick={() => {
											item.action?.();
											setIsProfileDropdownOpen(false);
										}}
									>
										{item.label}
									</button>
								)
							))}
						</div>
					)}
				</div>
			</div>
		</header>
	);
}