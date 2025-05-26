package core.model.composite_keys;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class UserMovieId implements Serializable {
    
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
    private String userId;  // Foreign key part of primary key
    
    @Column(name = "movie_ref", nullable = false, length = 255)
    private String movieRef; // Second part of primary key

    // Default constructor
    public UserMovieId() {}

    // Parameterized constructor
    public UserMovieId(String userId, String movieRef) {
        this.userId = userId;
        this.movieRef = movieRef;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getMovieRef() { return movieRef; }
    public void setMovieRef(String movieRef) { this.movieRef = movieRef; }

    // Override equals() and hashCode() for correct primary key comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMovieId that = (UserMovieId) o;
        return Objects.equals(userId, that.userId) && 
               Objects.equals(movieRef, that.movieRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, movieRef);
    }
}
