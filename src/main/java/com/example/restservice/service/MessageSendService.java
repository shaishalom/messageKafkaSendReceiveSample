package com.example.restservice.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import com.example.demo.exception.OutputStatusEnum;
import com.example.demo.exception.ProjBusinessException;
import com.example.restservice.converter.MessageToXMLConverter;
import com.example.restservice.converter.XMLtoMessageConverter;
import com.example.restservice.dto.BaseInputDTO;
import com.example.restservice.dto.MessageDTO;
import com.example.restservice.dto.StatusDTO;

import springfox.documentation.swagger2.mappers.ModelMapper;

@Service
public class MessageSendService {

	public final static String TOPIC_1 = "topic1";
	public final static String TOPIC_2 = "topic2";

	@Autowired
	protected ModelMapper modelMapper;

	@Autowired
	@Qualifier("messageToXMLConverter")
	MessageToXMLConverter messagetoXMLConverter;

	@Autowired
	@Qualifier("xmlToMessageConverter")
	XMLtoMessageConverter xmLtoMessageConverter;

	@Autowired
	Logger logger;

	@Autowired
	public ReplyingKafkaTemplate<String, String, String> replyingTemplate;

	/**
	 * make validation + call the repository to fetch the message
	 * 
	 * @param messageDTO
	 * @return
	 * @throws ProjBusinessException
	 */
	public MessageDTO sendMessage(MessageDTO messageDTO) throws ProjBusinessException {

		validateInput(messageDTO);
		String xml = messagetoXMLConverter.apply(messageDTO);

		ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_1, null, "message", xml);
		RequestReplyFuture<String, String, String> future = replyingTemplate.sendAndReceive(record);
		String outputFromTopic1;
		try {
			outputFromTopic1 = future.get(30, TimeUnit.SECONDS).value();
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			throw new ProjBusinessException(
					new StatusDTO(OutputStatusEnum.UNEXPECTED, "fail on receiveMessage from topic1"));
		}
		logger.info("output from topic1:" + outputFromTopic1);

		// if message was retreived from topic 1, forward it to topic 2(automatically)
		ProducerRecord<String, String> record2 = new ProducerRecord<>(TOPIC_2, null, "message", outputFromTopic1);
		RequestReplyFuture<String, String, String> future2 = replyingTemplate.sendAndReceive(record2);
		String outputFromTopic2;
		try {
			outputFromTopic2 = future2.get(30, TimeUnit.SECONDS).value();
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			throw new ProjBusinessException(
					new StatusDTO(OutputStatusEnum.UNEXPECTED, "fail on receiveMessage from topic2"));
		}

		logger.info("output from topic2:" + outputFromTopic2);

		MessageDTO newMessageDTO = xmLtoMessageConverter.apply(outputFromTopic2);

		return newMessageDTO;

	}

	/**
	 * make soe validation of the qtys
	 * 
	 * @param baseInputDTO
	 * @return
	 * @throws ProjBusinessException
	 */
	public Boolean validateInput(BaseInputDTO baseInputDTO) {
		MessageDTO messageDTO = (MessageDTO) baseInputDTO;

		return true;
	}

}