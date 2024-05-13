package com.fpedFIND.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.fpedFIND.Entity.FileFolder;

public interface FileFolderRepository extends JpaRepository<FileFolder, Long> {
	
	 @Query("SELECT ff FROM FileFolder ff WHERE ff.folder.id = :folderId")
	    List<FileFolder> findByFolderId(@Param("folderId") Long folderId);
	 
//	 @Query("SELECT new com.fpedFIND.DTO.FileFolderDTO(f.filefolder_id, f.folder, new com.fpedFIND.DTO.UserDto3(u.user_id, u.firstname, u.lastname, u.username, u.role_name), f.filefolder_name, f.folderpath, f.filefolderDateTime) FROM FileFolder f JOIN f.user u")
//	 List<FileFolderDTO> findAllLimited();
	 
	 @Query("SELECT f FROM FileFolder f JOIN FETCH f.user u JOIN FETCH f.folder folder")
	 List<FileFolder> findAllLimited();
}