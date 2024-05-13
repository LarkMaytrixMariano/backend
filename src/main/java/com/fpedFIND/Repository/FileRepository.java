package com.fpedFIND.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.fpedFIND.Entity.File;
import org.springframework.data.domain.Sort;


@Repository
public interface FileRepository extends JpaRepository <File , Integer> {

	File findByFileName(String fileName);

	
	@Query("SELECT f FROM File f WHERE f.user.user_id = :userId ORDER BY f.uploadDateTime DESC")
	List<File> findAllByUserIdOrderByUploadDateTimeDesc(@Param("userId") Integer userId);
	  
	  
	  long countByStatusName(String statusName);
	  
	    List<File> findAll(Sort sort);

	    
	    //Optional<File> findById(Integer fileId);
	    
	    @Query("SELECT f.fileId FROM File f WHERE f.fileId = :fileId")
	    Integer findFileIdById(Integer fileId);
	    

	    List<File> findByUploadDateTimeBeforeAndStatusName(LocalDateTime dateTime, String statusName);

	    
	    File findByFileId(Integer fileId);
	    
	    // New custom query method to find files by category name
	    List<File> findByCategoryName(String categoryName);

	 

	    List<File> findByDeadlineBetweenAndStatusName(LocalDate start, LocalDate end, String statusName);
	    List<File> findByDeadlineBeforeAndStatusName(LocalDate deadline, String statusName);
	    
	    List<File> findByTaggedUsersContaining(String userId);
	    
	    
	    @Query("SELECT f FROM File f WHERE f.statusChanged < ?1 AND f.statusName = ?2")
	    List<File> findFilesByStatusChangedBeforeAndStatusName(LocalDateTime dateTime, String status);
	    
	    
	    
	    @Query("SELECT f FROM File f WHERE f.uploadDateTime < ?1 AND f.statusName = ?2")
	    List<File> findFilesByUploadDateTimeBeforeAndStatusName(LocalDateTime dateTime, String status);

	    @Query("SELECT f FROM File f WHERE f.statusName = ?1 AND f.deadline < ?2")
	    List<File> findFilesByStatusNameAndDeadline(String statusName, LocalDate deadline);


}
