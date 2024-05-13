package com.fpedFIND.Entity;

public class AdminAlert {
	
    private String message;
    private String sender; // Include sender information
    private String receiver; // New field for the receiver(s)

    
    public AdminAlert() {
	}


	public AdminAlert(String message, String sender, String receiver) {
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}


	public String getReceiver() {
		return receiver;
	}


	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}


	@Override
	public String toString() {
		return "AdminAlert [message=" + message + ", sender=" + sender + ", receiver=" + receiver + "]";
	}

	

    
    
    
    
}
