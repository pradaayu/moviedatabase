package core.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import core.validation.MaximumAge;
import core.validation.MinimumAge;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "AppUser")
public class User {

    @Id
    @PrimaryKeyJoinColumn
    @Column(length = 12, nullable = false, updatable = false)
    private String id;
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank
    private String avatar;

    @Past
    @MinimumAge(13)
    @MaximumAge(120)
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserCredential userCredential;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMovie> userMovies;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserReview> userReviews;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserLogin userLogin;

    // Default Constructor
    public User() {}

    // Parameterized Constructor
    public User(String id, String name, String avatar, LocalDate dateOfBirth, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
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
    
    public String getAvatar() {
    	return avatar;
    }

    public void setAvatar(String avatar) {
    	this.avatar = avatar;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
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
    }
    
    public UserLogin getUserLogin() {
    	return this.userLogin;
    }
    
    public void setUserLogin(UserLogin userLogin) {
    	this.userLogin = userLogin;
    }
    
    public List<UserMovie> getUserMovies() {
    	return userMovies;
    }
    
    public List<UserReview> getUserReviews() {
    	return userReviews;
    }
    
}
