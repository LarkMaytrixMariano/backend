package com.fpedFIND.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.fpedFIND.Entity.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
	List<Folder> findByFoldername(String foldername);
	List<Folder> findByParentFolder(Folder parentFolder);
	 List<Folder> findByParentFolderIsNull();
}