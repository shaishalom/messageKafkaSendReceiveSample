package com.example.restservice.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.example.demo.exception.Level;
import com.example.demo.exception.OutputStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.restservice.util.StringUtils;

@XmlRootElement
public class StatusDTO {

	public StatusDTO() {
	}
	
	public StatusDTO(OutputStatusEnum outputStatusEnum, String exceptionReason) {
		super();
		this.outputStatusEnum = outputStatusEnum;
		this.exceptionReason = exceptionReason;
		this.level = this.outputStatusEnum.getLevel();
	}

	public StatusDTO(OutputStatusEnum outputStatusEnum, String exceptionReason, String freeText) {
		super();
		this.outputStatusEnum = outputStatusEnum;
		this.exceptionReason = exceptionReason;
		this.freeText = freeText;
	}

	
	private OutputStatusEnum outputStatusEnum = OutputStatusEnum.SUCCESS;
	
	@JsonIgnore
	private String exceptionReason = null;
	
	@JsonIgnore
	private String exceptionUUID = null;
	
	private String freeText;
	
	private Level level;
	
	


	public Boolean isSuccess() {
		if (this.getOutputStatusEnum().getLevel().equals(Level.SUCCESS)
				|| this.getOutputStatusEnum().getLevel().equals(Level.WARNING)) {
			return true;
		}

		return false;
	}

	public String getExceptionUUID() {
		return exceptionUUID;
	}

	public void setExceptionUUID(String exceptionUUID) {
		this.exceptionUUID = exceptionUUID;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public OutputStatusEnum getOutputStatusEnum() {
		return outputStatusEnum;
	}

	public void setOutputStatusEnum(OutputStatusEnum outputStatusEnum) {
		this.outputStatusEnum = outputStatusEnum;
	}

	public String toString() {
		
		String outputAsjson = StringUtils.toJson(this);
		return outputAsjson;
		
	}

}
