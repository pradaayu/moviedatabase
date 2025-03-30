package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "UserLogin")
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 12, updatable = false, nullable = false)
    private String id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_user_login_user"))
    private User user;
    
    @Email
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;
    
    // Default Constructor
    public UserLogin() {}

    // Parameterized Constructor
    public UserLogin(String id, User user, String email, String password) {
        this.id = id;
        this.user = user;
        this.email = email;
        this.password = password;
    }
    
    public String getId() { 
    	return id; 
	}
//    public void setId(String id) { /* id is automatically created by Hibernate */
//    	this.id = id; 
//	}

    public User getUser() { 
    	return user; 
    }
    public void setUser(User user) { 
    	this.user = user; 
	}

    public String getEmail() { 
    	return email; 
	}
    public void setEmail(String email) { 
    	this.email = email; 
	}

    public String getPassword() { 
    	return password; 
	}
    public void setPassword(String password) { 
    	this.password = password; 
	}
}
