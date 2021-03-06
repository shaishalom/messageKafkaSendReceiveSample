package com.example.restservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.example.demo.exception.OutputStatusEnum;

@XmlRootElement
public class MessageDTO extends BaseInputDTO{

	public MessageDTO() {
		this.setStatus(new StatusDTO(OutputStatusEnum.SUCCESS, "", ""));
	}
	
	
	public MessageDTO(String message, String type) {
		super();
		this.message = message;
		this.type = type;
	}
	private String message;
	private String type;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "MessageDTO [message=" + message + ", type=" + type + "]";
	}
	
}
