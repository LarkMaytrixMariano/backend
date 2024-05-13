package com.fpedFIND.UserController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fpedFIND.Entity.User;
import com.fpedFIND.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;


@RestController
@RequestMapping("/profile-picture")
@CrossOrigin(origins = "*")
public class ProfilePictureController {

    @Autowired
    private UserService userService; // Assuming you have a UserService to interact with user data

    @GetMapping("/{userId}")
    public ResponseEntity<String> getProfilePictureBase64(@PathVariable Integer userId) {
        Optional<User> userOptional = Optional.ofNullable(userService.getUserById2(userId));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getProfilePicture() != null) {
                // Assuming profile picture is stored as a base64 encoded string
                return ResponseEntity.ok(user.getProfilePicture());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}