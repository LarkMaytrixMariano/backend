package com.fpedFIND.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpedFIND.Entity.FileCategory;


@Repository
public interface FileCategoryRepository  extends JpaRepository <FileCategory , Integer>{

	FileCategory findByCategoryName(String categoryName);

}
