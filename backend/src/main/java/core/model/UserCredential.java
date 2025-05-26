package core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "UserCredential")
public class UserCredential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;
    
    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_user_credential_user"))
    private User user;
    
    // Default Constructor
    public UserCredential() {}

    // Parameterized Constructor
    public UserCredential(User user, String email, String password) {
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
    
    public User getUser() { 
    	return user; 
    }
	public void setUser(User user) {
		this.user = user;
	}
}
