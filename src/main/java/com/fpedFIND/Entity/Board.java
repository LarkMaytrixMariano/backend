package com.fpedFIND.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Fped_board")
public class Board {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Date datecreation;
    private Date dateexpiration;
    private String link;
    
    
	public Board() {
	}
	
	
	public Board(Long id, String title, String content, Date datecreation, Date dateexpiration, String link) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.datecreation = datecreation;
		this.dateexpiration = dateexpiration;
		this.link = link;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDatecreation() {
		return datecreation;
	}
	public void setDatecreation(Date datecreation) {
		this.datecreation = datecreation;
	}
	public Date getDateexpiration() {
		return dateexpiration;
	}
	public void setDateexpiration(Date dateexpiration) {
		this.dateexpiration = dateexpiration;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}


	@Override
	public String toString() {
		return "Board [id=" + id + ", title=" + title + ", content=" + content + ", datecreation=" + datecreation
				+ ", dateexpiration=" + dateexpiration + ", link=" + link + "]";
	}
	
}
