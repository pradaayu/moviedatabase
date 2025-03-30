package model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @Column(length = 12, nullable = false, updatable = false)
    private String id;
    
    @Size(min = 1)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "created_at", updatable = false, insertable = false)
    private Timestamp createdAt;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserLogin userLogin;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMovie> userMovies;
    
    // Ensure that user reviews remain upon user deletion
    @OneToMany(mappedBy = "user", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = false)
    private List<UserReview> userReviews;

    // Default Constructor
    public User() {}

    // Parameterized Constructor
    public User(String id, String name, LocalDateTime dateOfBirth, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }
    
    public void addUserLogin(UserLogin login) {
        this.userLogin = login;
        login.setUser(this);
    }
    
    public List<UserMovie> getUserMovies() {
    	return userMovies;
    }
    
    public List<UserReview> getUserReviews() {
    	return userReviews;
    }
}
