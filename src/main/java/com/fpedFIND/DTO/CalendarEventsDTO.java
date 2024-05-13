package com.fpedFIND.DTO;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CalendarEventsDTO {
	

    private Long id;
    private String groupId;
    private String title;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime end;
    private Boolean allDay;
    private String url;
    
    private UserDto3 user;
    private Integer chief_id;
    private String bgColor;
    
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
	public CalendarEventsDTO(Long id, String groupId, String title, OffsetDateTime start, OffsetDateTime end,
			Boolean allDay, String url) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.title = title;
		this.start = start;
		this.end = end;
		this.allDay = allDay;
		this.url = url;
	}
	public CalendarEventsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserDto3 getUser() {
		return user;
	}
	public void setUser(UserDto3 user) {
		this.user = user;
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
