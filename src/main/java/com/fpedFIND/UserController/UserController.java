package com.fpedFIND.UserController;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fpedFIND.DTO.UserDTO4;
import com.fpedFIND.DTO.UserDto;
import com.fpedFIND.DTO.UserStatusDTO;
import com.fpedFIND.Entity.LastActivityRequest;
import com.fpedFIND.Entity.User;
import com.fpedFIND.Entity.UserProfileUpdateRequest;
import com.fpedFIND.Repository.UserRepository;
import com.fpedFIND.Service.UserService;







@RestController
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserRepository repo;

	@Autowired
	private UserService service;
	
	

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // Autowire SimpMessagingTemplate

    
    
    private List<User> filterResignedUsers(List<User> users) {
        List<User> filteredUsers = new ArrayList<>();
        for (User user : users) {
            if (!user.isInactive()) { // Check if user is not resigned
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }
    
	
	// THIS IS FOR CRUD OPERATION // CREATE, READ, UPDATE, AND DELETE
    @Cacheable("allUsers")
    @GetMapping("/user/Alluser")
    public List<User> getUser() {
        List<User> users = service.getUser();
        return filterResignedUsers(users);
    }
    

    @GetMapping("/user/Allusernopic")
    public List<UserDto> withnoPic() {
        List<User> users = service.getUser();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
        	if(!user.isInactive()) {
            UserDto dto = new UserDto();
            dto.setUser_id(user.getUser_id());
            dto.setUsername(user.getUsername());
            dto.setSectionName(user.getSectionName());
            dto.setFirstname(user.getFirstname());
            dto.setLastname(user.getLastname());
            dto.setRole_name(user.getRole_name());
            dto.setBirthday(user.getBirthday());
            dto.setStatus(user.getStatus());
            dto.setHasAccess(user.getHasAccess());
            dto.setEmail(user.getEmail());
            dto.setLastActive(user.getLastActive());
            userDtos.add(dto);
        	}
        }
        return userDtos;
    }
    
    @GetMapping("/active-users")
    public Map<String, Object> getActiveUsers() {
        Map<String, Object> response = new HashMap<>();
        List<UserDto> activeUsers = service.getActiveUsers();
        int activeUserCount = activeUsers.size();
        response.put("count", activeUserCount);
        response.put("users", activeUsers);
        return response;
    }
    
	
	
	 @GetMapping("user/birthdays")
	    public List<UserDTO4> getUsersBirthdays() {
	        List<User> users = service.getUser();
	        List<UserDTO4> userDtos = new ArrayList<>();
	        for (User user : users) {
	        	if(!user.isInactive()) {
	        	UserDTO4 dto = new UserDTO4();
	            dto.setUser_id(user.getUser_id());
	            dto.setFirstname(user.getFirstname());
	            dto.setLastname(user.getLastname());
	            dto.setBirthday(user.getBirthday());
	            userDtos.add(dto);
	        	}
	        }
	        return userDtos;
	    }
	@PostMapping("/user/addnew")
		public String saveUser(@RequestParam("profilePicture") MultipartFile profilePicture,
				@RequestParam("firstname") String firstname,
				@RequestParam("lastname") String lastname,
				@RequestParam("username") String username,
				@RequestParam("password") String password,
				@RequestParam("email") String email,
				@RequestParam("sectionName") String sectionName,
				@RequestParam("department") String department,
				@RequestParam("role_name") String role_name,
		        @RequestParam("birthday")  LocalDate birthday)
				
		{
		service.saveUserToDB(profilePicture, username, password, email, sectionName, firstname, lastname, department, role_name, birthday);
			 return null;
		}
	
	
	
	@PutMapping("/user/{id}/edit")
	public void updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
		service.updateUser(user);

	}
	
	@DeleteMapping("/user/{id}/delete")
	public void deleteUser(@PathVariable("id") Integer id) {
		service.deleteUser(id);
	}
	
	//// END OF CRUD OPERATION ////
	
	@PostMapping("/user/login")
	public ResponseEntity<?> loginUser(@RequestBody User userData) {
	    User user = repo.findByUsername(userData.getUsername());

	    if (user != null && service.checkPassword(userData.getPassword(), user.getPassword())) {
	     //   System.out.println("Login Successfully!");
	        user.setStatus("online");
            user.setLastActive(LocalDateTime.now());
	        repo.save(user);
	        
	        // Create UserStatusDTO with userId and status
            UserStatusDTO userStatusDTO = new UserStatusDTO();
            userStatusDTO.setUser_id(user.getUser_id());
            userStatusDTO.setStatus(user.getStatus());
            userStatusDTO.setFirstname(user.getFirstname());
            userStatusDTO.setLastname(user.getLastname());

            // Send UserStatusDTO via messagingTemplate
            messagingTemplate.convertAndSend("/topic/userStatus", userStatusDTO);
            
	        // Check the user's role (case-insensitive)
	        String userRole = user.getRole_name().toLowerCase();

	        if ("admin".equals(userRole)) {
	            // ADMIN ROLE
	            return ResponseEntity.ok(user);
	        } else if ("user".equals(userRole)) {
	            // User is a regular user
	            return ResponseEntity.ok(user);
	        } else if ("superadmin".equals(userRole)) {
	            // SuperAdmin ROLE
	            return ResponseEntity.ok(user);
	        } else {
	            // Invalid role (you can handle this case as needed)
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{\"error\": \"Invalid Role\"}");
	        }
	    } else if (user == null) {
	     //   System.out.println("User Does Not Exist");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User Does Not Exist\"}");
	    } else {
	      //  System.out.println("Invalid Username/Password");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Invalid Username/Password\"}");
	    }
	}


	    // Logout endpoint
	    @PostMapping("/user/logout")
	    public ResponseEntity<?> logoutUser(@RequestBody User userData) {
	        User user = repo.findByUsername(userData.getUsername());
	        if (user != null) {
	            // Update user status to "offline" upon logout
	            user.setStatus("offline");
	            user.setLastActive(LocalDateTime.now());
	            repo.save(user);
	            // Create UserStatusDTO with userId and status
	            UserStatusDTO userStatusDTO = new UserStatusDTO();
	            userStatusDTO.setUser_id(user.getUser_id());
	            userStatusDTO.setStatus(user.getStatus());
	            userStatusDTO.setFirstname(user.getFirstname());
	            userStatusDTO.setLastname(user.getLastname());

	            // Send UserStatusDTO via messagingTemplate
	            messagingTemplate.convertAndSend("/topic/userStatus", userStatusDTO);
	            
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User Does Not Exist\"}");
	        }
	    }
	        
	    // Update user status to "offline" upon session expiration
	    @PostMapping("/user/sessionExpired")
	    public ResponseEntity<?> sessionExpired(@RequestBody User userData) {
	        User user = repo.findByUsername(userData.getUsername());
	        if (user != null) {
	            // Update user status to "idle" upon session expiration
	            user.setStatus("idle");
	            repo.save(user);
	            
	            // Create UserStatusDTO with userId and status
	            UserStatusDTO userStatusDTO = new UserStatusDTO();
	            userStatusDTO.setUser_id(user.getUser_id());
	            userStatusDTO.setStatus(user.getStatus());
	            userStatusDTO.setFirstname(user.getFirstname());
	            userStatusDTO.setLastname(user.getLastname());


	            // Send UserStatusDTO via messagingTemplate
	            messagingTemplate.convertAndSend("/topic/userStatus", userStatusDTO);
	            
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User Does Not Exist\"}");
	        }
	    }
	    
	    
	    // Update user status to "offline" upon session expiration
	    @PostMapping("/user/sessionOnline")
	    public ResponseEntity<?> sessionOnline(@RequestBody User userData) {
	        User user = repo.findByUsername(userData.getUsername());
	        if (user != null) {
	            // Update user status to "offline" upon session expiration
	            user.setStatus("online");
	            repo.save(user);
	            // Create UserStatusDTO with userId and status
	            UserStatusDTO userStatusDTO = new UserStatusDTO();
	            userStatusDTO.setUser_id(user.getUser_id());
	            userStatusDTO.setStatus(user.getStatus());
	            userStatusDTO.setFirstname(user.getFirstname());
	            userStatusDTO.setLastname(user.getLastname());
	            // Send UserStatusDTO via messagingTemplate
	            messagingTemplate.convertAndSend("/topic/userStatus", userStatusDTO);
	            
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User Does Not Exist\"}");
	        }
	    }
	    
	    
	    
	    @PostMapping("/update-last-activity")
	    public ResponseEntity<String> updateLastActivity(@RequestBody LastActivityRequest request) {
	    	repo.updateLastActive(request.getUserId(), request.getLastActivityTime());
	        return new ResponseEntity<>("Last activity updated successfully", HttpStatus.OK);
	        
	    }
	    
	    
	 @GetMapping("/users/{userId}")
	    public ResponseEntity<UserDto> findByUserId(@PathVariable Integer userId) {
	        User user = service.findByUserId(userId);
	        if (user != null) {
	        	UserDto userDTO = convertToUserDto(user);
	            return ResponseEntity.ok(userDTO);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	  
	  
	  
	   // Method to convert User entity to UserDTO
	    private UserDto convertToUserDto(User user) {
	        UserDto userDTO = new UserDto();
	        userDTO.setUser_id(user.getUser_id());
	        userDTO.setFirstname(user.getFirstname());
	        userDTO.setLastname(user.getLastname());
	        userDTO.setUsername(user.getUsername());
	        //userDTO.setRole_name(user.getRole_name());
	       // userDTO.setBirthday(user.getBirthday());
	        userDTO.setSectionName(user.getSectionName());
	        userDTO.setHasAccess(user.getHasAccess());
	        userDTO.setStatus(user.getStatus());

	        // Set other properties as needed
	        return userDTO;
	    }
	  
	  
	  
	// Handle profile update
	  @PutMapping("/user/{userId}/profile")
	  public ResponseEntity<String> updateUserProfile(
	      @PathVariable("userId") Integer userId,
	      @RequestBody UserProfileUpdateRequest request) {

	      // Retrieve the user from the repository by userId
	      User existingUser = service.findByUserId(userId);

	      if (existingUser != null) {
	          // Check if the old password matches
	          if (service.checkPassword(request.getOldPassword(), existingUser.getPassword())) {
	              // Check if the new password is empty
	              if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
	                  return ResponseEntity.badRequest().body("Please input your new password");
	              } else {
	                  // Update the user profile
	                  service.updateUserProfile(existingUser, request);
	                  return ResponseEntity.ok("User profile updated successfully");
	              }
	          } else {
	              return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
	          }
	      } else {
	          return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	      }
	  }

	    
	 // Update user profile picture
	    @PutMapping(value = "/user/{userId}/profilepicture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	    public ResponseEntity<Object> updateUserProfilepicture(
	            @PathVariable("userId") Integer userId,
	            @RequestParam("profilePicture") MultipartFile profilePicture) {

	        // Retrieve the user from the repository by userId
	        User existingUser = service.findByUserId(userId);

	        if (existingUser != null) {
	            // Update profile picture if provided
	            if (profilePicture != null && !profilePicture.isEmpty()) {
	                try {
	                    // Convert profile picture to base64 string
	                    String base64Image = Base64.getEncoder().encodeToString(profilePicture.getBytes());
	                    existingUser.setProfilePicture(base64Image);
	                } catch (IOException e) {
	                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process profile picture");
	                }
	            }

	            // Save the updated user to the repository
	            service.updateUserProfilepicture(existingUser);
	            
	            // Return a success response
	            return ResponseEntity.ok().build();
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	        }
	    }
	    
	    
	    
	 // Handle old password check
	    @PostMapping("/user/{userId}/check-password")
	    public ResponseEntity<Boolean> checkOldPassword(
	        @PathVariable("userId") Integer userId,
	        @RequestBody UserProfileUpdateRequest request) {

	        // Retrieve the user from the repository by userId
	        User existingUser = service.findByUserId(userId);

	        if (existingUser != null) {
	            // Check if the old password matches
	            boolean isPasswordCorrect = service.checkPassword(request.getOldPassword(), existingUser.getPassword());
	            return ResponseEntity.ok(isPasswordCorrect);
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    }
	    
	  ///HAS ACCESS
	  	    @RequestMapping(value = "/users/{userId}/access", method = RequestMethod.PUT)
	  	    public ResponseEntity<?> toggleAccess(@PathVariable("userId") Integer userId, @RequestBody Boolean hasAccess) {
	  	        service.toggleAccess(userId, hasAccess);
	  	        return ResponseEntity.ok().build();
	  	    }
	  	    
	  	  @GetMapping("/user/profilePicturebyId/{userId}")
	  	public ResponseEntity<String> getProfilePictureByUserId(@PathVariable("userId") String userId) {
	  	    try {
	  	        Optional<User> optionalUser = repo.findByUserId(userId);
	  	        if (optionalUser.isPresent()) {
	  	            User user = optionalUser.get();
	  	            String profilePicture = user.getProfilePicture();

	  	             // Assuming your profile picture is stored as a Base64-encoded string
	                HttpHeaders headers = new HttpHeaders();
	                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	                return new ResponseEntity<>(profilePicture, headers, HttpStatus.OK);
	            } else {
	                // If there is no space, consider the entire sender as the first name
	               // String firstname = sender;
	                String profilePicture = service.getProfilePictureByUserId(userId);

	                HttpHeaders headers = new HttpHeaders();
	                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	                return new ResponseEntity<>(profilePicture, headers, HttpStatus.OK);
	            }
	        } catch (Exception e) {
	            // Add logging statements for exceptions
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	    
	  	
	    
	    
	    @GetMapping("/user/profilePicture/{sender}")
	    public ResponseEntity<String> getProfilePictureBySender(@PathVariable("sender") String sender) {
	        try {
	            // Split the sender's name at the last space
	            int lastSpaceIndex = sender.lastIndexOf(" ");
	            
	            if (lastSpaceIndex != -1) {
	                String firstname = sender.substring(0, lastSpaceIndex);
	                String lastname = sender.substring(lastSpaceIndex + 1);

	                // Add logging statements
	              //  System.out.println("Received request for profile picture with sender: " + sender);
	              //  System.out.println("First name: " + firstname);
	              //  System.out.println("Last name: " + lastname);

	                String profilePicture = service.getProfilePictureByFullname(firstname, lastname);

	                // Assuming your profile picture is stored as a Base64-encoded string
	                HttpHeaders headers = new HttpHeaders();
	                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	                return new ResponseEntity<>(profilePicture, headers, HttpStatus.OK);
	            } else {
	                // If there is no space, consider the entire sender as the first name
	                String firstname = sender;
	                String profilePicture = service.getProfilePictureByFullname(firstname, null);

	                HttpHeaders headers = new HttpHeaders();
	                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

	                return new ResponseEntity<>(profilePicture, headers, HttpStatus.OK);
	            }
	        } catch (Exception e) {
	            // Add logging statements for exceptions
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	    
	    
	    
	    
	    
	    }
