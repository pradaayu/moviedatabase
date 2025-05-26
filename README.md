# MovieDB 🎬

MovieDB is a comprehensive movie and TV show database application that provides users with detailed information about films and series, with planned features for registered users.

## 🌟 Features

### Current Features
- Browse extensive movie and TV show catalog
- Detailed movie/show information
- View ratings, cast, and comprehensive details
- Search functionality

### Planned Features
- User registration and authentication
- Personal watchlist management
- Movie and show reviews
- Advanced search and filter options
- User profile management

## 🛠 Tech Stack

### Frontend
- **Language**: TypeScript with React
- **Routing**: React Router
- **Styling**: Tailwind CSS

### Backend
- **Language**: Java
- **Framework**: Spring Boot
- **ORM**: Hibernate
- **Database**: MySQL

### External API
- OMDB API for movie and TV show data

## 🚀 Getting Started

### Prerequisites
- Node.js (v16+)
- npm or yarn
- Java Development Kit (JDK) 17+
- MySQL
- OMDB API Key

### Frontend Setup
1. Clone the repository
```bash
git clone https://github.com/yourusername/moviedb.git
cd moviedb/frontend
```

2. Install dependencies
```bash
npm install
```

3. Create a `.env` file
```
REACT_APP_OMDB_API_KEY=your_omdb_api_key
REACT_APP_BACKEND_URL=http://localhost:8080/api
```

4. Run the application
```bash
npm start
```

### Backend Setup
1. Navigate to backend directory
```bash
cd ../backend
```

2. Configure `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moviedb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build and run the application
```bash
./mvnw spring-boot:run
```

## 🌐 API Integration

### OMDB API
- Provides movie and TV show details
- Fetches:
  - Ratings
  - Cast information
  - Plot summaries
  - Genres
  - Release details

### Custom Backend API
- Will handle:
  - User authentication
  - Watchlist management
  - Review system
  - User profile operations

## 📋 Development Roadmap

### Upcoming Implementations
- [ ] User Registration
- [ ] User Authentication
- [ ] Watchlist Functionality
- [ ] Review System
- [ ] Profile Management
- [ ] Advanced Search Filters
- [ ] Comprehensive Error Handling
- [ ] Unit and Integration Testing

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch 
   ```
   git checkout -b feature/AmazingFeature
   ```
3. Commit your changes 
   ```
   git commit -m 'Add some AmazingFeature'
   ```
4. Push to the branch 
   ```
   git push origin feature/AmazingFeature
   ```
5. Open a Pull Request

## 🛠 Development Notes

### TypeScript React Conventions
- Use strict typing
- Leverage React functional components with hooks
- Implement prop and state interfaces

### Backend Best Practices
- Follow Spring Boot conventions
- Use service layer for business logic
- Implement proper exception handling
- Create comprehensive DTOs

## 📞 Contact

Project Link: [https://github.com/pradaayu/moviedatabase](https://github.com/pradaayu/moviedatabase)

## 🙏 Acknowledgements

- [OMDB API](http://www.omdbapi.com/)
- [React](https://reactjs.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Tailwind CSS](https://tailwindcss.com/)
