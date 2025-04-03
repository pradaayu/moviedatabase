package model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @PrimaryKeyJoinColumn
    @Column(length = 12, nullable = false, updatable = false)
    private String id;
    
    @Size(min = 1)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserCredential userCredential;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMovie> userMovies;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserReview> userReviews;

    // Default Constructor
    public User() {}

    // Parameterized Constructor
    public User(String id, String name, Date dateOfBirth, LocalDateTime createdAt) {
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public UserCredential getUserCredential() {
        return userCredential;
    }

    public void setUserCredential(UserCredential userCredential) {
        this.userCredential = userCredential;
        userCredential.setUser(this);
    }
    
    public List<UserMovie> getUserMovies() {
    	return userMovies;
    }
    
    public List<UserReview> getUserReviews() {
    	return userReviews;
    }
}
