package com.fpedFIND.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Fped_filesection")
public class FileSection {
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   @Column(name="section_id")
	   private Integer section_id;

	   private String sectionName;

	public FileSection() {

	}

	public FileSection(Integer section_id, String sectionName) {
		this.section_id = section_id;
		this.sectionName = sectionName;
	}

	public Integer getSectionId() {
		return section_id;
	}

	public void setSectionId(Integer section_id) {
		this.section_id = section_id;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	@Override
	public String toString() {
		return "FileSection [section_id=" + section_id + ", sectionName=" + sectionName + "]";
	}
	   
	
	
	   
}
