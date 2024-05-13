package com.fpedFIND.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpedFIND.Entity.FileSection;

@Repository
public interface FileSectionRepository extends JpaRepository<FileSection, Integer> {
	
	FileSection findBySectionName(String sectionName);
}
