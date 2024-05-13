package com.fpedFIND.UserController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fpedFIND.DTO.CommentDTO;
import com.fpedFIND.DTO.UserDTO2;
import com.fpedFIND.Entity.CommentTagging;
import com.fpedFIND.Entity.User;
import com.fpedFIND.Paths.GlobalPaths;
import com.fpedFIND.Service.UserService;


@RestController
@CrossOrigin(origins = "*")
public class CommentController {
	
	
	@Autowired
	private UserService service;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	
	
    
    public CommentController(UserService service, SimpMessagingTemplate messagingTemplate) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
    }
	  
	  
	  
	  

	    // Endpoint to get all comments
	    @GetMapping("/comments")
	    public ResponseEntity<List<CommentTagging>> getAllComments() {
	        List<CommentTagging> comments = service.getAllComments();
	        return new ResponseEntity<>(comments, HttpStatus.OK);
	    }
	    
	    @GetMapping("/comments/{fileId}")
	    public ResponseEntity<List<CommentDTO>> getCommentsForFile(@PathVariable Integer fileId) {
	        List<CommentTagging> comments = service.getCommentsForFile(fileId);
	        
	        List<CommentDTO> commentDTOs = new ArrayList<>();
	        for (CommentTagging comment : comments) {
	            CommentDTO commentDTO = new CommentDTO();
	            commentDTO.setId(comment.getId());
	            commentDTO.setFileId(comment.getFileId());
	            commentDTO.setAttachmentName(comment.getAttachmentName());
	            commentDTO.setAttachmentFilePath(comment.getAttachmentfilePath());
	            commentDTO.setContent(comment.getContent());
	            commentDTO.setUser(convertToDto(comment.getUser()));

	            // Convert Object[] timestamp to LocalDateTime
	            Object[] timestampArray = comment.getTimestamp();
	            LocalDateTime timestamp = LocalDateTime.of(
	                (int) timestampArray[0],    // Year
	                (int) timestampArray[1],    // Month
	                (int) timestampArray[2],    // Day
	                (int) timestampArray[3],    // Hour
	                (int) timestampArray[4],    // Minute
	                (int) timestampArray[5],    // Second
	                (int) timestampArray[6]     // Nano
	            );
	            commentDTO.setTimestamp(timestamp);
	            
	            commentDTOs.add(commentDTO);
	        }

	        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
	    }
	    

	    public UserDTO2 convertToDto(User user) {
	    	UserDTO2 userDto = new UserDTO2();
	        userDto.setUser_id(user.getUser_id());
	        userDto.setUsername(user.getUsername());
	        userDto.setFirstname(user.getFirstname());
	        userDto.setLastname(user.getLastname());
	        userDto.setProfilePicture(user.getProfilePicture());        
	        // Set other fields as needed
	        return userDto;
	    }
	    
	    
	    
	    @PostMapping("/comment/sendcomment")
	    public ResponseEntity<String> addCommentWithFile(
	            @RequestParam(value = "file", required = false) MultipartFile file,
	            @RequestParam("user") Integer user_id,
		        @RequestParam("file_id") Integer file_id,
	            @RequestParam("content") String content) {

	         User user = service.findByUserId(user_id);
	        if	 (user == null) {
	            return ResponseEntity.badRequest().body("User with ID " + user_id + " does not exist.");
	        }

	        try {
	        	
	            CommentTagging fileEntity = new CommentTagging();
	            fileEntity.setUser(new User(user.getUser_id(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getProfilePicture())); // Set only required user details
	            fileEntity.setFileId(file_id);
	            fileEntity.setTimestamp(LocalDateTime.now());
	            fileEntity.setContent(content);
	            String username = user.getUsername();
	            String originalFileName = (file != null) ? file.getOriginalFilename() : null;

	  

	            if (file != null && !file.isEmpty()) {

	                String uploadDir = GlobalPaths.UPLOAD_DIR;


	                String userFolder = uploadDir + username + "/";
	                String originalFilePath = userFolder + originalFileName;

	                int count = 1;
	                while (new java.io.File(originalFilePath).exists()) {
	                    // If file with the same name exists, modify the filename to make it unique
	                    String newFileName = getUniqueFileName(originalFileName, count);
	                    originalFilePath = userFolder + newFileName;
	                    count++;
	                }

	                fileEntity.setAttachmentName(originalFileName);
	                fileEntity.setAttachmentfilePath(originalFilePath);

	                java.io.File newFile = new java.io.File(originalFilePath);
	                CommentTagging saved1Comment = service.createComment(fileEntity);
	                messagingTemplate.convertAndSend("/topic/comments", saved1Comment);


	                Path userDirectory = Paths.get(userFolder);
	                if (!Files.exists(userDirectory)) {
	                    Files.createDirectories(userDirectory);
	                    System.out.println("User directory created successfully: " + userFolder);
	                }

	                file.transferTo(newFile);
	            } else {
	                // No file provided, handle accordingly (e.g., do not check for existing files)
	                fileEntity.setAttachmentName(null);
	                fileEntity.setAttachmentfilePath(null);
	                CommentTagging savedComment = service.createComment(fileEntity);
	                // Send the comment to the WebSocket destination for real-time updates
	                messagingTemplate.convertAndSend("/topic/comments", savedComment);
	            }

	            return ResponseEntity.ok("Comment added successfully.");
	            } catch (IOException e) {
	                e.printStackTrace();
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Comment upload failed.");
	            }
	        }

	            // Helper method to generate a unique filename
	            private String getUniqueFileName(String originalFileName, int count) {
	                String baseFileName = FilenameUtils.getBaseName(originalFileName);
	                String fileExtension = FilenameUtils.getExtension(originalFileName);
	                return baseFileName + "(" + count + ")." + fileExtension;
	            }
	            
	            
	            @DeleteMapping("/comments/{id}")
	            public ResponseEntity<String> deleteCommentById(@PathVariable Long id) {
	            	service.deleteComment(id);
	                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Comment with ID " + id + " has been deleted.");
	            }

	    
	    
	       


}
