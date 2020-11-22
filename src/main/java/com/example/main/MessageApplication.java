package com.example.main;

import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.restservice.converter.MessageToXMLConverter;
import com.example.restservice.converter.XMLtoMessageConverter;
import com.example.restservice.dto.MessageDTO;
import com.thoughtworks.xstream.XStream;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static com.example.restservice.service.MessageSendService.TOPIC_1;
import static com.example.restservice.service.MessageSendService.TOPIC_2;

@SpringBootApplication
@ComponentScan("com.example")
@EnableSwagger2
@EnableWebMvc

public class MessageApplication implements WebMvcConfigurer{

	
	@Autowired
	@Qualifier("messageToXMLConverter")
	MessageToXMLConverter messagetoXMLConverter;
	
	@Autowired
	@Qualifier("xmlToMessageConverter")
	XMLtoMessageConverter xmLtoMessageConverter;
	
    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
    }

    @KafkaListener(id = TOPIC_1, topics = TOPIC_1)
    @SendTo
    public String handleTopic1(String str) {
        System.out.println(str);
        return str;
    }

    @KafkaListener(id = TOPIC_2, topics = TOPIC_2)
    @SendTo
    public String handleTopic2(String str) {
        System.out.println(str);
        return str;
    }
    
    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingTemplate(ProducerFactory<String, String> pf,
            ConcurrentKafkaListenerContainerFactory<String, String> factory) {

        ConcurrentMessageListenerContainer<String, String> replyContainer =
                factory.createContainer(TOPIC_2);
        replyContainer.getContainerProperties().setGroupId("topic2.reply");
        ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate = new ReplyingKafkaTemplate<>(pf, replyContainer);
        return replyingKafkaTemplate;
    }

    @Bean
    public KafkaTemplate<String, String> replyTemplate(ProducerFactory<String, String> pf,
            ConcurrentKafkaListenerContainerFactory<String, String> factory) {

        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }

//    @Bean
//    public ApplicationRunner runner(ReplyingKafkaTemplate<String, String, String> template) {
//        return args -> {
//        	MessageDTO messageDTO = new MessageDTO();
//        	messageDTO.setMessage("00000");
//        	messageDTO.setType("11111");
//    		String xml = messagetoXMLConverter.apply(messageDTO);
//        	
//        	
//            ProducerRecord<String, String> record = new ProducerRecord<>("topic1", null, "message", xml);
//            RequestReplyFuture<String, String, String> future = template.sendAndReceive(record);
//            System.out.println(future.get(10, TimeUnit.SECONDS).value());
//        };
//    }

    @Bean
    public NewTopic topic() {
        return new NewTopic(TOPIC_1, 1, (short) 1);
    }

    @Bean
    public NewTopic reply() {
        return new NewTopic(TOPIC_2, 1, (short) 1);
    }


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Demo")
				.description("commIT Project ")
				.license("Apache 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
				.termsOfServiceUrl("")
				.version("1.0.0")
				.build();
	}
	
	@Bean
	public XStream xStream() {

//		return new XStream(new JsonHierarchicalStreamDriver());
		return new XStream();

	}


	
//	@Bean 
//	public ModelMapper getModelMapper() {
//		ModelMapper mapper = new ModelMapper();
//		mapper.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.STANDARD);
//		mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
//		
//		return mapper;
//
//	}
	

	 
		@Bean(value = "myLogger")
		@Scope("prototype")
		Logger logger(InjectionPoint _injectionPoint) {
			String callingClass = "General";
			if (_injectionPoint != null) {
				if (_injectionPoint.getMember() != null) {
					callingClass = _injectionPoint.getMember()
							.getDeclaringClass()
							.getName();
				} else if (_injectionPoint.getField() != null) {
					callingClass = _injectionPoint.getField()
							.getDeclaringClass()
							.getName();
				} else if (_injectionPoint.getMethodParameter() != null) {
					callingClass = _injectionPoint.getMethodParameter()
							.getContainingClass()
							.getName();
				}
			}

			Logger myLogger = LoggerFactory.getLogger(callingClass);

			return myLogger;
		}    
		    
}