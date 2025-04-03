import React, { useState, useEffect } from 'react';

const App = () => {
  const [message, setMessage] = useState('Loading...');
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  const [totalResults, setTotalResults] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  // Search for movies - returns multiple results with pagination
  const searchMovies = async (page = 1) => {
    if (!searchTerm.trim()) return;

    setLoading(true);
    setError(null);

    try {
      const response = await fetch(`/api/omdb/search?s=${encodeURIComponent(searchTerm)}&page=${page}`);

      if (!response.ok) {
        throw new Error(`Server responded with status: ${response.status}`);
      }

      const data = await response.json();

      if (data.Response === "False") {
        setError(data.Error || "No movies found");
        setMovies([]);
        setTotalResults(0);
        setTotalPages(0);
      } else {
        setMovies(data.Search || []);
        setTotalResults(parseInt(data.totalResults) || 0);
        setTotalPages(Math.min(Math.ceil(parseInt(data.totalResults) / 10), 10));
        setCurrentPage(page);
      }
    } catch (error) {
      console.error('Error searching movies:', error);
      setError('Error connecting to server');
      setMovies([]);
    } finally {
      setLoading(false);
    }
  };

  const handlePageChange = (page) => {
    searchMovies(page);
  };

  useEffect(() => {
    // Fetch message from backend
//    fetch('/api/hello')
//      .then(response => response.json())
//      .then(data => setMessage(data.message))
//      .catch(error => setMessage('Error connecting to backend'));
	  // If you want to load initial data when the component mounts
	  searchMovies(1);
  }, []);

  const renderPagination = () => {
      if (totalPages <= 1) return null;

      return (
        <div style={{ margin: '20px 0' }}>
          <button
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1 || loading}
            style={{ margin: '0 5px' }}
          >
            Previous
          </button>

          {[...Array(totalPages)].map((_, index) => (
            <button
              key={index + 1}
              onClick={() => handlePageChange(index + 1)}
              disabled={currentPage === index + 1 || loading}
              style={{
                margin: '0 5px',
                fontWeight: currentPage === index + 1 ? 'bold' : 'normal',
                backgroundColor: currentPage === index + 1 ? '#e0e0e0' : 'white'
              }}
            >
              {index + 1}
            </button>
          ))}

          <button
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages || loading}
            style={{ margin: '0 5px' }}
          >
            Next
          </button>
        </div>
      );
    };

    return (
      <div style={{ textAlign: 'center', marginTop: '50px' }}>
        <h1>Movie Search</h1>

        <div style={{ margin: '20px 0' }}>
          <input
            type="text"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            placeholder="Enter movie title..."
            style={{ padding: '8px', marginRight: '10px', width: '300px' }}
          />
          <button
            onClick={() => searchMovies(1)}
            disabled={loading}
            style={{ padding: '8px 16px' }}
          >
            {loading ? 'Searching...' : 'Search'}
          </button>
        </div>

        {error && <p style={{ color: 'red' }}>{error}</p>}

        {totalResults > 0 && (
          <p>Found {totalResults} results</p>
        )}

        <div style={{ display: 'flex', flexWrap: 'wrap', justifyContent: 'center' }}>
          {movies.map((movie) => (
            <div key={movie.imdbID} style={{ margin: '10px', width: '200px' }}>
              {movie.Poster && movie.Poster !== 'N/A' ? (
                <img
                  src={movie.Poster}
                  alt={movie.Title}
                  style={{ width: '100%', height: 'auto' }}
                />
              ) : (
                <div style={{
                  width: '100%',
                  height: '300px',
                  backgroundColor: '#f0f0f0',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}>
                  No Poster
                </div>
              )}
              <h3>{movie.Title}</h3>
              <p>{movie.Year}</p>
            </div>
          ))}
        </div>

        {renderPagination()}
      </div>
    );
};

export default App;