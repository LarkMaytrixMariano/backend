package com.fpedFIND.UserController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fpedFIND.DTO.FileFolderDTO;
import com.fpedFIND.DTO.UserDto3;
import com.fpedFIND.Entity.FileFolder;
import com.fpedFIND.Entity.Folder;
import com.fpedFIND.Entity.User;
import com.fpedFIND.Paths.GlobalPaths;
import com.fpedFIND.Service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/filefolders")
public class FileFolderController {

	 @Autowired
	    private UserService fileFolderService;
	 
	 @Autowired
	    private UserService folderService; 
	 
	 @Autowired
	    private UserService userService;
	 
	 
//	 @GetMapping("/filefolders")
//	    public List<FileFolderDTO> getAllLimited() {
//	        return fileFolderService.getAllLimited();
//	    }
//	 
	    @GetMapping
	    public ResponseEntity<List<FileFolderDTO>> getAllLimitedFileFolders() {
	        List<FileFolderDTO> fileFolders = fileFolderService.findAllLimited();
	        return new ResponseEntity<>(fileFolders, HttpStatus.OK);
	    }
	 
	 // Retrieve files by folder_id
	 // Retrieve files by folder_id
		//@Cacheable(cacheNames = "Folderfiles")
		    @GetMapping("/files/qms/{folderId}")
		    public ResponseEntity<List<FileFolderDTO>> getFilesByFolderId(@PathVariable Long folderId) {
		        List<FileFolder> files = fileFolderService.getFilesByFolderId(folderId);
		        List<FileFolderDTO> fileFolderDtos = new ArrayList<>();
		        for (FileFolder file : files) {
		            // Check if the category name is "QMS"
		            if ("QMS".equals(file.getCategory_name())) {
		                FileFolderDTO dto = new FileFolderDTO();
		                dto.setFolder(file.getFolder());
		                dto.setFilefolder_id(file.getFilefolder_id());
		                dto.setFilefolder_name(file.getFilefolder_name());
		                dto.setFolderpath(file.getFolderpath());
		                dto.setFilefolderDateTime(file.getFilefolderDateTime());
		                dto.setCategory_name(file.getCategory_name());
		                dto.setDetails(file.getDetails());
		                
		                // Set UserDto3
		                User user = file.getUser(); // Assuming you have a user field in your FileFolder entity
		                if (user != null) {
		                    UserDto3 userDto = new UserDto3();
		                    userDto.setUser_id(user.getUser_id());
		                    userDto.setFirstname(user.getFirstname());
		                    userDto.setLastname(user.getLastname());
		                    userDto.setUsername(user.getUsername());
		                    userDto.setRole_name(user.getRole_name());
		                    dto.setUser(userDto);
		                }

		                fileFolderDtos.add(dto);
		            }
		            else if ("other".equals(file.getCategory_name())) {
		            	
		            }
		        }
		        return ResponseEntity.ok(fileFolderDtos);
		    }
		    
		    @GetMapping("/files/other/{folderId}")
		    public ResponseEntity<List<FileFolderDTO>> getFilesByFolderId2(@PathVariable Long folderId) {
		        List<FileFolder> files = fileFolderService.getFilesByFolderId(folderId);
		        List<FileFolderDTO> fileFolderDtos = new ArrayList<>();
		        for (FileFolder file : files) {
		            // Check if the category name is "QMS"
		            if ("other".equals(file.getCategory_name())) {
		                FileFolderDTO dto = new FileFolderDTO();
		                dto.setFolder(file.getFolder());
		                dto.setFilefolder_id(file.getFilefolder_id());
		                dto.setFilefolder_name(file.getFilefolder_name());
		                dto.setFolderpath(file.getFolderpath());
		                dto.setFilefolderDateTime(file.getFilefolderDateTime());
		                dto.setCategory_name(file.getCategory_name());
		                dto.setDetails(file.getDetails());
		                
		                // Set UserDto3
		                User user = file.getUser(); // Assuming you have a user field in your FileFolder entity
		                if (user != null) {
		                    UserDto3 userDto = new UserDto3();
		                    userDto.setUser_id(user.getUser_id());
		                    userDto.setFirstname(user.getFirstname());
		                    userDto.setLastname(user.getLastname());
		                    userDto.setUsername(user.getUsername());
		                    userDto.setRole_name(user.getRole_name());
		                    dto.setUser(userDto);
		                }

		                fileFolderDtos.add(dto);
		            }
		            else if ("other".equals(file.getCategory_name())) {
		            	
		            }
		        }
		        return ResponseEntity.ok(fileFolderDtos);
		    }


		 
		 //FILEFOLDER =FILES
		   @GetMapping("/files/filename/{id}")
		    public FileFolder getFileFolder(@PathVariable Long id) {
		        return fileFolderService.getFileFolder(id);
		    }

		   @PostMapping("/upload")
		   public ResponseEntity<String> uploadFileFolder(
		       @RequestParam("file") MultipartFile[] files,
		       @RequestParam("folderId") Long folder_id,
		       @RequestParam("user_id") Integer user_id,
		       @RequestParam("details") String details,
		       @RequestParam("category_name") String category_name) {

		       User user = userService.getUserById1(user_id);
		       if (user == null) {
		           return ResponseEntity.badRequest().body("User with ID " + user_id + " does not exist.");
		       }

		       Folder folder = folderService.getFolderById(folder_id);
		       if (folder == null) {
		           return ResponseEntity.badRequest().body("Folder with ID " + folder_id + " does not exist.");
		       }

		       for (MultipartFile file : files) {
		           if (file.isEmpty()) {
		               return ResponseEntity.badRequest().body("Please select a file to upload.");
		           }

		           try {
		               FileFolder fileFolder = new FileFolder();
		               fileFolder.setUser(user);
		               fileFolder.setFolder(folder);
		               fileFolder.setFilefolder_name(file.getOriginalFilename());
		               fileFolder.setFilefolderDateTime(LocalDateTime.now());
		               fileFolder.setDetails(details);
		               fileFolder.setCategory_name(category_name);

		               String uploadDir = GlobalPaths.FILEFOLDER_DIR;
		               String userFolder = uploadDir + user.getUsername() + "/";
		               String filePath = userFolder + file.getOriginalFilename();
		               fileFolder.setFolderpath(filePath);

		               fileFolderService.createFileFolder(fileFolder);

		               Path userDirectory = Paths.get(userFolder);
		               if (!Files.exists(userDirectory)) {
		                   Files.createDirectories(userDirectory);
		               }

		               java.io.File newFile = new java.io.File(filePath);
//		               if(newFile.exists()) {
//		                   return ResponseEntity.status(HttpStatus.CONFLICT).body("File already exists.");
//		               }

		               file.transferTo(newFile);
		           } catch (IOException e) {
		               e.printStackTrace();
		               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
		           }
		       }

		       return ResponseEntity.ok("Files uploaded successfully.");
		   }
	 
	 //DOWNLOAD FILE
	 
	 @GetMapping("/download/{fileFolderId}")
	 public ResponseEntity<Resource> downloadFileFolder(@PathVariable Long fileFolderId) {
	     FileFolder fileFolder = fileFolderService.getFileFolder(fileFolderId);
	     if (fileFolder == null) {
	         return ResponseEntity.notFound().build();
	     }

	     String filePath = fileFolder.getFolderpath();

	     try {
	         // Load the file as a resource
	         Path fileFullPath = Paths.get(filePath);
	         Resource resource = new UrlResource(fileFullPath.toUri());

	         if (resource.exists() && resource.isReadable()) {
	             return ResponseEntity.ok()
	                     .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
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


	 //RENAME
	 @PutMapping("/rename/{fileFolderId}")
	 public ResponseEntity<String> renameFileFolder(@PathVariable Long fileFolderId, @RequestParam("newName") String newName) {
	     FileFolder fileFolder = fileFolderService.getFileFolder(fileFolderId);
	     if (fileFolder == null) {
	         return ResponseEntity.notFound().build();
	     }

	     // Update the file folder name
	     fileFolder.setFilefolder_name(newName);
	     fileFolderService.updateFileFolder(fileFolder);

	     return ResponseEntity.ok("File folder renamed successfully.");
	 }
	 
	 //SEARCH
	  @GetMapping("/search/filefolders")
	    public List<FileFolder> searchFileFolders(@RequestParam String term) {
	        return fileFolderService.searchFileFolders(term);
	    }
	//DELETE
		 @DeleteMapping("/delete/{id}")
		    public void deleteFileFolder(@PathVariable Long id) {
			 fileFolderService.deleteFileFolder(id);
		    }
	 
	 

}