import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';

export function SearchResultPage() {
	const navigate = useNavigate();
	const [results, setResults] = useState([] as movieItem[]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState("");
	const [currentPage, setCurrentPage] = useState(1 as number);
	const [totalResults, setTotalResults] = useState(0);
	const [totalPages, setTotalPages] = useState(0);

	const urlParams = new URLSearchParams(window.location.search);
	const query = urlParams.get('query') || "";

	useEffect(() => {
	  if (query) {
	    fetchResults(query,currentPage);
	  }
	}, [query]);

	const fetchResults = async (searchTerm: string, page?: number) => {
		if (!searchTerm.trim()) return;
	  	setLoading(true);
		setError("");

	  try {
		const response = await fetch(`/api/omdb/search?s=${encodeURIComponent(searchTerm)}&page=${page}`);

	    if (!response.ok) {
	      throw new Error(`Server responded with status: ${response.status}`);
	    }

	    const data = await response.json();

	    if (data.Response === "False") {
	      setError(data.Error || "No results found");
	      setResults([]);
		  setTotalResults(0);
		  setTotalPages(0);
	    } else {
	      setResults(data.Search || []);
		  setTotalResults(parseInt(data.totalResults) || 0);
		  setTotalPages(Math.min(Math.ceil(parseInt(data.totalResults) / 10), 10));
		  if (page) setCurrentPage(page);
	      setError("");
	    }
	  } catch (err) {
	    console.error('Error fetching results:', err);
	    setError('Error connecting to server');
	    setResults([]);
	  } finally {
	    setLoading(false);
	  }
	};

	const handleItemClick = (item: any) => {
	  const route = item.Type === 'series' ? `/tv/${item.imdbID}` : `/movie/${item.imdbID}`;
	  navigate(route);
	};

	const handlePageChange = (page: number) => {
	  fetchResults(query,page);
	};

	const renderPagination = () => {
	    if (totalPages <= 1) return null;

	    return (
	      <div className="flex justify-center items-center space-x-2 my-6">
	        <button
	          onClick={() => handlePageChange(currentPage - 1)}
	          disabled={currentPage === 1 || loading}
	          className="px-4 py-2 border rounded-md
	            disabled:opacity-50 disabled:cursor-not-allowed
	            hover:bg-gray-100 transition-colors"
	        >
	          Previous
	        </button>

	        {[...Array(totalPages)].map((_, index) => (
	          <button
	            key={index + 1}
	            onClick={() => handlePageChange(index + 1)}
	            disabled={currentPage === index + 1 || loading}
	            className={`px-4 py-2 border rounded-md
	              disabled:opacity-50 disabled:cursor-not-allowed
	              transition-colors
	              ${currentPage === index + 1
	                ? 'bg-gray-200 font-bold'
	                : 'hover:bg-gray-100'}`}
	          >
	            {index + 1}
	          </button>
	        ))}

	        <button
	          onClick={() => handlePageChange(currentPage + 1)}
	          disabled={currentPage === totalPages || loading}
	          className="px-4 py-2 border rounded-md
	            disabled:opacity-50 disabled:cursor-not-allowed
	            hover:bg-gray-100 transition-colors"
	        >
	          Next
	        </button>
	      </div>
	    );
	  };

	return (	<div className="container mx-auto px-4 py-8">
	     <h1 className="text-3xl font-bold mb-6">Search results for: {query}</h1>

	     {loading && <p className="text-center py-12">Loading...</p>}

	     {error && <p className="text-center text-red-500 py-12">{error}</p>}

	     {!loading && !error && results.length === 0 && (
	       <p className="text-center py-12">No results found for "{query}"</p>
	     )}

	     <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6">
	       {results.map(item => (
	         <div
	           key={item.imdbID}
	           onClick={() => handleItemClick(item)}
	           className="cursor-pointer transition-transform hover:scale-105"
	         >
	           <div className="relative rounded overflow-hidden">
	             {item.Poster && item.Poster !== 'N/A' ? (
	               <img
	                 src={item.Poster}
	                 alt={item.Title}
	                 className="w-full h-auto"
	               />
	             ) : (
	               <div className="bg-gray-800 w-full h-64 flex items-center justify-center">
	                 No Poster
	               </div>
	             )}
	             <div className="absolute top-2 right-2 bg-black bg-opacity-70 text-xs px-2 py-1 rounded-full">
	               {item.Type === 'movie' ? 'Movie' : 'TV'}
	             </div>
	           </div>
	           <h3 className="mt-2 font-medium line-clamp-1">{item.Title}</h3>
	           <p className="text-gray-400 text-sm">{item.Year}</p>
	         </div>
	       ))}
	     </div>
		 {renderPagination()}
	   </div>)
}

type movieItem = {
	imdbID: string;
	Poster: string;
	Title: string;
	Year: string;
	Type: string;
}