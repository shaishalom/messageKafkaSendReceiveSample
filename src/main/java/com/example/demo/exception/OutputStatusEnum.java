package com.example.demo.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum OutputStatusEnum implements OutputStatus
{
	SUCCESS(0, "Success", Level.SUCCESS),
	INVALID_INPUT(1, "Invalid Input",  Level.WARNING ),
	NO_RECORDS_FOUND(2, "NO DATA FOUND",  Level.ERROR),
	QUATE_NAME_ALREADY_EXIST(3, "QUATE_NAME_ALREADY_EXIST", Level.ERROR),
	TEMPORARY_FAIL(4, "Request Failed Temporary", Level.ERROR),
	UNEXPECTED(5, "Request Failed", Level.ERROR );
	
	private Integer code;
	
	@JsonIgnore
	private String description;
	
	@JsonIgnore
	private Level level;
	
	private OutputStatusEnum(Integer code, String description , Level level)
	{
		this.code = code;
		this.description = description;
		this.level = level;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}


}

