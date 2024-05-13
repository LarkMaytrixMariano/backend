package com.fpedFIND.Entity;

import java.time.LocalDateTime;
import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="Fped_carousel")
public class Image {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String name;
    
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;
    
    public Image() {
    }
    
    private String imagePath;
    private LocalDateTime imageDateTime;
    
    

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public LocalDateTime getImageDateTime() {
        return imageDateTime;
    }

    public void setImageDateTime(LocalDateTime imageDateTime) {
        this.imageDateTime = imageDateTime;
    }

	@Override
	public String toString() {
		return "Image [imageId=" + imageId + ", name=" + name + ", data=" + Arrays.toString(data) + ", imagePath="
				+ imagePath + ", imageDateTime=" + imageDateTime + "]";
	}

	

    
	    
	    

}