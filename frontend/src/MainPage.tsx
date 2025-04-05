import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export function MainPage() {
  const navigate = useNavigate();

  // Featured Movies for the carousel (using popular movie titles as search terms)
  const featuredMovieTerms = ['Matrix', 'Inception', 'Avengers', 'Interstellar', 'Godfather', 'Parasite', 'Avatar', 'Joker', 'Dune', 'Titanic'];
  const [featuredMovies, setFeaturedMovies] = useState([]);
  const [loadingFeatured, setLoadingFeatured] = useState(true);

  // Category data - using meaningful search terms for each category
  const categories = [
    {
      title: "Latest Movies",
      subtitle: "Recently released films from the past year",
      params: { s: "2024", type: "movie", y: "2024" }
    },
    {
      title: "Popular TV Shows",
      subtitle: "Highly-rated series to binge watch",
      params: { s: "game", type: "series" }
    },
    {
      title: "Action Movies",
      subtitle: "High-octane thrills and epic journeys",
      params: { s: "fast", type: "movie" }
    },
    {
      title: "Sci-Fi Favorites",
      subtitle: "Mind-bending stories and futuristic worlds",
      params: { s: "star", type: "movie" }
    },
    {
      title: "Animated Adventures",
      subtitle: "Colorful worlds and memorable characters",
      params: { s: "pixar", type: "movie" }
    }
  ];

  // Fetch featured movies for carousel
  useEffect(() => {
    const fetchFeaturedMovies = async () => {
      setLoadingFeatured(true);
      const movies = [];

      try {
        // Get one random movie from each search term (movie title keywords)
        for (const term of featuredMovieTerms) {
          if (movies.length >= 10) break; // Limit to 10 movies

          // Use the search endpoint with 's' as movie title search term
          const response = await fetch(`/api/omdb/search?s=${encodeURIComponent(term)}`);

          if (response.ok) {
            const data = await response.json();

            if (data.Response === "True" && data.Search && data.Search.length > 0) {
              // Add a random movie from the results that isn't already in our featured list
              const randomIndex = Math.floor(Math.random() * Math.min(data.Search.length, 5)); // Take from first 5 results for better relevance
              const randomMovie = data.Search[randomIndex];

              // Check if movie is already in the list
              if (!movies.some(m => m.imdbID === randomMovie.imdbID)) {
                // Add a fake rating percentage for UI purposes
                randomMovie.ratingPercentage = Math.floor(60 + Math.random() * 40).toString();
                movies.push(randomMovie);
              }
            }
          }
        }
      } catch (error) {
        console.error('Error fetching featured movies:', error);
      } finally {
        setFeaturedMovies(movies);
        setLoadingFeatured(false);
      }
    };

    fetchFeaturedMovies();
  }, []);

  // Handler for movie card clicks
  const handleMovieClick = (movie) => {
    const route = movie.Type === 'series' ? `/tv/${movie.imdbID}` : `/movie/${movie.imdbID}`;
    navigate(route);
  };

  // Custom hook instances for each category
  const categoryData = categories.map(category => ({
    ...category,
    ...useMovieData(category.params)
  }));

  return (
    <div className="min-h-screen bg-black text-white">
      {/* Hero Carousel */}
      <MovieCarousel
        movies={featuredMovies}
        loading={loadingFeatured}
        onMovieClick={handleMovieClick}
      />

      {/* Movie Categories */}
      {categoryData.map((category, index) => (
        <MovieSlider
          key={index}
          title={category.title}
          subtitle={category.subtitle}
          movies={category.data}
          loading={category.loading}
          error={category.error}
          onMovieClick={handleMovieClick}
        />
      ))}
    </div>
  );
}

// Custom hook for fetching movie data
const useMovieData = (searchParams) => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Convert searchParams to a string for dependency comparison
  const searchParamsString = JSON.stringify(searchParams);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        const queryParams = new URLSearchParams();

        // Add all search params to URL params
        Object.entries(searchParams).forEach(([key, value]) => {
          queryParams.append(key, value);
        });

        const response = await fetch(`/api/omdb/search?${queryParams.toString()}`);

        if (!response.ok) {
          throw new Error(`Server responded with status: ${response.status}`);
        }

        const result = await response.json();

        if (result.Response === "False") {
          setData([]);
        } else {
          setData(result.Search || []);
        }
      } catch (err) {
        console.error('Error fetching data:', err);
        setError('Failed to fetch data');
        setData([]);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [searchParamsString]); // Depend on the stringified params

  return { data, loading, error };
};

// Movie Card Component
const MovieCard = ({ movie, onCardClick }) => {
  return (
    <div
      className="flex flex-col cursor-pointer transition-transform hover:scale-105"
      onClick={() => onCardClick(movie)}
    >
      <div className="relative overflow-hidden rounded-lg">
        {movie.Poster && movie.Poster !== 'N/A' ? (
          <img
            src={movie.Poster}
            alt={movie.Title}
            className="w-full h-auto aspect-[2/3] object-cover"
            loading="lazy"
          />
        ) : (
          <div className="w-full aspect-[2/3] bg-gray-800 flex items-center justify-center">
            <span className="text-white">No Poster</span>
          </div>
        )}
        <div className="absolute top-2 right-2 bg-black bg-opacity-70 text-white text-xs px-2 py-1 rounded-full">
          {movie.Type === 'movie' ? 'Movie' : 'TV'}
        </div>
        {movie.ratingPercentage && (
          <div className={`absolute bottom-2 left-2 rounded-full w-8 h-8 flex items-center justify-center text-xs font-bold ${parseInt(movie.ratingPercentage) > 60 ? 'bg-green-500' : 'bg-yellow-500'}`}>
            {movie.ratingPercentage}%
          </div>
        )}
      </div>
      <h3 className="mt-2 font-medium text-white line-clamp-1">{movie.Title}</h3>
      <p className="text-gray-400 text-sm">{movie.Year}</p>
    </div>
  );
};

// Movie Carousel Component
const MovieCarousel = ({ movies, loading, onMovieClick }) => {
  const [currentIndex, setCurrentIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      if (movies.length > 0) {
        setCurrentIndex((prevIndex) => (prevIndex + 1) % movies.length);
      }
    }, 5000);

    return () => clearInterval(interval);
  }, [movies]);

  if (loading || movies.length === 0) {
    return (
      <div className="relative w-full h-[500px] bg-gray-900 animate-pulse">
        <div className="absolute inset-0 flex items-center justify-center">
          <span className="text-white text-xl">Loading featured content...</span>
        </div>
      </div>
    );
  }

  const movie = movies[currentIndex];

  return (
    <div className="relative w-full h-[500px] overflow-hidden">
      <div
        className="absolute inset-0 bg-cover bg-center transition-opacity duration-500"
        style={{
          backgroundImage: `url(${movie.Poster !== 'N/A' ? movie.Poster : ''})`,
          filter: 'blur(2px)',
          opacity: 0.3
        }}
      />
      <div className="absolute inset-0 bg-gradient-to-t from-black via-black/50 to-transparent" />
      <div className="absolute inset-0 flex items-center">
        <div className="container mx-auto px-4 flex flex-col md:flex-row items-center md:items-start gap-8">
          <div className="w-48 md:w-64 flex-shrink-0">
            <img
              src={movie.Poster !== 'N/A' ? movie.Poster : ''}
              alt={movie.Title}
              className="w-full rounded-lg shadow-2xl"
            />
          </div>
          <div className="flex flex-col text-white max-w-2xl">
            <div className="text-primary font-semibold mb-2">#{currentIndex + 1} Spotlight</div>
            <h1 className="text-4xl md:text-5xl font-bold mb-4">{movie.Title}</h1>
            <p className="text-lg mb-6">
              A compelling {movie.Type} released in {movie.Year} that captivates audiences with its unique storytelling and remarkable performances.
            </p>
            <div className="flex space-x-4">
              <button
                className="bg-primary hover:bg-green-600 text-white px-6 py-3 rounded-md font-medium transition-colors"
              >
                ADD TO WATCHLIST
              </button>
              <button
			  	onClick={() => onMovieClick(movie)}
			  	className="bg-gray-800 hover:bg-gray-700 text-white px-6 py-3 rounded-md font-medium transition-colors">
                DETAILS
              </button>
            </div>
          </div>
        </div>
      </div>
      <div className="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-2">
        {movies.map((_, index) => (
          <button
            key={index}
            onClick={() => setCurrentIndex(index)}
            className={`w-2 h-2 rounded-full ${index === currentIndex ? 'bg-green-500' : 'bg-gray-400'}`}
          />
        ))}
      </div>
    </div>
  );
};

// Movie Slider Component
const MovieSlider = ({ title, subtitle, movies, loading, error, onMovieClick }) => {
  const scrollRef = React.useRef(null);

  const scroll = (direction) => {
    if (scrollRef.current) {
      const { current } = scrollRef;
      const scrollAmount = direction === 'left' ? -current.offsetWidth / 2 : current.offsetWidth / 2;
      current.scrollBy({ left: scrollAmount, behavior: 'smooth' });
    }
  };

  return (
    <div className="py-8">
      <div className="container mx-auto px-4">
        <h2 className="text-2xl md:text-3xl font-bold text-white mb-1">{title}</h2>
        {subtitle && <p className="text-gray-400 mb-4">{subtitle}</p>}

        {loading && (
          <div className="flex space-x-4 overflow-x-auto py-4">
            {[...Array(5)].map((_, index) => (
              <div key={index} className="flex-shrink-0 w-36 md:w-44 animate-pulse">
                <div className="bg-gray-700 aspect-[2/3] rounded-lg mb-2"></div>
                <div className="h-4 bg-gray-700 rounded w-3/4 mb-1"></div>
                <div className="h-3 bg-gray-700 rounded w-1/2"></div>
              </div>
            ))}
          </div>
        )}

        {error && <p className="text-red-500 py-4">{error}</p>}

        {!loading && !error && movies.length === 0 && (
          <p className="text-gray-400 py-4">No movies found</p>
        )}

        {!loading && !error && movies.length > 0 && (
          <div className="relative group">
            <button
              onClick={() => scroll('left')}
              className="absolute left-0 top-1/2 transform -translate-y-1/2 z-10 bg-black bg-opacity-50 hover:bg-opacity-70 text-white rounded-full p-2 opacity-0 group-hover:opacity-100 transition-opacity focus:outline-none"
              aria-label="Scroll left"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
              </svg>
            </button>

            <div
              ref={scrollRef}
              className="flex space-x-4 overflow-x-auto py-4 scrollbar-hide"
              style={{ scrollbarWidth: 'none', msOverflowStyle: 'none' }}
            >
              {movies.map((movie) => (
                <div key={movie.imdbID} className="flex-shrink-0 w-36 md:w-44">
                  <MovieCard movie={movie} onCardClick={onMovieClick} />
                </div>
              ))}
            </div>

            <button
              onClick={() => scroll('right')}
              className="absolute right-0 top-1/2 transform -translate-y-1/2 z-10 bg-black bg-opacity-50 hover:bg-opacity-70 text-white rounded-full p-2 opacity-0 group-hover:opacity-100 transition-opacity focus:outline-none"
              aria-label="Scroll right"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
              </svg>
            </button>
          </div>
        )}
      </div>
    </div>
  );
};