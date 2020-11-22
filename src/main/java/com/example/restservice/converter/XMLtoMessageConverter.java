package com.example.restservice.converter;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.restservice.dto.MessageDTO;
import com.thoughtworks.xstream.XStream;

@Component("xmlToMessageConverter")
public class XMLtoMessageConverter implements Function<String,MessageDTO> {

	@Autowired
	private XStream	xStream;
	
	@Override
	public MessageDTO apply(String xml) {
				
		MessageDTO messageDto = (MessageDTO) xStream.fromXML(xml);

		return messageDto;
	}



}

