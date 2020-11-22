/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.restservice;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.main.MessageApplication;
import com.example.restservice.controller.MessageController;
import com.example.restservice.dto.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessageApplication.class)
@AutoConfigureMockMvc
public class MessageControllerTests {

	@Autowired
	private MockMvc mockMvc;

	
	@Autowired
	MessageController messageController;
	

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Test
	public void runMessage() throws Exception {

		MessageDTO messageDTO= new MessageDTO("shai shalom message", "test");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String messageJson = ow.writeValueAsString(messageDTO);

		ResponseEntity<MessageDTO> messageResponse = null;

//you can run the controller directly		
//		messageResponse = messageController.message(messageDTO, null);
//		MessageDTO messageOutDto = messageResponse.getBody();
//		assertNotNull(messageOutDto);
//		assertTrue(messageOutDto.getMessage().equals("shai shalom message"));

// or run via path:		
		
		ResultActions resultActions= mockMvc.perform(post("http://localhost:8080/message").contentType(APPLICATION_JSON_UTF8)
		        .content(messageJson)).andExpect(status().isOk());
		resultActions.andExpect(jsonPath("$.message").exists()); 
		resultActions.andExpect(jsonPath("$.message").value("shai shalom message"));
		assertTrue(resultActions !=null);
		
		
	}

}
