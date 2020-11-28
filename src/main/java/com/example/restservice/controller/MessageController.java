package com.example.restservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ProjBusinessException;
import com.example.restservice.dto.MessageDTO;
import com.example.restservice.dto.StatusDTO;
import com.example.restservice.service.MessageSendService;
import com.example.restservice.util.StringUtils;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("")
@Api()
public class MessageController {
	@Autowired
	MessageSendService messageSendService;

	@Autowired
	Logger logger;

	
	/**
	 * waiting for message to send to kafka topic
	 * @param messageDTO
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<MessageDTO> message(@RequestBody MessageDTO messageDTO, HttpServletRequest request)
			throws Exception {

		
		String criteriaInputStr = StringUtils.toJson(messageDTO);
		logger.info("listings REQUEST->" + criteriaInputStr);
		
		MessageDTO messageOutputDTO = null;
		try {
			messageOutputDTO = messageSendService.sendMessage(messageDTO);
		} catch (ProjBusinessException e) {
			messageOutputDTO = new MessageDTO("","");
			messageOutputDTO.setStatus(handleBusinessException(e));
			return new ResponseEntity<MessageDTO>(messageOutputDTO, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String output = StringUtils.toJson(messageOutputDTO);
		logger.info("listings RESPONSE->" + output);
		return new ResponseEntity<MessageDTO>(messageOutputDTO, new HttpHeaders(), HttpStatus.OK);
	}

	
	public StatusDTO handleBusinessException(ProjBusinessException projBusinessException) {
		StatusDTO status = projBusinessException.getStatus();
		return status;
	}
	
	


}