package model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

public class UserReview {
	
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 12, updatable = false, nullable = false)
    private String id;

    @ManyToOne
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

    @Column(name = "updated_at", nullable = false, insertable = false)
    private LocalDateTime updatedAt;

    // Default Constructor
    public UserReview() {}

    // Parameterized Constructor
    public UserReview(User user, String content, int rating) {
        this.user = user;
        this.content = content;
        this.rating = rating;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
//  public void setId(String id) { /* id is automatically created by Hibernate */
//      	this.id = id;
//  }

    public User getUser() {
    	return user;
    }
    public void setUser(User user) {
    	this.user = user;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdateAt() {
    	return updatedAt;
    }
}
