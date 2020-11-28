package com.example.restservice;

//import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.main.MessageApplication;
//import com.example.restservice.controller.MessageController;
import com.example.restservice.dto.MessageDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;


//@RunWith(SpringRunner.class)
@SpringBootTest(classes = MessageApplication.class)
//@WebMvcTest(MessageController.class)
@AutoConfigureMockMvc
public class MessageControllerTest {

	@Autowired
	private MockMvc mockMvc;


//	@Autowired
//	MessageController messageController;
	

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	
	@Test
	public void sendMessageToTopic() throws Exception {

		MessageDTO messageDTO= new MessageDTO("shai shalom message", "test");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String messageJson = ow.writeValueAsString(messageDTO);


//you can run the controller directly		
//		ResponseEntity<MessageDTO> messageResponse = null;
//		messageResponse = messageController.message(messageDTO, null);
//		MessageDTO messageOutDto = messageResponse.getBody();
//		assertNotNull(messageOutDto);
//		assertTrue(messageOutDto.getMessage().equals("shai shalom message"));

// or run via path:		
		
		ResultActions resultActions= this.mockMvc.perform(post("http://localhost:8080/message").contentType(APPLICATION_JSON_UTF8)
		        .content(messageJson)).andExpect(status().isOk());
		resultActions.andExpect(jsonPath("$.message").exists()); 
		resultActions.andExpect(jsonPath("$.message").value("shai shalom message"));
		
		
	}

}
