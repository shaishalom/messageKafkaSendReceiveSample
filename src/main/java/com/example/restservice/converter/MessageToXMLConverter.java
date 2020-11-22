package com.example.restservice.converter;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.restservice.dto.MessageDTO;
import com.thoughtworks.xstream.XStream;

@Component("messageToXMLConverter")
public class MessageToXMLConverter implements Function<MessageDTO,String> {

	@Autowired
	private XStream	xStream;
	
	@Override
	public String apply(MessageDTO messageDTO) {
				
		String xml = xStream.toXML(messageDTO);

		return xml;
	}



}

