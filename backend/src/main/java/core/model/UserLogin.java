package core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserLogin")
public class UserLogin {
	@Id
	@Column(updatable = false, nullable = false)
	private String token;
	
	@ManyToOne // ensure multi-device or multi-session capabilities
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_login_user"))
    private User user;
    
	private long issueTime;
	private long lastUseTime;
	
	public UserLogin() {};
	
	public UserLogin(String token, User user, long issueTime, long lastUseTime) {
		this.token = token;
		this.user = user;
		this.issueTime = issueTime;
		this.lastUseTime = lastUseTime;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public long getIssueTime() {
		return this.issueTime;
	}
	
	public void setIssueTime(long issueTime) {
		this.issueTime = issueTime;
	}
	
	public long getLastUseTime() {
		return this.lastUseTime;
	}
	
	public void setLastUseTime(long lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
}
