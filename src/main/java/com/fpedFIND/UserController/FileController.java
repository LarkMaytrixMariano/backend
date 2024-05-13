package com.fpedFIND.UserController;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.fpedFIND.DTO.FileDTO;
import com.fpedFIND.DTO.FileStatusNameUpdateRequest;
import com.fpedFIND.DTO.UserDto3;
import com.fpedFIND.Entity.File;
import com.fpedFIND.Entity.User;
import com.fpedFIND.Paths.GlobalPaths;
import com.fpedFIND.Repository.FileRepository;
import com.fpedFIND.Service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;




@RestController
@CrossOrigin(origins = "*")
public class FileController {

	
	
	@Autowired
	private UserService fileservice;
	
	
	@Autowired
	private FileRepository repo;

	//@Cacheable(cacheNames = "allFiles")
	   @GetMapping("/file/Allfile")
    public ResponseEntity<List<FileDTO>> getAllFilesWithUserDetails() {
        List<FileDTO> files = fileservice.getAllFilesWithUserDetails()
                                          .stream()
                                          .sorted(Comparator.comparing(FileDTO::getUploadDateTime).reversed())
                                          .collect(Collectors.toList());
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
	 
	// @Cacheable("fileCache")
	// @GetMapping("/all-with-user-details")
	// public ResponseEntity<Page<FileDTO>> getAllFilesWithUserDetails(
	  //   @PageableDefault(size = 10, sort = "uploadDateTime", direction = Sort.Direction.DESC) Pageable pageable) {
	    // Page<FileDTO> filesPage = fileservice.getAllFilesWithUserDetails(pageable);
	    // return new ResponseEntity<>(filesPage, HttpStatus.OK);
	// }
	
	@PostMapping("/file/upload")
	public ResponseEntity<Object> uploadFile(
	        @RequestParam("file") MultipartFile file,
	        @RequestParam("description") String description,
	        @RequestParam("remarks") String remarks,
	        @RequestParam("dts") String dts,
	        @RequestParam("statusName") String statusName,
	        @RequestParam("categoryName") String categoryName,
	        @RequestParam("sectionName") String sectionName,
	        @RequestParam("user_id") Integer user_id,
	        @RequestParam("filetitle") String filetitle,
	        @RequestParam(value = "deadline", required = false)  LocalDate deadline,
	        @RequestParam(value = "taggedUsers", required = false) String taggedUsers
			) {

	    User user = fileservice.findByUserId(user_id);
	    if (user == null) {
	        return ResponseEntity.badRequest().body("User with ID " + user_id + " does not exist.");
	    }

	    if (file.isEmpty()) {
	        return ResponseEntity.badRequest().body("Please select a file to upload.");
	    }

	    try {
	        File fileEntity = new File();
	        fileEntity.setDescription(description);
	        fileEntity.setRemarks(remarks);
	        fileEntity.setDts(dts);
	        fileEntity.setStatusName(statusName);
	        fileEntity.setCategoryName(categoryName);
	        fileEntity.setSectionName(sectionName);
	        fileEntity.setUser(user);
	        fileEntity.setUploadDateTime(LocalDateTime.now());
	        fileEntity.setFiletitle(filetitle);
	        fileEntity.setDeadline(deadline);
	        
	        // Adding tagged users if available
	        if (taggedUsers != null && !taggedUsers.isEmpty()) {
	            // Splitting tagged users by comma and adding them to the fileEntity
	            List<String> taggedUserIds = Arrays.asList(taggedUsers.split(","));
	            fileEntity.addMultipleTaggedUsers(taggedUserIds);
	        }
	        
	        String username = user.getUsername();
	        String originalFileName = file.getOriginalFilename();
	        fileEntity.setFileName(originalFileName);
	   
	        
            String uploadDir = GlobalPaths.UPLOAD_DIR;


	        List<User> allUsers = fileservice.getUser();

	     // Inside your method
	        for (User currentUser : allUsers) {
	            String currentUserFolder = uploadDir + currentUser.getUsername() + "/";
	            String currentUserFilePath = currentUserFolder + originalFileName;

	            java.io.File existingFile = new java.io.File(currentUserFilePath);
	            if (existingFile.exists()) {
	                File existingFileEntity = fileservice.findFileByFileName(originalFileName);
	                if (existingFileEntity != null) {
	                    Integer existingFileId = existingFileEntity.getFileId();
	                    // Return a JSON response with the error message
	                    return ResponseEntity
	                        .status(HttpStatus.BAD_REQUEST)
	                        .body("{\"error\": \"A file with the same name already exists for another user\", \"fileId\": " + existingFileId + "}");
	                }
	            }
	        }

	        String userFolder = uploadDir + username + "/";
	        String filePath = userFolder + originalFileName;
	        fileEntity.setFilePath(filePath);

	        java.io.File newFile = new java.io.File(filePath);
	        fileservice.addFile(fileEntity);
	        File savedFileEntity = fileservice.addFile(fileEntity);
	        Integer fileId = savedFileEntity.getFileId();

	        Path userDirectory = Paths.get(userFolder);
	        if (!Files.exists(userDirectory)) {
	            Files.createDirectories(userDirectory);
	            System.out.println("User directory created successfully: " + userFolder);
	        }

	        file.transferTo(newFile);
	        
	        Map<String, Object> responseMap = new HashMap<>();
	        responseMap.put("fileId", fileId);
	        responseMap.put("message", "File uploaded successfully.");
	        return ResponseEntity.ok(responseMap);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
	    }
	}
	
	// FOR FETCHING FILES BY TAGGED //
	
	   @GetMapping("/tagged/{userId}")
	    public ResponseEntity<List<FileDTO>> getFilesByTaggedUsers(@PathVariable String userId) {
	        List<FileDTO> fileDTOs = fileservice.findByTaggedUsers(userId);
	        return new ResponseEntity<>(fileDTOs, HttpStatus.OK);
	    }
	   
	   @PutMapping("/file/{id}/edit")
	   public void updateFileStatusName(@PathVariable("id") Integer id, @RequestBody FileStatusNameUpdateRequest request) {
	       String updatedStatus = request.getStatusName();
	       fileservice.updateFileStatusName(id, updatedStatus);
	       // System.out.println("UPDATED STATUS: " + updatedStatus);

	       // Update statusChanged field only if the status is "Completed"
	       if ("Completed".equalsIgnoreCase(updatedStatus)) {
	    	   LocalDateTime currentTime = LocalDateTime.now();
	           File file = fileservice.getFileById(id); // Retrieve the file from the service or repository
	           if (file != null) {
	               file.setStatusChanged(currentTime);
	               repo.save(file); // Save the updated file
	           } else {
	               // Handle case where file with given id is not found
	               System.out.println("File with id " + id + " not found.");
	           }
	       }
	   }
	   
	
	//FILE TITLE
	@PutMapping("/files/{fileId}/title")
	public ResponseEntity<String> updateFiletitle(@PathVariable Integer fileId, @RequestBody Map<String, String> body) {
	    String newFiletitle = body.get("filetitle");
	    if (newFiletitle == null || newFiletitle.isEmpty()) {
	        return ResponseEntity.badRequest().body("New file title is missing.");
	    }

	    try {
	        File file = fileservice.getFileById(fileId);
	        if (file != null) {
	            file.setFiletitle(newFiletitle);
	            fileservice.updateFile(file);
	            return ResponseEntity.ok("File title updated successfully.");
	        } else {
	            return ResponseEntity.notFound().build();
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update file title.");
	    }
	}
	

	
	
	
	@DeleteMapping("/file/{id}/delete")
	public void deleteFIle(@PathVariable("id") Integer id) {
		fileservice.deleteFile(id);
	}
	
	
	  // Retrieve files by user_id
	 @GetMapping("/filesByUser/{userId}")
	    public ResponseEntity<List<FileDTO>> getFilesByUserId(@PathVariable Integer userId) {
	        List<File> files = fileservice.getFilesByUserIdSortedByDate(userId);
	        
	        if (files.isEmpty()) {
	            return ResponseEntity.noContent().build();
	        } else {
	            List<FileDTO> fileDTOs = files.stream()
	                    .map(this::convertToFileDTO)
	                    .collect(Collectors.toList());
	            return ResponseEntity.ok(fileDTOs);
	        }
	    }
	 
	 
	 @GetMapping("/file/view/{username}/{fileName:.+}")
	 public ResponseEntity<Resource> viewFile(@PathVariable String username, @PathVariable String fileName) {
	     // Define the base directory where the files are stored

	        String uploadDir = GlobalPaths.UPLOAD_DIR;


	     // Construct the full file path based on the provided username and file name
	     String userFolder = uploadDir + username + "/";
	     String filePath = userFolder + fileName;

	     try {
	         // Load the file as a resource
	         Path fileFullPath = Paths.get(filePath);
	         Resource resource = new UrlResource(fileFullPath.toUri());

	         if (resource.exists() && resource.isReadable()) {
	             return ResponseEntity.ok()
	                     .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + resource.getFilename()) // Change "attachment" to "inline"
	                     .contentLength(resource.contentLength())
	                     .contentType(MediaType.APPLICATION_PDF) // Set the content type for PDF
	                     .body(resource);
	         } else {
	             return ResponseEntity.notFound().build();
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	     }
	 }
	 
	 
	 // DOWNLOAD FILE
	 @GetMapping("/file/download/{username}/{fileName:.+}")
	 public ResponseEntity<Resource> downloadFile(@PathVariable String username, @PathVariable String fileName) {
	     // Define the base directory where the files are stored
	     String uploadDir = GlobalPaths.UPLOAD_DIR;

	     // Construct the full file path based on the provided username and file name
	     String userFolder = uploadDir + username + "/";
	     String filePath = userFolder + fileName;

	     try {
	         // Load the file as a resource
	         Path fileFullPath = Paths.get(filePath);
	         Resource resource = new UrlResource(fileFullPath.toUri());

	         if (resource.exists() && resource.isReadable()) {
	             // Encode the filename using URL encoding
	             String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8)
	                     .replace("[", "%5B").replace("]", "%5D"); // Encoding brackets explicitly

	             return ResponseEntity.ok()
	                     .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
	                     .contentLength(resource.contentLength())
	                     .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                     .body(resource);
	         } else {
	             return ResponseEntity.notFound().build();
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	     }
	 }
	 
	 @GetMapping("/file/views/{username}/{fileName:.+}")
	 public ResponseEntity<Resource> ViewFile(@PathVariable String username, @PathVariable String fileName) {
	     // Define the base directory where the files are stored
	     String uploadDir = GlobalPaths.UPLOAD_DIR;

	     // Construct the full file path based on the provided username and file name
	     String userFolder = uploadDir + username + "/";
	     String filePath = userFolder + fileName;

	     try {
	         // Load the file as a resource
	         Path fileFullPath = Paths.get(filePath);
	         Resource resource = new UrlResource(fileFullPath.toUri());

	         if (resource.exists() && resource.isReadable()) {
	             // Encode the filename using URL encoding
	             String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8)
	                     .replace("[", "%5B").replace("]", "%5D"); // Encoding brackets explicitly

	             // Determine the content type based on file extension
	             String contentType;
	             if (fileName.toLowerCase().endsWith(".pdf")) {
	                 contentType = MediaType.APPLICATION_PDF_VALUE;
	             } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
	                 contentType = MediaType.IMAGE_JPEG_VALUE;
	             } else if (fileName.toLowerCase().endsWith(".png")) {
	                 contentType = MediaType.IMAGE_PNG_VALUE;
	             }else if (fileName.toLowerCase().endsWith(".img")) {
	                 contentType = MediaType.IMAGE_PNG_VALUE;
	             }
	             else {
	                 // Default to octet-stream for unknown types
	                 contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
	             }

	             return ResponseEntity.ok()
	                     .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + encodedFileName + "\"")
	                     .contentLength(resource.contentLength())
	                     .contentType(MediaType.parseMediaType(contentType)) // Set content type based on file type
	                     .body(resource);
	         } else {
	             return ResponseEntity.notFound().build();
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	     }
	 }
	 
	   @GetMapping("file/{fileId}")
	    public ResponseEntity<FileDTO> getFileById(@PathVariable Integer fileId) {
	        // Assuming getFileById directly returns File object
	        File file = fileservice.getFileById(fileId);
	        if (file != null) {
	            FileDTO fileDTO = convertToFileDTO(file);
	            return new ResponseEntity<>(fileDTO, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	   
	   
	   @GetMapping("fileName/{fileName}")
	    public ResponseEntity<FileDTO> getFileByFileName(@PathVariable String fileName) {
	        // Assuming getFileById directly returns File object
	        File file = fileservice.getFileByFileName(fileName);
	        if (file != null) {
	            FileDTO fileDTO = convertToFileDTO(file);
	            return new ResponseEntity<>(fileDTO, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    }
	   
	   
	    // Method to convert File entity to FileDTO
	    private FileDTO convertToFileDTO(File file) {
	        FileDTO fileDTO = new FileDTO();
	        fileDTO.setFileId(file.getFileId());
	        fileDTO.setDescription(file.getDescription());
	        fileDTO.setRemarks(file.getRemarks());
	        fileDTO.setFileName(file.getFileName());
	        fileDTO.setFilePath(file.getFilePath());
	        fileDTO.setDts(file.getDts());
	        fileDTO.setStatusName(file.getStatusName());
	        fileDTO.setCategoryName(file.getCategoryName());
	        fileDTO.setSectionName(file.getSectionName());
	        fileDTO.setFiletitle(file.getFiletitle());
	        fileDTO.setUploadDateTime(file.getUploadDateTime());
	        fileDTO.setDeadline(file.getDeadline());
	        // Convert User entity to UserDto
	        UserDto3 userDto = new UserDto3();
	        User user = file.getUser();
	        if (user != null) {
	            userDto.setUser_id(user.getUser_id());
	            userDto.setFirstname(user.getFirstname());
	            userDto.setLastname(user.getLastname());
	            userDto.setUsername(user.getUsername());
	            userDto.setRole_name(user.getRole_name());
	        }
	        fileDTO.setUser(userDto);
	        return fileDTO;
	    }
	    // FOR TAGGING//	    
	    // Endpoint to tag a user to a file
	    // Endpoint for single tagging
	    @PutMapping("/files/{fileId}/tag")
	    public ResponseEntity<String> tagUserToFile(@PathVariable Integer fileId, @RequestParam String userId) {
	        try {
	            File file = fileservice.getFileById(fileId);
	            if (file != null) {
	                String taggedUsers = file.getTaggedUsers();
	                if (taggedUsers == null || taggedUsers.isEmpty()) {
	                    taggedUsers = userId;
	                } else {
	                    taggedUsers += "," + userId;
	                }
	                file.setTaggedUsers(taggedUsers);
	                fileservice.updateFile(file);
	                // Return response as plain text
	                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("User tagged successfully.");
	            } else {
	                return ResponseEntity.notFound().build();
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to tag user.");
	        }
	    }
	    
	    
	    // Endpoint for multiple tagging
	    @PutMapping("/files/{fileId}/tag-multiple")
	    public ResponseEntity<String> tagUsersToFile(@PathVariable Integer fileId, @RequestParam List<String> userIds) {
	        try {
	            File file = fileservice.getFileById(fileId);
	            if (file != null) {
	                file.addMultipleTaggedUsers(userIds); // Add multiple tagged users
	                fileservice.updateFile(file);
	                // Return response as plain text
	                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Users tagged successfully.");
	            } else {
	                return ResponseEntity.notFound().build();
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to tag users.");
	        }
	    }
	    
	    // Endpoint for multiple tagging all users
	    @PutMapping("/files/{fileId}/tag-all")
	    public ResponseEntity<String> tagAllUsersToFile(@PathVariable Integer fileId) {
	        try {
	            // Retrieve all user IDs as a comma-separated string
	            String allUserIds = fileservice.getAllUserIdsAsString();

	            File file = fileservice.getFileById(fileId);
	            if (file != null) {
	                // Update the tagged users list with all user IDs
	                file.setTaggedUsers(allUserIds);
	                fileservice.updateFile(file);
	                // Return response as plain text
	                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("All users tagged successfully.");
	            } else {
	                return ResponseEntity.notFound().build();
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to tag all users.");
	        }
	    }
	    
	   
	    // Endpoint to untag a user from a file
	    @PutMapping("/files/{fileId}/untag")
	    public ResponseEntity<String> untagUserFromFile(@PathVariable Integer fileId, @RequestParam String userId) {
	        try {
	            File file = fileservice.getFileById(fileId);
	            if (file != null) {
	                String taggedUsers = file.getTaggedUsers();
	                if (taggedUsers != null && !taggedUsers.isEmpty()) {
	                    List<String> userList = new ArrayList<>(Arrays.asList(taggedUsers.split(",")));
	                    if (userList.contains(userId)) {
	                        userList.remove(userId);
	                        taggedUsers = String.join(",", userList);
	                        file.setTaggedUsers(taggedUsers);
	                        // Update the file after modification
	                        fileservice.updateFile(file);
	                        return ResponseEntity.ok("User untagged successfully.");
	                    } else {
	                        return ResponseEntity.badRequest().body("User not tagged to this file.");
	                    }
	                } else {
	                    return ResponseEntity.badRequest().body("No tagged users found for this file.");
	                }
	            } else {
	                return ResponseEntity.notFound().build();
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to untag user.");
	        }
	    }
	    
	    
	    // Endpoint to get tagged users for a	 file
	    @GetMapping("/files/{fileId}/taggedUsers")
	    public ResponseEntity<List<String>> getTaggedUsersForFile(@PathVariable Integer fileId) {
	        List<String> taggedUsers = fileservice.getTaggedUsersForFile(fileId);
	        return ResponseEntity.ok(taggedUsers);
	    }
	    
	    
	    @Autowired
	    private SimpMessagingTemplate messagingTemplate;
	    
	    @PutMapping("files/{fileId}/deadline")
	    public File updateDeadlineForFile(@PathVariable Integer fileId, @RequestParam("deadline") String deadline) {
	        LocalDate newDeadline = LocalDate.parse(deadline);
	        File updatedFile = fileservice.updateDeadline(fileId, newDeadline);
	        
	        // Send notification to WebSocket topic
	        messagingTemplate.convertAndSend("/topic/deadline", updatedFile);
	        
	        return updatedFile;
	    }
	    
	    // GET endpoint to fetch deadline for a specific file
	    @GetMapping("/files/{fileId}/deadline")
	    public ResponseEntity<String> getDeadlineForFile(@PathVariable Integer fileId) {
	    	LocalDate deadline = fileservice.getDeadlineForFile(fileId);
	        if (deadline != null) {
	            return ResponseEntity.ok(deadline.toString());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Deadline not found for fileId: " + fileId);
	        }
	    }
	
	
	
}
