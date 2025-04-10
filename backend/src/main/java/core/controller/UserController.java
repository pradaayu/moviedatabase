package core.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.model.User;
import core.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user/profile")
public class UserController {
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@DeleteMapping
	public ResponseEntity<String> deleteUser(HttpServletRequest request) {
//        String token = jwtUtil.extractTokenFromCookie(request);
//        if (token == null) {
//            return ResponseEntity.status(401).build(); // Unauthorized
//        }
//	    try {
//	    	String userId = jwtUtil.extractUserId(token);
//	        userService.deleteUser(userId);
//	        return ResponseEntity.ok("User deleted successfully");
//	    } catch (IllegalArgumentException e) {
//	        return ResponseEntity.badRequest().body(e.getMessage());
//	    }
		return ResponseEntity.ok("success");
    }
	
	@PutMapping
	public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequest updateRequest, HttpServletRequest request) {
//        String token = jwtUtil.extractTokenFromCookie(request);
//        if (token == null) {
//            return ResponseEntity.status(401).build(); // Unauthorized
//        }
//	    try {
//	    	User user = new User();
//	    	user.setDateOfBirth(updateRequest.getDateOfBirth());
//	    	user.setName(updateRequest.getName());
//	    	String userId = jwtUtil.extractUserId(token);
//	    	user.setId(userId);
//	    	userService.updateUser(user);
//	        return ResponseEntity.ok("User profile updated successfully");
//	    } catch (IllegalArgumentException e) {
//	        return ResponseEntity.badRequest().body(e.getMessage());
//	    }
		return ResponseEntity.ok("success");
    }
	
	// DTO (data transfer object) class to restrict updates to specific fields
	public static class UpdateUserRequest {
	    private String name;
	    private Date dateOfBirth;

	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }

	    public Date getDateOfBirth() { return dateOfBirth; }
	    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
	}

}
