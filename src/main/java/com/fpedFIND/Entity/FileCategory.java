package com.fpedFIND.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Fped_filecategory")
public class FileCategory {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "category_id")
	    private Integer categoryId;

	    @Column(name = "category_name")
	    private String categoryName;

	    @Column(name = "section_name")
	    private String sectionName;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "parent_id")
	    @JsonIgnore // Add this annotation to break the cycle during serialization
	    private FileCategory parentCategory;

	    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
	    @JsonIgnore // Add this annotation to break the cycle during serialization
	    private List<FileCategory> subCategories = new ArrayList<>();


	public FileCategory() {
	
	}


	public Integer getCategoryId() {
		return categoryId;
	}


	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getSectionName() {
		return sectionName;
	}


	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}


	public FileCategory getParentCategory() {
		return parentCategory;
	}


	public void setParentCategory(FileCategory parentCategory) {
		this.parentCategory = parentCategory;
	}


	public List<FileCategory> getSubCategories() {
		return subCategories;
	}


	public void setSubCategories(List<FileCategory> subCategories) {
		this.subCategories = subCategories;
	}


	public FileCategory(Integer categoryId, String categoryName, String sectionName, FileCategory parentCategory,
			List<FileCategory> subCategories) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.sectionName = sectionName;
		this.parentCategory = parentCategory;
		this.subCategories = subCategories;
	}


	@Override
	public String toString() {
		return "FileCategory [categoryId=" + categoryId + ", categoryName=" + categoryName + ", sectionName="
				+ sectionName + ", parentCategory=" + parentCategory + ", subCategories=" + subCategories + "]";
	}

	
	
	
	
}
