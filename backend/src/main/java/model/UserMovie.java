package model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import model.composite_keys.UserMovieId;

@Entity
@Table (name = "UserMovie")
public class UserMovie {
	
    @EmbeddedId
    private UserMovieId id;
    
    @ManyToOne
    @MapsId("userId")  // Links to userId in UserReviewId
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_movie_user"))
    private User user;
    
    @Column(name = "added_at", updatable = false, insertable = false)
    private LocalDateTime addedAt;
    
    // Default Constructor
    public UserMovie() {}

    // Parameterized Constructor
    public UserMovie(User user, String movieRef) {
        this.user = user;
        this.id = new UserMovieId(user.getId(), movieRef);
    }

    public String getMovieRef() { 
    	return id.getMovieRef(); 
	}
    public void setMovieRef(String movieRef) {
    	this.id.setMovieRef(movieRef);
    }
    
    public LocalDateTime getAddedAt() {
    	return addedAt;
    }
    
    public User getUser() { 
    	return user; 
	}
}
