package com.example.main;

import static com.example.restservice.service.MessageSendService.TOPIC_1;
import static com.example.restservice.service.MessageSendService.TOPIC_2;

import org.apache.kafka.clients.admin.NewTopic;
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
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.restservice.converter.MessageToXMLConverter;
import com.example.restservice.converter.XMLtoMessageConverter;
import com.thoughtworks.xstream.XStream;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan("com.example")
@EnableSwagger2
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


    @Bean
    public NewTopic topic() {
        return new NewTopic(TOPIC_1, 1, (short) 1);
    }

    @Bean
    public NewTopic reply() {
        return new NewTopic(TOPIC_2, 1, (short) 1);
    }

    
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();                                           
    }

	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
	    registry.addRedirectViewController("/api/v2/api-docs", "/v2/api-docs");
	    registry.addRedirectViewController("/api/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
	    registry.addRedirectViewController("/api/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
	    registry.addRedirectViewController("/api/swagger-resources", "/swagger-resources");
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
	
	@Bean	
  public ApplicationRunner runner(ReplyingKafkaTemplate<String, String, String> template) {
  return args -> {
      System.out.println("just check runner");
  };
}
	

	 
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
