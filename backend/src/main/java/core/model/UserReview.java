package core.model;

import java.time.LocalDateTime;

import core.model.composite_keys.UserReviewId;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table (name = "UserReview")
public class UserReview {
	
	@EmbeddedId
	private UserReviewId id;

    @ManyToOne
    @MapsId("userId")  // Links to userId in UserReviewId
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_review_user"))
    private User user;

    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    @Lob
    private String content;
    
    @Min(1)  // Ensure rating is at least 1
    @Max(5)  // Ensure rating is no greater than 5
    @Column(nullable = false)
    private int rating;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false, insertable = false, 
    	columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
    )
    private LocalDateTime updatedAt;

    // Default Constructor
    public UserReview() {}

    // Parameterized Constructor
    public UserReview(User user, String content, int rating, String movieRef) {
        this.user = user;
        this.content = content;
        this.rating = rating;
        this.id = new UserReviewId(user.getId(), movieRef);
    }
    
    public String getContent() {
    	return content;
    }
    public void setContent(String content) {
    	this.content = content;
    }
    
    public int getRating() {
    	return rating;
    }
    public void setRating(int rating) {
    	this.rating = rating;
    }
    
    public String getMovieRef() {
    	return this.id.getMovieRef();
    }
    public void setMovieRef(String movieRef) {
    	this.id.setMovieRef(movieRef);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdateAt() {
    	return updatedAt;
    }
    
    public User getUser() {
    	return user;
    }
}
