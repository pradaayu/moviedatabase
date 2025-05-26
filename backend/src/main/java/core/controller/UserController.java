package core.controller;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.model.User;
import core.service.UserService;
import core.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import core.security.JwtUtil;

@RestController
@RequestMapping("/api/user/profile")
public class UserController {
	private final UserService userService;
	private final JwtUtil jwtUtil;
	
	public UserController(UserService userService, JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil; 
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<Object>> getUser(
			@RequestHeader("Authorization") String authorizationHeader, HttpServletResponse responsetp) {
			String accessToken = authorizationHeader.substring(7); // Remove "Bearer "
			String userId = jwtUtil.extractUserId(accessToken);
		    User user = userService.getUser(userId);
		    if (user == null) {
		    	return ResponseEntity.status(HttpStatus.NOT_FOUND)
		    			.body(new ApiResponse<>(false,"USER NOT FOUND",null));
		    }
		    UserDTO userDTO = new UserDTO(
			        user.getName(),
			        user.getAvatar(),
			        user.getDateOfBirth()
			);
	    	return ResponseEntity.status(HttpStatus.ACCEPTED)
	    			.body(new ApiResponse<>(true,null,userDTO));			
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

	public static class UserDTO {
	    private String name;
	    private String avatar;
	    private LocalDate dateOfBirth;

	    public UserDTO() {}

	    public UserDTO(String name, String avatar, LocalDate dateOfBirth) {
	        this.name = name;
	        this.avatar = avatar;
	        this.dateOfBirth = dateOfBirth;
	    }

	    // Getters and Setters
	    public String getName() { return name; }
	    public void setName(String name) { this.name = name; }

	    public String getAvatar() { return avatar; }
	    public void setAvatar(String avatar) { this.avatar = avatar; }

	    public LocalDate getDateOfBirth() { return dateOfBirth; }
	    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
	}

}
