package com.fpedFIND.Entity;



public class Message {
	 private String sender;
	 private String content;
	 private MessageType type;
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public Message() {
	}
	public Message(String sender, String content, MessageType type) {
		this.sender = sender;
		this.content = content;
		this.type = type;
	}
	@Override
	public String toString() {
		return "Message [sender=" + sender + ", content=" + content + ", type=" + type + "]";
	} 
	
	  public static class Builder {
	        private MessageType type;
	        private String sender;

	        public Builder type(MessageType type) {
	            this.type = type;
	            return this;
	        }

	        public Builder sender(String sender) {
	            this.sender = sender;
	            return this;
	        }

	        public Message build() {
	            return new Message(this);
	        }
	    }

	    private Message(Builder builder) {
	        this.type = builder.type;
	        this.sender = builder.sender;
	        // ... other fields initialization
	    }

	    public static Builder builder() {
	        return new Builder();
	    }
	
	   	    

}
