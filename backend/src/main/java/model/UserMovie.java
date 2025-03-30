package model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table (name = "UserMovie")
public class UserMovie {
	
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 12, updatable = false, nullable = false)
	private String id;
	
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_movie_user"))
    private User user;

    @Column(name = "movie_ref", nullable = false, length = 255)
    private String movie;

    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(name = "added_at", updatable = false, insertable = false)
    private LocalDateTime addedAt;
    
    // Default Constructor
    public UserMovie() {}

    // Parameterized Constructor
    public UserMovie(User user, String movie) {
        this.user = user;
        this.movie = movie;
    }
    
    public String getId() {
    	return id;
    }
//  public void setId(String id) { /* id is automatically created by Hibernate */
//  	this.id = id;
//  }
    
    public User getUser() { 
    	return user; 
	}
    public void setUser(User user) { 
    	this.user = user; 
	}

    public String getMovie() { 
    	return movie; 
	}
    public void setMovie(String movie) {
    	this.movie = movie;
    }
    
    public LocalDateTime getAddedAt() {
    	return addedAt;
    }
}
