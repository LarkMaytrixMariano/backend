package com.fpedFIND.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpedFIND.Entity.File;
import com.fpedFIND.Entity.User;
import com.fpedFIND.Repository.FileRepository;
import com.fpedFIND.Repository.UserRepository;
import com.fpedFIND.Service.UserService;




@RestController
@RequestMapping("/tags")
public class TaggedUserController {

    @Autowired
    private UserService taggedUserService;

    @Autowired
    private FileRepository fileRepository; // Assuming you have FileRepository

    @Autowired
    private UserRepository userRepository; // Assuming you have UserRepository
    

    @PostMapping("/add")
    public ResponseEntity<String> addTag(
            @RequestParam Integer fileId,
            @RequestParam Integer userId
    ) {
        File file = fileRepository.findById(fileId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (file != null && user != null) {
            taggedUserService.addTag(fileId, userId);

            return ResponseEntity.ok("Tag added successfully");
        } else {
            return ResponseEntity.badRequest().body("File or User not found");
        }
    }

    @DeleteMapping("/remove/{tagId}")
    public ResponseEntity<String> removeTag(@PathVariable Long tagId) {
        taggedUserService.removeTag(tagId);
        return ResponseEntity.ok("Tag removed successfully");
    }
}


