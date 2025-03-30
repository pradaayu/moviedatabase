package controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.PostUpdate;
import jakarta.validation.Valid;
import model.User;
import service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable String id) {
	    try {
	        userService.deleteUser(id);
	        return ResponseEntity.ok("User deleted successfully");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateUser(@Valid @RequestBody User user) {
	    try {
	        // TODO
	        return ResponseEntity.ok("User profile updated successfully");
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
    }

}
