import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";

export function DetailPage({ type = "movie" }) {
  const { id } = useParams();
  const [item, setItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Determine if we're displaying a movie or TV show
  const isMovie = type === "movie";
  const contentType = isMovie ? "movie" : "TV show";

  // Fetch details on component mount
  useEffect(() => {
    const fetchDetails = async () => {
      setLoading(true);
      try {
        const response = await fetch(`/api/omdb/id/${id}`);

        if (!response.ok) {
          throw new Error(`Server responded with status: ${response.status}`);
        }

        const data = await response.json();

        if (data.Response === "False") {
          throw new Error(data.Error || `${contentType} details not found`);
        }

        setItem(data);
      } catch (err) {
        console.error(`Error fetching ${contentType} details:`, err);
        setError(err.message || `Failed to fetch ${contentType} details`);
      } finally {
        setLoading(false);
      }
    };

    if (id) {
      fetchDetails();
    }
  }, [id, contentType]);

  if (loading) {
    return (
      <div className="min-h-screen flex justify-center items-center bg-black text-white">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-t-primary border-b-green-700 border-l-green-600 border-r-green-600 rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-xl">Loading {contentType} details...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen flex justify-center items-center bg-black text-white">
        <div className="text-center max-w-lg p-8 bg-gray-900 rounded-lg shadow-lg">
          <h2 className="text-2xl font-bold text-red-500 mb-4">Error</h2>
          <p className="mb-6">{error}</p>
          <Link to="/" className="bg-primary hover:bg-green-700 text-white px-6 py-3 rounded-md font-medium transition-colors">
            Return Home
          </Link>
        </div>
      </div>
    );
  }

  if (!item) {
    return (
      <div className="min-h-screen flex justify-center items-center bg-black text-white">
        <div className="text-center max-w-lg p-8 bg-gray-900 rounded-lg shadow-lg">
          <h2 className="text-2xl font-bold mb-4">{contentType} Not Found</h2>
          <p className="mb-6">We couldn't find the {contentType} you're looking for.</p>
          <Link to="/" className="bg-primary hover:bg-green-700 text-white px-6 py-3 rounded-md font-medium transition-colors">
            Return Home
          </Link>
        </div>
      </div>
    );
  }

  // Parse IMDB rating
  const imdbRating = parseFloat(item.imdbRating) || 0;
  const ratingColor = imdbRating >= 7.5 ? "text-primary" : (imdbRating >= 6 ? "text-yellow-500" : "text-red-500");

  // Mock reviews
  const mockReviews = [
    {
      id: 1,
      username: isMovie ? "moviefan123" : "tvfan123",
      date: "2024-03-15",
      rating: 9,
      title: isMovie ? "A masterpiece of cinema" : "A fantastic series",
      content: isMovie
        ? "This film completely blew me away. The direction, acting, and cinematography are all top-notch. I was engaged from beginning to end and can't wait to watch it again."
        : "This show completely blew me away. The writing, acting, and production design are all excellent. I binged the entire season in one weekend."
    },
    {
      id: 2,
      username: isMovie ? "filmcritic2000" : "seriescritic",
      date: "2024-03-10",
      rating: 10,
      title: isMovie ? "One of the best films I've seen this year" : "One of the best shows on television",
      content: isMovie
        ? "Everything about this movie is perfect. The story is compelling, the characters are well-developed, and the visual effects are stunning. It's a must-see for any film enthusiast."
        : "Everything about this series is exceptional. The character development is nuanced, the plot is engaging, and the performances are outstanding. Can't wait for the next season!"
    }
  ];

  return (
    <div className="bg-black text-white min-h-screen">
      {/* Main content */}
      <div className="container mx-auto px-4 py-6">
        {/* Main hero section with poster and trailer */}
		<MovieDetailHero item={item} />

        {/* Cast Section */}
		<CastSection  item={item} />

        {/* TV Show Seasons (TV Shows Only) */}
        {!isMovie && item.totalSeasons && (
          <div className="mb-12">
            <h2 className="text-2xl font-bold mb-6 flex items-center">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 mr-2 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10" />
              </svg>
              Seasons
            </h2>

            <div className="bg-gray-800 rounded-lg p-6">
              <p className="mb-4">This TV show has {item.totalSeasons} seasons.</p>
              <p className="text-gray-400">Detailed season information not available via the OMDB API.</p>
            </div>
          </div>
        )}

        {/* Reviews Section */}
        <div className="mb-12">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold flex items-center">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 mr-2 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
              </svg>
              User Reviews
            </h2>
            <button className="px-4 py-2 bg-primary hover:bg-green-700 rounded-lg transition-colors text-black text-sm font-medium">
              Write a Review
            </button>
          </div>

          <div className="space-y-6">
            {mockReviews.map((review) => (
              <div key={review.id} className="bg-gray-800 rounded-lg p-6">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="font-bold text-lg">{review.title}</h3>
                    <div className="flex items-center mt-1">
                      <div className="bg-gray-700 rounded-full px-2 py-1 text-sm flex items-center mr-3">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 text-yellow-500 mr-1" viewBox="0 0 20 20" fill="currentColor">
                          <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                        </svg>
                        {review.rating}/10
                      </div>
                      <span className="text-gray-400 text-sm">{review.date}</span>
                    </div>
                  </div>
                  <span className="text-gray-400 text-sm">@{review.username}</span>
                </div>
                <p className="text-gray-300">{review.content}</p>
                <div className="flex items-center mt-4 text-sm text-gray-400">
                  <button className="flex items-center mr-4 hover:text-white transition-colors">
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M14 10h4.764a2 2 0 011.789 2.894l-3.5 7A2 2 0 0115.263 21h-4.017c-.163 0-.326-.02-.485-.06L7 20m7-10V5a2 2 0 00-2-2h-.095c-.5 0-.905.405-.905.905 0 .714-.211 1.412-.608 2.006L7 11v9m7-10h-2M7 20H5a2 2 0 01-2-2v-6a2 2 0 012-2h2.5" />
                    </svg>
                    Helpful ({Math.floor(Math.random() * 50) + 5})
                  </button>
                  <button className="flex items-center hover:text-white transition-colors">
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                    </svg>
                    Reply
                  </button>
                </div>
              </div>
            ))}

            <div className="text-center mt-8">
              <button className="px-6 py-3 bg-gray-800 hover:bg-gray-700 text-white rounded-lg transition-colors text-sm font-medium">
                Load More Reviews
              </button>
            </div>
          </div>
        </div>

        {/* Details Section */}
        <div className="mb-12">
          <h2 className="text-2xl font-bold mb-6">Details</h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-2">
            {item.Released !== "N/A" && (
              <div className="py-2 border-b border-gray-800">
                <div className="flex">
                  <div className="w-32 text-gray-400 text-sm">Release date</div>
                  <div>{item.Released}</div>
                </div>
              </div>
            )}

            {item.Country !== "N/A" && (
              <div className="py-2 border-b border-gray-800">
                <div className="flex">
                  <div className="w-32 text-gray-400 text-sm">Countries of origin</div>
                  <div>{item.Country}</div>
                </div>
              </div>
            )}

            {item.Language !== "N/A" && (
              <div className="py-2 border-b border-gray-800">
                <div className="flex">
                  <div className="w-32 text-gray-400 text-sm">Language</div>
                  <div>{item.Language}</div>
                </div>
              </div>
            )}

            {item.imdbID !== "N/A" && (
              <div className="py-2 border-b border-gray-800">
                <div className="flex">
                  <div className="w-32 text-gray-400 text-sm">IMDb ID</div>
                  <div>
                    <a
                      href={`https://www.imdb.com/title/${item.imdbID}`}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="text-primary hover:text-green-400 transition-colors"
                    >
                      {item.imdbID}
                    </a>
                  </div>
                </div>
              </div>
            )}

            {item.BoxOffice !== "N/A" && (
              <div className="py-2 border-b border-gray-800">
                <div className="flex">
                  <div className="w-32 text-gray-400 text-sm">Box Office</div>
                  <div>{item.BoxOffice}</div>
                </div>
              </div>
            )}

            {item.Runtime !== "N/A" && (
              <div className="py-2 border-b border-gray-800">
                <div className="flex">
                  <div className="w-32 text-gray-400 text-sm">Runtime</div>
                  <div>{item.Runtime}</div>
                </div>
              </div>
            )}

            {item.Production !== "N/A" && (
              <div className="py-2 border-b border-gray-800">
                <div className="flex">
                  <div className="w-32 text-gray-400 text-sm">Production</div>
                  <div>{item.Production}</div>
                </div>
              </div>
            )}

            {item.DVD !== "N/A" && (
              <div className="py-2 border-b border-gray-800">
                <div className="flex">
                  <div className="w-32 text-gray-400 text-sm">DVD release</div>
                  <div>{item.DVD}</div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

function MovieDetailHero({ item }) {
  // Ensure we have a fallback for missing or incomplete data
  if (!item) return null;

  return (
    <div className="border-b border-gray-800 py-4">
      <div className="container mx-auto px-4">
        <div className="grid grid-cols-12 gap-6">
          {/* Left: Poster */}
          <div className="col-span-3 relative">
            <div className="relative" style={{ paddingBottom: "150%" }}>
              {item.Poster && item.Poster !== "N/A" ? (
                <img
                  src={item.Poster}
                  alt={`${item.Title} poster`}
                  className="absolute inset-0 w-full h-full object-cover rounded-lg"
                />
              ) : (
                <div className="absolute inset-0 w-full h-full bg-gray-800 rounded-lg flex items-center justify-center">
                  <span className="text-lg text-gray-400">No Poster</span>
                </div>
              )}

              {/* Add to Watchlist button */}
              <div className="absolute top-0 left-0 m-2">
                <button className="flex items-center justify-center bg-primary hover:bg-green-700 px-3 py-1.5 rounded font-medium transition-colors">
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 mr-1" viewBox="0 0 20 20" fill="currentColor">
                    <path d="M5 4a2 2 0 012-2h6a2 2 0 012 2v14l-5-2.5L5 18V4z" />
                  </svg>
                  <span>Add</span>
                </button>
              </div>
            </div>
          </div>

          {/* Right: Movie Details */}
          <div className="col-span-9 flex flex-col justify-between">
            <div>
              {/* Title and Basic Info */}
              <div className="mb-4">
                <h1 className="text-4xl font-bold mb-2">{item.Title}</h1>
                <div className="flex items-center text-sm text-gray-300">
                  {item.Year && <span>{item.Year}</span>}
                  {item.Rated && item.Rated !== "N/A" && (
                    <>
                      <span className="mx-2">•</span>
                      <span>{item.Rated}</span>
                    </>
                  )}
                  {item.Runtime && item.Runtime !== "N/A" && (
                    <>
                      <span className="mx-2">•</span>
                      <span>{item.Runtime}</span>
                    </>
                  )}
                </div>
              </div>

              {/* Ratings */}
              <div className="flex items-center space-x-6 mb-6">
                {item.imdbRating && item.imdbRating !== "N/A" && (
                  <div className="text-center">
                    <div className="text-xs text-gray-400">IMDb RATING</div>
                    <div className="flex items-center justify-center mt-1">
                      <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-yellow-400 mr-1" viewBox="0 0 20 20" fill="currentColor">
                        <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                      </svg>
                      <span className="font-bold">{item.imdbRating}</span>
                      <span className="text-gray-400 text-xs ml-1">/10</span>
                    </div>
                  </div>
                )}

                {item.Metascore && item.Metascore !== "N/A" && (
                  <div className="text-center">
                    <div className="text-xs text-gray-400">METASCORE</div>
                    <div className={`mt-1 px-2 py-1 text-white font-bold ${parseInt(item.Metascore) >= 70 ? "bg-green-600" : parseInt(item.Metascore) >= 50 ? "bg-yellow-600" : "bg-red-600"}`}>
                      {item.Metascore}
                    </div>
                  </div>
                )}
              </div>

              {/* Plot */}
              {item.Plot && item.Plot !== "N/A" && (
                <p className="text-lg mb-6">{item.Plot}</p>
              )}

              {/* Creators Info */}
              <div className="border-t border-gray-800 pt-4">
                <div className="grid grid-cols-1 gap-2">
                  {item.Director && item.Director !== "N/A" && (
                    <div className="flex">
                      <div className="w-24 text-gray-400">Director</div>
                      <div>{item.Director}</div>
                    </div>
                  )}

                  {item.Writer && item.Writer !== "N/A" && (
                    <div className="flex">
                      <div className="w-24 text-gray-400">Writers</div>
                      <div>{item.Writer}</div>
                    </div>
                  )}

                  {item.Actors && item.Actors !== "N/A" && (
                    <div className="flex">
                      <div className="w-24 text-gray-400">Stars</div>
                      <div>{item.Actors}</div>
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* Genres */}
            {item.Genre && item.Genre !== "N/A" && (
              <div className="mt-6 flex flex-wrap gap-2">
                {item.Genre.split(", ").map((genre, index) => (
                  <span
                    key={index}
                    className="px-4 py-2 bg-gray-800 rounded-full text-sm"
                  >
                    {genre}
                  </span>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

const CastSection = ({ item }) => {
  if (!item || !item.Actors) return null;

  // Split actors and limit to 10 for display
  const actors = item.Actors.split(", ").slice(0, 10);

  return (
    <div className="mb-12">
      <h2 className="text-2xl font-bold mb-6 flex items-center">
        <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 mr-2 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
        </svg>
        Cast
      </h2>

      <div className="flex overflow-x-auto space-x-4 pb-4">
        {actors.map((actor, index) => (
          <div
            key={index}
            className="flex-shrink-0 w-40 bg-gray-800 rounded-lg overflow-hidden shadow-lg"
          >
            <div className="aspect-square bg-gray-700 flex items-center justify-center">
              <svg xmlns="http://www.w3.org/2000/svg" className="h-12 w-12 text-gray-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </div>
            <div className="p-3 text-center bg-primary">
              <h3 className="font-medium text-base mb-1 line-clamp-1 text-black">{actor}</h3>
              <p className="text-gray-400 text-xs line-clamp-1">Character</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};