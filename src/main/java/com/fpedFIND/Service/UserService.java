package com.fpedFIND.Service;


import com.fpedFIND.Entity.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fpedFIND.DTO.CalendarEventsDTO;
import com.fpedFIND.DTO.CommentDTO;
import com.fpedFIND.DTO.FileDTO;
import com.fpedFIND.DTO.FileFolderDTO;
import com.fpedFIND.DTO.UserDTO2;
import com.fpedFIND.DTO.UserDto;
import com.fpedFIND.DTO.UserDto3;
import com.fpedFIND.Entity.Board;
import com.fpedFIND.Entity.CalendarEvents;
import com.fpedFIND.Entity.CommentTagging;
import com.fpedFIND.Entity.FileCategory;
import com.fpedFIND.Entity.FileFolder;
import com.fpedFIND.Entity.FileSection;
import com.fpedFIND.Entity.Folder;
import com.fpedFIND.Entity.Image;
import com.fpedFIND.Entity.Log;
import com.fpedFIND.Entity.Status;
import com.fpedFIND.Entity.TaggedUser;
import com.fpedFIND.Entity.User;
import com.fpedFIND.Entity.UserProfileUpdateRequest;
import com.fpedFIND.Repository.BoardRepository;
import com.fpedFIND.Repository.CalendarRepository;
import com.fpedFIND.Repository.CommentRepository;
import com.fpedFIND.Repository.FileCategoryRepository;
import com.fpedFIND.Repository.FileFolderRepository;
import com.fpedFIND.Repository.FileRepository;
import com.fpedFIND.Repository.FileSectionRepository;
import com.fpedFIND.Repository.FolderRepository;
import com.fpedFIND.Repository.ImageRepository;
import com.fpedFIND.Repository.LogRepository;
import com.fpedFIND.Repository.StatusRepository;
import com.fpedFIND.Repository.TaggedUserRepository;
import com.fpedFIND.Repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {
	
	  public User getUserById2(Integer userId) {
	        return repository.findById(userId).orElse(null);
	    }
	
BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public boolean checkPassword(String plainPassword, String hashedPassword) {
	    return passwordEncoder.matches(plainPassword, hashedPassword);
	}
	
	public String encodePassword(String plainPassword) {
	    return passwordEncoder.encode(plainPassword);
	}
	
	@Autowired
	private UserRepository repository;
	
	// ALL ARE THE SERVICES TO BE CALLED IN CONTROLLER
	//GETTING THE LIST OF ALL THE USERS
	public List<User> getUser(){
		return repository.findAll();
	}
		 // Method to retrieve all user IDs as a comma-separated string
    public String getAllUserIdsAsString() {
        // Fetch all users from the database
        List<User> allUsers = repository.findAll();
        
        // Create a StringBuilder to build the comma-separated string
        StringBuilder userIdsBuilder = new StringBuilder();
        
        // Append each user's ID to the StringBuilder
        for (User user : allUsers) {
            userIdsBuilder.append(user.getUser_id()).append(",");
        }
        
        // Remove the trailing comma if there are users
        if (!allUsers.isEmpty()) {
            userIdsBuilder.deleteCharAt(userIdsBuilder.length() - 1);
        }
        
        // Return the comma-separated string of user IDs
        return userIdsBuilder.toString();
    }
    
    public List<UserDto> getActiveUsers() {
        List<User> activeUsers = repository.findByStatus("online");
        return activeUsers.stream()
                .map(user -> new UserDto(user.getFirstname(), user.getLastname()))
                .collect(Collectors.toList());
    }
    
	
	// SAVING/ADDING AN ACCOUNT
	public void addUser(User user) {
		repository.save(user);

	}
	
	// FETCH BIRTHDATE
	@Cacheable("Users")
	public List<UserDto> getUsersWithBirthdays() {
	    List<User> users = repository.findAll();
	    return users.stream()
	            .map(user -> new UserDto(
	                    user.getUser_id(),
	                    user.getFirstname(),
	                    user.getLastname(),
	                    user.getBirthday(), // Include birthday here
	                    user.getSectionName(),
	                    user.getHasAccess()
	            		))
	            		
	            .collect(Collectors.toList());
	}
	
	    public List<User> getAllUsers() {
	        return repository.findAll();
	    }
	
	
	
	// ADD NEW USER
	public void saveUserToDB(MultipartFile file, String username, String password, String email,
			String sectionName, String firstname, String lastname, String department, String role_name, LocalDate birthday)
	{
		User u = new User();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if(fileName.contains(".."))
		{
			System.out.println("not a valid file");
		}
		try {
			u.setProfilePicture(Base64.getEncoder().encodeToString(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		u.setUsername(username);
		u.setPassword(passwordEncoder.encode(password));
		u.setEmail(email);
		u.setSectionName(sectionName);
		u.setFirstname(firstname);
		u.setLastname(lastname);
		u.setDepartment(department);
		u.setRole_name(role_name);
		u.setBirthday(birthday);
		repository.save(u);
	}
	
	
	// UPDATING ACCOUNT
	public void updateUser(User user) {
		repository.save(user);
	}
	
	
	
	// DELETING ACCOUNT
	public void deleteUser(Integer id) {
		repository.deleteById(id);
	}
	
	  public User findByUserId(Integer user_id) {
	        Optional<User> userOptional = repository.findById(user_id); // Access through the instance
	        return userOptional.orElse(null);
	    }
	  
	  //BAGONG GAWA
	  public User getUserById1(Integer user_id) {
		    return repository.findById(user_id).orElse(null);
		}
	  
	  
	  
	  
	  // UPDATE USER //
	    public void updateUserProfile(User user, UserProfileUpdateRequest request) {
	        // Update the user with the information from the request
	        // You may want to add null checks to handle fields that are not present in the request

	  

	        if (request.getFirstname() != null) {
	            user.setFirstname(request.getFirstname());
	        }

	        if (request.getLastname() != null) {
	            user.setLastname(request.getLastname());
	        }
	        
	        if (request.getUsername() != null) {
	            user.setUsername(request.getUsername());
	        }
	        
	         
	        if (request.getEmail() != null) {
	            user.setEmail(request.getEmail());
	        }
	        
	        if (request.getSectionName() != null) {
	            user.setSectionName(request.getSectionName());
	        }
	        
	        if (request.getRole_name() != null) {
	            user.setRole_name(request.getRole_name());
	        }
	        
	        if (request.getOldPassword() != null && request.getNewPassword() != null) {
	            // Check if the old password matches the stored password
	            if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
	                // If it does, hash the new password and update the stored password
	                String hashedPassword = passwordEncoder.encode(request.getNewPassword());
	                user.setPassword(hashedPassword);
	            } else {
	                // If it doesn't, throw an exception or handle the error
	                throw new IllegalArgumentException("Old password is incorrect.");
	            }
	        }
	        

	        if (request.getBirthday() != null) {
	            user.setBirthday(request.getBirthday());
	        }

	        // Save the updated user to the repository
	        repository.save(user);
	    }
	    
	    public void updateUserProfilepicture(User user) {
	        repository.save(user);
	    }
	    
	  
	  public String getProfilePictureByFullname(String firstname, String lastname) {
	        Optional<User> userOptional = repository.findByFirstnameAndLastname(firstname, lastname);

	        if (userOptional.isPresent()) {
	            User user = userOptional.get();
	            if (user.getProfilePicture() != null) {
	                return user.getProfilePicture();
	            } else {
	                throw new RuntimeException("Profile picture not found for user with fullname: " + firstname + " " + lastname);
	            }
	        } else {
	            throw new RuntimeException("User with fullname: " + firstname + " " + lastname + " not found");
	        }
	    }
	  
	  public String getProfilePictureByUserId(String userId) {
		    Optional<User> userOptional = repository.findByUserId(userId);

		    if (userOptional.isPresent()) {
		        User user = userOptional.get();
		        if (user.getProfilePicture() != null) {
		            return user.getProfilePicture();
		        } else {
		            throw new RuntimeException("Profile picture not found for user with ID: " + userId);
		        }
		    } else {
		        throw new RuntimeException("User with ID: " + userId + " not found");
		    }
		}
	  
	  
	  
	//HASACCESS
		    public User toggleAccess(Integer userId, Boolean hasAccess) {
		        User user = repository.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found"));
		        user.setHasAccess(hasAccess);
		        repository.save(user);
		        return user;
		    }
	  
	
	//////////// --------FOR FILE---------------- ////////////////////////

	
	
	@Autowired
	private FileRepository repost;

	// FOR FILES //
		// GETTING ALL THE FILES
	 public List<File> getAllFiles() {
	      return repost.findAll();
	    }
	 
	 
	  public Page<File> getAllFiles(Pageable pageable) {
	        return fileRepository.findAll(pageable);
	    }
	 
	// PAYLOAD LESSEN FOR FETCHING FILES
	  public List<FileDTO> getAllFilesWithUserDetails() {
	      List<File> files = fileRepository.findAll();
	      List<FileDTO> fileDTOs = new ArrayList<>();

	      // Map to store latest comment for each file
	      List<CommentDTO> latestCommentsList = new ArrayList<>();

	      // Fetch latest comments for each file
	      for (CommentTagging comment : commentstatus.findLatestCommentsForEachFile()) {
	          CommentDTO commentDTO = new CommentDTO();
	          // Map CommentTagging attributes to CommentDTO attributes
	          commentDTO.setId(comment.getId());
	          commentDTO.setFileId(comment.getFileId());
	          commentDTO.setContent(comment.getContentWithUsername());
	          commentDTO.setAttachmentName(comment.getAttachmentName()); // Set the attachment name
	          latestCommentsList.add(commentDTO);
	      }

	      for (File file : files) {
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

	       // Fetching tagged users and converting IDs to full names along with section names
	          String taggedUsersIds = file.getTaggedUsers();
	          if (taggedUsersIds != null && !taggedUsersIds.isEmpty()) {
	              String[] userIds = taggedUsersIds.split(",");
	              List<String> fullNamesWithSections = new ArrayList<>();
	              for (String userId : userIds) {
	                  User user = repository.findById(Integer.parseInt(userId)).orElse(null);
	                  if (user != null) {
	                      // Fetch the SectionName for the user
	                      String sectionName = user.getSectionName();
	                      String fullNameWithSection = user.getFirstname() + " " + user.getLastname();
	                      if (sectionName != null && !sectionName.isEmpty()) {
	                          fullNameWithSection += " (" + sectionName + ")";
	                      }
	                      fullNamesWithSections.add(fullNameWithSection);
	                  }
	              }
	              fileDTO.setTaggedUsers(String.join(", ", fullNamesWithSections));
	          } else {
	              fileDTO.setTaggedUsers(""); // Set empty if no tagged users
	          }

	          // Fetching uploader details
	          User uploader = file.getUser();
	          if (uploader != null) {
	              UserDto3 uploaderDTO = new UserDto3();
	              uploaderDTO.setUser_id(uploader.getUser_id());
	              uploaderDTO.setFirstname(uploader.getFirstname());
	              uploaderDTO.setLastname(uploader.getLastname());
	              uploaderDTO.setUsername(uploader.getUsername());
	              fileDTO.setUser(uploaderDTO);
	          }

	          // Fetching latest comment for the file
	          CommentDTO latestComment = getLatestCommentForFile(file.getFileId(), latestCommentsList);
	          if (latestComment != null) {
	              String latestCommentContent = latestComment.getContent();
	              String latestAttachment = latestComment.getAttachmentName() != null ? latestComment.getAttachmentName() : "";

	              UserDTO2 latestCommentUser = latestComment.getUser();
	              
	              // Check if the latestCommentUser is not null
	              if (latestCommentUser != null) {
	                  String latestCommentUsername = latestCommentUser.getUsername();
	                  if (latestCommentUsername != null && !latestCommentUsername.isEmpty()) {
	                      fileDTO.setLatestComment(latestCommentUsername + " : " + latestCommentContent + latestAttachment);
	                  }
	                  else if(latestCommentContent == null) {
	                      fileDTO.setLatestComment(latestCommentUsername + " : " + latestAttachment + latestAttachment );
	                  }
	                  else {
	                      fileDTO.setLatestComment(latestCommentContent + latestAttachment);
	                      // Or handle it differently based on your requirements
	                  }
	              } else {
	                  fileDTO.setLatestComment(latestCommentContent + latestAttachment);
	                  // Or handle it differently based on your requirements
	              }
	          } else {
	              fileDTO.setLatestComment(""); // Set empty if no comment found
	          }

	          fileDTOs.add(fileDTO);
	      }

	      return fileDTOs;
	  }

	// Method to get the latest comment for a specific file ID
	  public CommentDTO getLatestCommentForFile(Integer fileId, List<CommentDTO> latestCommentsList) {
		    CommentDTO latestComment = null;
		    LocalDateTime latestTimestamp = null;

		    // Iterate through the latestCommentsList
		    for (CommentDTO comment : latestCommentsList) {
		        // Check if the comment is associated with the specified fileId
		        if (comment.getFileId().equals(fileId)) {
		            LocalDateTime commentTimestamp = comment.getTimestamp();
		            // Check if the current comment is the latest one
		            if (latestTimestamp == null || commentTimestamp.isAfter(latestTimestamp)) {
		                latestTimestamp = commentTimestamp;
		                latestComment = comment; // Update the latestComment variable
		            }
		        }
		    }

		    return latestComment;
		}
	  
	  public List<FileDTO> findByTaggedUsers(String userId) {
	        List<File> files = fileRepository.findByTaggedUsersContaining(userId);
	        List<FileDTO> fileDTOs = new ArrayList<>();
	        for (File file : files) {
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
	            fileDTO.setTaggedUsers(file.getTaggedUsers());

	            // Fetching uploader details
	            User uploader = file.getUser();
	            if (uploader != null) {
	                UserDto3 uploaderDTO = new UserDto3();
	                uploaderDTO.setUser_id(uploader.getUser_id());
	                uploaderDTO.setFirstname(uploader.getFirstname());
	                uploaderDTO.setLastname(uploader.getLastname());
	                uploaderDTO.setUsername(uploader.getUsername());
	                fileDTO.setUser(uploaderDTO);
	            }
	  	      	List<CommentDTO> latestCommentsList = new ArrayList<>();
	  	      	
	  	      // Fetch latest comments for each file
	  	      for (CommentTagging comment : commentstatus.findLatestCommentsForEachFile()) {
	  	          CommentDTO commentDTO = new CommentDTO();
	  	          // Map CommentTagging attributes to CommentDTO attributes
	  	          commentDTO.setId(comment.getId());
	  	          commentDTO.setFileId(comment.getFileId());
	  	          commentDTO.setContent(comment.getContentWithUsername());
	  	          commentDTO.setAttachmentName(comment.getAttachmentName()); // Set the attachment name
	  	          latestCommentsList.add(commentDTO);
	  	      }
	            // Fetching latest comment for the file
		          CommentDTO latestComment = getLatestCommentForFile(file.getFileId(), latestCommentsList);
		          if (latestComment != null) {
		              String latestCommentContent = latestComment.getContent();
		              String latestAttachment = latestComment.getAttachmentName() != null ? latestComment.getAttachmentName() : "";

		              UserDTO2 latestCommentUser = latestComment.getUser();
		              
		              // Check if the latestCommentUser is not null
		              if (latestCommentUser != null) {
		                  String latestCommentUsername = latestCommentUser.getUsername();
		                  if (latestCommentUsername != null && !latestCommentUsername.isEmpty()) {
		                      fileDTO.setLatestComment(latestCommentUsername + " : " + latestCommentContent + latestAttachment);
		                  }
		                  else if(latestCommentContent == null) {
		                      fileDTO.setLatestComment(latestCommentUsername + " : " + latestAttachment + latestAttachment );
		                  }
		                  else {
		                      fileDTO.setLatestComment(latestCommentContent + latestAttachment);
		                      // Or handle it differently based on your requirements
		                  }
		              } else {
		                  fileDTO.setLatestComment(latestCommentContent + latestAttachment);
		                  // Or handle it differently based on your requirements
		              }
		          } else {
		              fileDTO.setLatestComment(""); // Set empty if no comment found
		          }

		          fileDTOs.add(fileDTO);
		      }

		      return fileDTOs;
		  }
	  
	  
	  
	 ///
	   public List<File> getAllFilesSortedByDate() {
	        // Sort files by uploadDateTime in descending order (latest first)
	        return repost.findAll(Sort.by(Sort.Order.desc("uploadDateTime")));
	    }
	   
	   // Method to find new files uploaded before a given LocalDateTime
	    public List<File> findNewFilesUploadedBefore(LocalDateTime dateTime) {
	        return repost.findByUploadDateTimeBeforeAndStatusName(dateTime, "New");
	    }
	 
	 
	
	// SAVING FILE //
	    public File addFile(File file) {
	        return repost.save(file);
	    }
			 
	 // UPDATING FILE //
	public void updateFile(File file) {
	    try {
	        // Assuming repost is an instance of your FileRepository interface
	        // Ensure your File entity is properly annotated and mapped to the database table
	        String taggedUsers = file.getTaggedUsers();
	        if (taggedUsers == null || taggedUsers.isEmpty()) {
	            taggedUsers = ""; // Ensuring consistency
	        }
	        repost.save(file); // Save the file with the corrected taggedUsers
	        // Logging statements
	       // System.out.println("File updated successfully with tagged users: " + file.getTaggedUsers());
	    } catch (Exception e) {
	        // Handle any exceptions
	        System.out.println("Failed to update file: " + e.getMessage());
	    }
	}
	
	
	 // UPDATING FILE //
	 public void updateFileStatusName(Integer id, String statusName) {
	        File file = fileRepository.findById(id).orElse(null);
	        if (file != null) {
	            file.setStatusName(statusName);
	            repost.save(file);
	        } else {
	            // Handle file not found error
	        }
	    }

	
	
	 
	 // DELETING FILE //
	public void deleteFile(Integer fileId) {
		repost.deleteById(fileId);
	}
	
	public Optional<File> getFilebyId(Integer fileId){
		return repost.findById(fileId);
	}
	
	public File findFileByFileName(String fileName) {
	    return repost.findByFileName(fileName);
	}
	
	
    public File updateDeadline(Integer fileId, LocalDate newDeadline) {
        File file = repost.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found"));

        file.setDeadline(newDeadline);
        return repost.save(file);
    }
	
	
	 // GETTING THE STATUS COUNT  
	   
	   public long getCountByStatusName(String statusName) {
	        return repost.countByStatusName(statusName);
	    }
	   
	   public Integer findFileById(Integer fileId) {
		    return fileRepository.findFileIdById(fileId);
		}
	   
	   
	 // TAGGING FOR FILE //
	   
	// Tag users to a file
	    public void tagUsersToFile(Integer fileId, List<String> userIds) {
	        Optional<File> optionalFile = repost.findById(fileId);

	        if (optionalFile.isPresent()) {
	            File file = optionalFile.get();
	            List<String> currentTaggedUsers = Arrays.asList(file.getTaggedUsers().split(","));

	            for (String userId : userIds) {
	                if (!currentTaggedUsers.contains(userId)) {
	                    if (file.getTaggedUsers() == null || file.getTaggedUsers().isEmpty()) {
	                        file.setTaggedUsers(userId);
	                    } else {
	                        file.setTaggedUsers(file.getTaggedUsers() + "," + userId);
	                    }
	                }
	            }
	            repost.save(file);
	        } else {
	            // Handle the case where the file with the specified ID is not found
	            // You might want to throw an exception or handle it based on your application's logic
	        }
	    }

	    // Untag users from a file
	    public void untagUsersFromFile(Integer fileId, List<String> userIds) {
	        Optional<File> optionalFile = repost.findById(fileId);

	        if (optionalFile.isPresent()) {
	            File file = optionalFile.get();
	            List<String> currentTaggedUsers = Arrays.asList(file.getTaggedUsers().split(","));

	            currentTaggedUsers.removeAll(userIds); // Remove the specified user IDs

	            file.setTaggedUsers(String.join(",", currentTaggedUsers));
	            repost.save(file);
	        } else {
	            // Handle the case where the file with the specified ID is not found
	            // You might want to throw an exception or handle it based on your application's logic
	        }
	    }
	    
	    public File getFileById(Integer fileId) {
	        return repost.findById(fileId).orElse(null);
	    }
	    
	    
	    public File getFileByFileName(String fileName) {
	        return repost.findByFileName(fileName);
	    }
	
	   
	  
	    
	    public List<String> getTaggedUsersForFile(Integer fileId) {
	        File file = repost.findByFileId(fileId);
	        if (file != null) {
	            String taggedUsers = file.getTaggedUsers();
	            if (taggedUsers != null && !taggedUsers.isEmpty()) {
	                return Arrays.asList(taggedUsers.split(","));
	            }
	        }
	        return Collections.emptyList();
	    }
	    
	    
	    public LocalDate getDeadlineForFile(Integer fileId) {
	        Optional<File> fileOptional = fileRepository.findById(fileId);
	        if (fileOptional.isPresent()) {
	            return fileOptional.get().getDeadline();
	        } else {
	            throw new EntityNotFoundException("File not found with id: " + fileId);
	        }
	    }
	    
	
		//////////// --------FOR FILE CATEGORY---------------- ////////////////////////
	    @Autowired
		private FileCategoryRepository reposit;
	  
		// GETTING ALL THE CATEGORY
		public List<FileCategory> getAllCategory(){
			return reposit.findAll();
		}
		
		 public void addCategory(FileCategory category) {
			 reposit.save(category);
		 }
		 
		 // UPDATING FILE //
		 public void updateCategory(FileCategory category) {
			 reposit.save(category);
		 }
		 
		 // DELETING FILE //
		 public void deleteCategory(Integer id) {
			    FileCategory categoryToDelete = reposit.findById(id)
			            .orElseThrow(() -> new NoSuchElementException("Category not found"));

			    List<File> filesInCategory = repost.findByCategoryName(categoryToDelete.getCategoryName());

			    if (filesInCategory.isEmpty()) {
			        // No files associated with the category, check for sub-categories
			        List<FileCategory> subCategories = categoryToDelete.getSubCategories();

			        if (subCategories.isEmpty()) {
			            // No sub-categories, safe to delete the category
			            reposit.deleteById(id);
			        } else {
			            // Check if sub-categories have associated files
			            for (FileCategory subCategory : subCategories) {
			                List<File> filesInSubCategory = repost.findByCategoryName(subCategory.getCategoryName());
			                if (!filesInSubCategory.isEmpty()) {
			                    // Files found in a sub-category, handle accordingly (e.g., do not delete, delete files as well, etc.)
			                    // You can customize this part based on your business logic
			                    throw new IllegalStateException("Cannot delete category as it contains sub-categories with associated files.");
			                }
			            }

			            // No associated files in sub-categories, safe to delete the category
			            reposit.deleteById(id);
			        }
			    } else {
			        // Files found in the category, handle accordingly (e.g., do not delete, delete files as well, etc.)
			        // You can customize this part based on your business logic
			        throw new IllegalStateException("Cannot delete category as it contains associated files.");
			    }
			}


		
		// GETTING THE ID 
		
		   public FileCategory getCategoryById(Integer id) {
		        return reposit.findById(id).orElse(null);
		    }
		   
		   
		    // ADDING SUBCATEGORY TO A PARENT CATEGORY
		    public void addSubcategoryToCategory(Integer parentId, FileCategory subcategory) {
		        FileCategory parentCategory = getCategoryById(parentId);
		        if (parentCategory != null) {
		            List<FileCategory> subCategories = parentCategory.getSubCategories();
		            subCategories.add(subcategory);
		            subcategory.setParentCategory(parentCategory);
		            reposit.save(parentCategory);
		        }
		    }

		    // GETTING ALL SUBCATEGORIES OF A PARENT CATEGORY
		    public List<FileCategory> getAllSubcategoriesOfCategory(Integer parentId) {
		        FileCategory parentCategory = getCategoryById(parentId);
		        if (parentCategory != null) {
		            return parentCategory.getSubCategories();
		        }
		        return new ArrayList<>(); // Return empty list if the parent category doesn't exist
		    }
		    
		    // GETTING MAIN CATEGORIES ONLY
		    public List<FileCategory> getMainCategories() {
		        List<FileCategory> allCategories = reposit.findAll();
		        
		        List<FileCategory> mainCategories = new ArrayList<>();
		        for (FileCategory category : allCategories) {
		            if (category.getParentCategory() == null) {
		                mainCategories.add(category);
		            }
		        }
		        return mainCategories;
		    }
		    
		    

		    // Update a specific sub-category of a specific main category
		    public void updateSubCategoryOfMainCategory(Integer parentId, Integer subId, FileCategory subcategory) {
		        FileCategory parentCategory = getCategoryById(parentId);
		        if (parentCategory != null) {
		            List<FileCategory> subCategories = parentCategory.getSubCategories();
		            for (FileCategory sub : subCategories) {
		                if (sub.getCategoryId().equals(subId)) {
		                    // Update subcategory details
		                    sub.setCategoryName(subcategory.getCategoryName());
		                    sub.setSectionName(subcategory.getSectionName());
		                    reposit.save(parentCategory);
		                    return; // Exit the loop once the subcategory is updated
		                }
		            }
		        }
		        throw new NoSuchElementException("Subcategory not found");
		    }

		    // Delete a specific sub-category of a specific main category
		    public void deleteSubCategoryOfMainCategory(Integer parentId, Integer subId) {
		        FileCategory parentCategory = reposit.findById(parentId)
		                .orElseThrow(() -> new NoSuchElementException("Parent category not found"));

		        List<FileCategory> subCategories = parentCategory.getSubCategories();
		        Optional<FileCategory> subCategoryToDelete = subCategories.stream()
		                .filter(sub -> sub.getCategoryId().equals(subId))
		                .findFirst();

		        if (subCategoryToDelete.isPresent()) {
		            FileCategory subCategory = subCategoryToDelete.get();

		            List<File> filesInSubCategory = repost.findByCategoryName(subCategory.getCategoryName());

		            if (filesInSubCategory.isEmpty()) {
		                // No files associated with the sub-category, safe to delete
		                subCategories.remove(subCategory);
		                reposit.save(parentCategory);
		                reposit.delete(subCategory); // Delete the subcategory from the database
		            } else {
		                // Files found in the sub-category, handle accordingly (e.g., do not delete, delete files as well, etc.)
		                // You can customize this part based on your business logic
		                throw new IllegalStateException("Cannot delete sub-category as it contains associated files.");
		            }
		        } else {
		            throw new NoSuchElementException("Subcategory not found");
		        }
		    }
		    
		 // RENAME SUBCATEGORY
		    public void renameSubCategory(Integer parentId, Integer subId, String newName) {
		        FileCategory parentCategory = getCategoryById(parentId);
		        if (parentCategory != null) {
		            List<FileCategory> subCategories = parentCategory.getSubCategories();
		            for (FileCategory sub : subCategories) {
		                if (sub.getCategoryId().equals(subId)) {
		                    // Rename subcategory
		                    sub.setCategoryName(newName);
		                    reposit.save(parentCategory);
		                    return; // Exit the loop once the subcategory is renamed
		                }
		            }
		        }
		        throw new NoSuchElementException("Subcategory not found");
		    }
		    
		    
		    //FILE CATEGORY PER SECTION
		    public List<FileCategory> getCategoriesBySection(String section) {
		        List<FileCategory> allCategories = reposit.findAll();
		        List<FileCategory> categoriesBySection = new ArrayList<>();
		        for (FileCategory category : allCategories) {
		            if (section.equals(category.getSectionName())) {
		                categoriesBySection.add(category);
		            }
		        }
		        return categoriesBySection;
		    }


		    
		    


	//////////// --------FOR SECTION---------------- ////////////////////////
	@Autowired
	private FileSectionRepository reposection;
	
	// GETTING ALL THE SECTION
	public List<FileSection> getAllSection(){
		return reposection.findAll();
	}
	
	// ADDING NEW SECTION
	public void addSection(FileSection section) {
		reposection.save(section);
	}
	
	// UPDATING SECTION
	public void updateSection(FileSection section) {
		reposection.save(section);
	}
	
	// DELETING SECTION
	public void deleteSection(Integer id) {
		reposection.deleteById(id);
	}
	
	// GETTING THE ID
	   public FileSection getSectionById(Integer id) {
	        return reposection.findById(id).orElse(null);
	    }
	
	
////////////--------FOR THE CALENDAR EVENTS---------------- ////////////////////////
		
@Autowired
private CalendarRepository repocalendar;

// GETTING ALL THE EVENTS


//Updated service method
public List<CalendarEventsDTO> getEventsByUserId(Integer userId) {
 return repocalendar.findAllByUserId(userId).stream()
         .map(this::mapToDTO)
         .collect(Collectors.toList());
}

public List<CalendarEventsDTO> getEventsByChiefId(Integer chiefId) {
	 return repocalendar.findAllByChiefId(chiefId).stream()
	         .map(this::mapToDTO)
	         .collect(Collectors.toList());
	 }


public List<CalendarEventsDTO> getAllCalendarEvents() {
    return repocalendar.findAll().stream()
            .filter(event -> event.getChief_id() == null)
            .map(this::mapToDTO)
            .collect(Collectors.toList());
}

private CalendarEventsDTO mapToDTO(CalendarEvents calendarEvent) {
    CalendarEventsDTO dto = new CalendarEventsDTO();
    dto.setId(calendarEvent.getId());
    dto.setGroupId(calendarEvent.getGroupId());
    dto.setTitle(calendarEvent.getTitle());
    dto.setStart(calendarEvent.getStart());
    dto.setEnd(calendarEvent.getEnd());
    dto.setAllDay(calendarEvent.getAllDay());
    dto.setUrl(calendarEvent.getUrl());
    dto.setBgColor(calendarEvent.getBgColor());
    dto.setChief_id(calendarEvent.getChief_id());
    // Assuming you have access to uploader details in calendarEvent
    User uploader = calendarEvent.getUser();
    
    // Convert uploader details to UserDto3 if uploader is not null
    if (uploader != null) {
        UserDto3 uploaderDTO = new UserDto3();
        uploaderDTO.setUser_id(uploader.getUser_id());
        uploaderDTO.setFirstname(uploader.getFirstname());
        uploaderDTO.setLastname(uploader.getLastname());
        uploaderDTO.setUsername(uploader.getUsername());
        uploaderDTO.setRole_name(uploader.getRole_name());
        // Assuming fileDTO is the correct object to set user details to in your context
        dto.setUser(uploaderDTO);
    }
    
    return dto;
}



// ADDING NEW EVENT
public void addEvent(CalendarEvents events) {
	// Convert start and end times to GMT+8 before saving
  events.setStart(events.getStart().atZoneSameInstant(ZoneOffset.ofHours(8)).toOffsetDateTime());
  events.setEnd(events.getEnd().atZoneSameInstant(ZoneOffset.ofHours(8)).toOffsetDateTime());
  
	repocalendar.save(events);
}

// ADD EVENT ON CHIEF
public void addEventChief(CalendarEvents events) {
	// Convert start and end times to GMT+8 before saving
  events.setStart(events.getStart().atZoneSameInstant(ZoneOffset.ofHours(8)).toOffsetDateTime());
  events.setEnd(events.getEnd().atZoneSameInstant(ZoneOffset.ofHours(8)).toOffsetDateTime());
  events.setChief_id(events.getChief_id());
  
	repocalendar.save(events);
}

// UPDATE NEW EVENT
public void updateEventFields(Long id, Map<String, Object> fields) {
	CalendarEvents event = repocalendar.findById(id).orElseThrow(() -> new NoSuchElementException("Event not found"));

  if (fields.containsKey("title")) {
      event.setTitle((String) fields.get("title"));
  }
  if (fields.containsKey("groupId")) {
      event.setGroupId((String) fields.get("groupId"));
  }
  if (fields.containsKey("url")) {
      event.setUrl((String) fields.get("url"));
  }
  if (fields.containsKey("bgColor")) {
      event.setBgColor((String) fields.get("bgColor"));
  }
  if (fields.containsKey("start")) {
      // Convert the start date to OffsetDateTime before setting it
      OffsetDateTime startDate = OffsetDateTime.parse((String) fields.get("start"));
      event.setStart(startDate);
  }
  if (fields.containsKey("end")) {
      // Convert the end date to OffsetDateTime before setting it
      OffsetDateTime endDate = OffsetDateTime.parse((String) fields.get("end"));
      event.setEnd(endDate);
  }
  if (fields.containsKey("allDay")) {
      event.setAllDay((Boolean) fields.get("allDay"));
  }
  // Add similar blocks for other fields if needed
  repocalendar.save(event);
}
	
	// DELETE NEW EVENT
	  public void deleteEvent(Long id) {
		 // System.out.println("Deleting event with ID: " + id);
		  repocalendar.deleteById(id);
	    }
	  
	  
	  
	  public List<CalendarEventsDTO> getAllEvents() {
		    LocalDate today = LocalDate.now();
		    LocalDate tomorrow = today.plusDays(1);

		    // Fetch all events from the repository
		    List<CalendarEvents> allEvents = repocalendar.findAll();
		    
		    // Convert CalendarEvents to CalendarEventsDTO, including chief_id
		    List<CalendarEventsDTO> allEventsDTO = allEvents.stream()
		            .map(this::mapToDTO)
		            .collect(Collectors.toList());
		    
		    // Filter out events associated with chiefs before sending notifications
		    List<CalendarEventsDTO> filteredEvents = allEventsDTO.stream()
		            .filter(event -> event.getChief_id() == null) // Exclude events associated with a chief
		            .filter(event -> (event.getStart().toLocalDate().isEqual(today) || event.getStart().toLocalDate().isEqual(tomorrow)))
		            .collect(Collectors.toList());
		    
		    // Send notifications for filtered events
		    filteredEvents.forEach(event -> {
		        sendEventNotification("Event '" + event.getTitle() + "' is starting " + (event.getStart().toLocalDate().isEqual(today) ? "today" : "tomorrow"));
		    });

		    return filteredEvents;
		}

	    @Autowired
	    private SimpMessagingTemplate messagingTemplate;
	    
		public void sendEventNotification(String message) {
		    messagingTemplate.convertAndSend("/events", message);
		}

		
	
	//////////// --------FOR THE STATUS---------------- ////////////////////////
		
		@Autowired
		private StatusRepository repostatus;
		
		//GETTING ALL THE STATUS
		public List<Status> getAllStatus(){
			return repostatus.findAll();
		}
		
		// ADDING NEW STATUS
		public void addStatus(Status status) {
			repostatus.save(status);
		}
	
		// UPDATE  STATUS
		public void updateStatus(Status status) {
			repostatus.save(status);
		}
		
		// DELETE STATUS
		public void deleteStatus(Integer id) {
			repostatus.deleteById(id);
		}

		public User getUserById(Integer user_id) {
			// TODO Auto-generated method stub
			return null;
		}
		
		
////////////--------FOR THE COMMENTS---------------- ////////////////////////
		
	@Autowired
	private CommentRepository commentstatus;
	
	 // Get all comments
   public List<CommentTagging> getAllComments() {
       return commentstatus.findAll();
   }
   // get specific comment
   public List<CommentTagging> getCommentsForFile(Integer fileId) {
       return commentstatus.findByFileId(fileId);
   }
   
   // Create a new comment
   public CommentTagging createComment(CommentTagging comment) {

       // Set the desired time zone, for example, the Philippines
       TimeZone.setDefault(TimeZone.getTimeZone("Asia/Manila"));
       LocalDateTime adjustedTimestamp = LocalDateTime.now();
       comment.setTimestamp(adjustedTimestamp);
       return commentstatus.save(comment);
   }
   
   
// SAVING A COMMENT //
	 
	public void addComment(CommentTagging comment) {
		commentstatus.save(comment);
		 }
   
	
   // Get a comment by its ID
   public Optional<CommentTagging> getCommentById(Long id) {
       return commentstatus.findById(id);
   }
   
   // Update a comment
   public CommentTagging updateComment(Long id, CommentTagging updatedComment) {
       if (commentstatus.existsById(id)) {
           updatedComment.setId(id); // Ensure the ID matches for updating
           return commentstatus.save(updatedComment);
       }
       return null; // Return null or handle differently if comment with given ID doesn't exist
   }

   // Delete a comment by its ID
   public void deleteComment(Long id) {
   	commentstatus.deleteById(id);
   }
		
	
	// RELATIONAL DATABASE FOR FILE UPLOAD// GETTING THE FILE BASED ON USER_ID //
	    public List<File> getFilesByUserIdSortedByDate(Integer userId) {
	        return repost.findAllByUserIdOrderByUploadDateTimeDesc(userId);
	    }
	    
	// FOR API SAVING ATTACHMENT
	    
	    public void saveFile(MultipartFile file, String filePath) {
	        try {
	            // Convert the MultipartFile to a Path
	            Path path = Path.of(filePath);

	            // Save the file to the specified path
	            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        } catch (IOException e) {
	            // Handle the exception appropriately (e.g., log it or throw a custom exception)
	            e.printStackTrace();
	        }
	    }
	    


		  		  
		  
////////////--------FOR THE BOARD---------------- ////////////////////////
			
		@Autowired
		private BoardRepository boardrepo;
		
		//GETTING ALL THE STATUS
		public List<Board> getAllBoard(){
			return boardrepo.findAll();
		}
		
		// ADDING NEW STATUS
		public void addBoard(Board board) {
			boardrepo.save(board);
		}
	
		// UPDATE  STATUS
		public void updateBoard(Board board) {
			boardrepo.save(board);
		}
		
		// DELETE STATUS
		public void deleteBoard(Long id) {
			boardrepo.deleteById(id);
		}
		
		
////////////--------FOR THE TAGGING---------------- ////////////////////////

	   @Autowired
	    private TaggedUserRepository taggedUserRepository;
	
	
	   public void addTag(Integer fileId, Integer userId) {
		    File file = repost.findById(fileId).orElse(null);
		    User user = repository.findById(userId).orElse(null);

		    if (file != null && user != null) {
		        TaggedUser taggedUser = new TaggedUser();
		        taggedUser.setFile(file);
		        taggedUser.setUser(user);
		        taggedUserRepository.save(taggedUser);
		    }
	   }
	   
	   

	    public void removeTag(Long tagId) {
	        // Logic to remove a TaggedUser entry by tagId
	        taggedUserRepository.deleteById(tagId);
	    }
	    

////////////--------FOR THE CAROUSEL---------------- ////////////////////////

    @Autowired
    private ImageRepository imageRepository;

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }
    
    public Image findByName(String name) {
        return imageRepository.findByName(name);
    }
    
    public List<Image> getImagesByName(String name) {
        return imageRepository.findByNameContaining(name);
    }
    
    public Image findById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public List<Image> getLatestImages() {
        PageRequest pageable = PageRequest.of(0, 5, Sort.by("imageDateTime").descending());
        Page<Image> page = imageRepository.findAllByOrderByImageDateTimeDesc(pageable);
        return page.getContent();
    }
    
    
    

////////////--------FOR THE FOLDER---------------- ////////////////////////

@Autowired
private FolderRepository folderRepository;

//@Cacheable(cacheNames = "Folders")
public List<Folder> getAllFolders() {
return folderRepository.findAll();
}

//FOR NULL
public List<Folder> getRootFolders() {
return folderRepository.findByParentFolderIsNull();
}

//@Cacheable(cacheNames = "Folder", key = "#id")
public Folder getFolderById(Long id) {
return folderRepository.findById(id).orElse(null);
}

//@CacheEvict(cacheNames = "Folders", allEntries = true)
public Folder createRootFolder(Folder folder) {
return folderRepository.save(folder);
}

//@CacheEvict(cacheNames = {"Folders", "Folder"}, allEntries = true)
public Folder createFolder(Folder folder, Long parentFolderId) {
if (parentFolderId != null) {
Folder parentFolder = folderRepository.findById(parentFolderId).orElse(null);
if (parentFolder != null) {
folder.setParentFolder(parentFolder);
} else {
throw new RuntimeException("Parent folder not found");
}
}
return folderRepository.save(folder);
}

public Folder updateFolder(Folder folder) {
return folderRepository.save(folder);
}

//@CacheEvict(cacheNames = {"Folders", "Folder", "FolderFiles"}, allEntries = true)
public void deleteFolder(Long id) {
folderRepository.deleteById(id);
}

public List<Folder> getFoldersByName(String foldername) {
return folderRepository.findByFoldername(foldername);
}

//@CachePut(cacheNames = "Folder", key = "#folder.id")
public Folder updateFolderName(Long id, String newFolderName) {
Folder folder = folderRepository.findById(id).orElse(null);
if (folder != null) {
folder.setFoldername(newFolderName);
folderRepository.save(folder);
}
return folder;
}

public List<Folder> getSubFolders(Folder parentFolder) {
return folderRepository.findByParentFolder(parentFolder);
}

///SEARCH
public List<Folder> searchFolders(String searchTerm) {
String searchTermLower = searchTerm.toLowerCase().trim();
List<Folder> allFolders = folderRepository.findAll();
return allFolders.stream()
.filter(folder -> folder.getFoldername().toLowerCase().contains(searchTermLower))
.collect(Collectors.toList());
}


////////////--------FOR THE FILE FOLDER---------------- ////////////////////////

@Autowired
private FileFolderRepository fileFolderRepository;


//public List<FileFolder> getAllFileFolders() {
//return fileFolderRepository.findAll();
//}


public List<FileFolderDTO> findAllLimited() {
List<FileFolder> fileFolders = fileFolderRepository.findAllLimited();
return fileFolders.stream()
.map(fileFolder -> new FileFolderDTO(
    fileFolder.getFilefolder_id(),
    fileFolder.getFolder(),
    new UserDto3(
        fileFolder.getUser().getUser_id(),
        fileFolder.getUser().getFirstname(),
        fileFolder.getUser().getLastname(),
        fileFolder.getUser().getUsername(),
        fileFolder.getUser().getRole_name()
    ),
    fileFolder.getFilefolder_name(),
    fileFolder.getFolderpath(),
    fileFolder.getFilefolderDateTime(),
    fileFolder.getCategory_name(),
    fileFolder.getDetails()
))
.collect(Collectors.toList());
}

public List<FileFolder> getFilesByFolderId(Long folderId) {
return fileFolderRepository.findByFolderId(folderId);
}

public FileFolder createFileFolder(FileFolder fileFolder) {
return fileFolderRepository.save(fileFolder);
}

//@Cacheable(cacheNames = "FileFolder", key = "#id")
public FileFolder getFileFolder(Long id) {
return fileFolderRepository.findById(id).orElse(null);
}

//@CachePut(cacheNames = "FileFolder", key = "#fileFolder.filefolder_id")
public FileFolder updateFileFolder(FileFolder fileFolder) {
return fileFolderRepository.save(fileFolder);
}

//@CacheEvict(cacheNames = {"FileFolders", "FileFolder", "FolderFiles"}, allEntries = true)
public void deleteFileFolder(Long id) {
fileFolderRepository.deleteById(id);
}

//SEARCH
public List<FileFolder> searchFileFolders(String searchTerm) {
String searchTermLower = searchTerm.toLowerCase().trim();
List<FileFolder> allFileFolders = fileFolderRepository.findAll();
return allFileFolders.stream()
.filter(fileFolder -> fileFolder.getFilefolder_name().toLowerCase().contains(searchTermLower))
.collect(Collectors.toList());
}


//===================FOR THE DEADLINE NOTIFICATION ====================//
//ON GOING STILL HAVE BUGS
//@Autowired
//private SimpMessagingTemplate messagingTemplate;
//
@Autowired
private FileRepository fileRepository;
//
//@Autowired
//private NotificationRepository notificationRepository;
//
//@Scheduled(fixedRate = 60000) // Check every minute, adjust as needed
//public void checkDeadlines() {
//LocalDate today = LocalDate.now();
//List<File> approachingDeadlines = fileRepository.findByDeadlineBetweenAndStatusName(
//    today.plusDays(1),
//    today.plusDays(2),
//    "Pending"
//);
//
//List<File> metDeadlines = fileRepository.findByDeadlineBeforeAndStatusName(today, "Pending");
//
//sendNotifications(approachingDeadlines, "The deadline for file '%s' is approaching in 2 days!");
//sendNotifications(metDeadlines, "The deadline for file '%s' has been met!");
//
//// You can also add additional logic here, e.g., updating the status or sending emails.
//}
//
//private void sendNotifications(List<File> files, String messageTemplate) {
//for (File file : files) {
//// Notify the uploader
//String userDestination = "/user/" + file.getUser().getUsername() + "/queue/deadline-notification";
//String message = String.format(messageTemplate, file.getFileName());
//
//// Save the notification to the database
//Notification notification = new Notification();
//notification.setTagged(file.getUser().getUsername());
//notification.setMessage(message);
//notification.setSender("System"); // You can set the sender to a system identifier
//notification.setFilename(file.getFileName());
//notification.setFileId(file.getFileId());
//notification.setTimestamp(LocalDateTime.now());
//notification.setSeen(false);
//notificationRepository.save(notification);
//
//// Send real-time notification
//messagingTemplate.convertAndSendToUser(file.getUser().getUsername(), userDestination, message);
//}
//}


//===================LOGS====================

@Autowired
private  LogRepository logRepository;

public void LogService(LogRepository logRepository) {
this.logRepository = logRepository;
}

public Log saveLog(String message) {
Log log = new Log();
log.setMessage(message);
log.setTimestamp(LocalDateTime.now());
return logRepository.save(log);
}

public List<Log> getAllLogs() {
return logRepository.findAll();
}


}





