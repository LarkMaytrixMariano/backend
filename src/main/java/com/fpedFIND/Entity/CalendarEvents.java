package com.fpedFIND.Entity;

import java.time.OffsetDateTime;


import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name="Fped_events")
public class CalendarEvents {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupId;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime end;
    private Boolean allDay;
    private String url;
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;
	private Integer chief_id;
	private String bgColor;
	

	public CalendarEvents() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	  public String getGroupId() {
	        return groupId;
	    }

	    public void setGroupId(String groupId) {
	        this.groupId = groupId;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public OffsetDateTime getStart() {
	        return start;
	    }

	    public void setStart(OffsetDateTime start) {
	        this.start = start;
	    }

	    public OffsetDateTime getEnd() {
	        return end;
	    }

	    public void setEnd(OffsetDateTime end) {
	        this.end = end;
	    }

	    public Boolean getAllDay() {
	        return allDay;
	    }

	    public void setAllDay(Boolean allDay) {
	        this.allDay = allDay;
	    }


	    public String getUrl() {
	        return url;
	    }

	    public void setUrl(String url) {
	        this.url = url;
	    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

		public CalendarEvents(Long id, String groupId, String title, OffsetDateTime start, OffsetDateTime end, Boolean allDay,
				String url, User user) {
			super();
			this.id = id;
			this.groupId = groupId;
			this.title = title;
			this.start = start;
			this.end = end;
			this.allDay = allDay;
			this.url = url;
			this.user = user;
		}

		@Override
		public String toString() {
			return "EventApi [id=" + id + ", groupId=" + groupId + ", title=" + title + ", start=" + start + ", end="
					+ end + ", allDay=" + allDay + ", url=" + url + ", user=" + user + "]";
		}

		public Integer getChief_id() {
			return chief_id;
		}

		public void setChief_id(Integer chief_id) {
			this.chief_id = chief_id;
		}

		public String getBgColor() {
			return bgColor;
		}

		public void setBgColor(String bgColor) {
			this.bgColor = bgColor;
		}


	
	

	
	
	


	
	
	
	
	
	
	
	
	

	
	
	
	
	
}