package com.sparta.itsmine.global.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sparta.itsmine.domain.chat.dto.MessageRequestDto;
import jakarta.jms.Queue;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ActiveMqConfig {

    @Value("${spring.activemq.broker-url}")
    private String activemqBrokerUrl;

    @Value("${spring.activemq.user}")
    private String activemqUsername;

    @Value("${spring.activemq.password}")
    private String activemqPassword;

    @Value("${activemq.queue.name}")
    private String queueName;

    /**
     * 지정된 큐 이름으로 Queue 빈을 생성
     *
     * @return Queue 빈 객체
     */
    @Bean
    public Queue queue() {
        return new ActiveMQQueue(queueName);
    }

    /**
     * ActiveMQ SSL ConnectionFactory를 생성하여 반환합니다.
     *
     * @return ActiveMQSslConnectionFactory 객체
     */
    @Bean
    public ActiveMQSslConnectionFactory activeMQConnectionFactory() throws Exception {
        ActiveMQSslConnectionFactory factory = new ActiveMQSslConnectionFactory();
        factory.setBrokerURL(activemqBrokerUrl);
        factory.setUserName(activemqUsername);
        factory.setPassword(activemqPassword);

        // SSL 설정
//        factory.setTrustStore("src/main/resources/bootsecurity.p12"); // TrustStore 경로를 설정하세요.
//        factory.setTrustStorePassword("skdmlcjs");

        return factory;
    }

    /**
     * JmsTemplate을 생성하여 반환합니다.
     *
     * @return JmsTemplate 객체
     */
    @Bean
    public JmsTemplate jmsTemplate() throws Exception {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setExplicitQosEnabled(true);    // 메시지 전송 시 QOS을 설정
        jmsTemplate.setDeliveryPersistent(false);   // 메시지의 영속성을 설정
        jmsTemplate.setReceiveTimeout(1000 * 3);    // 메시지를 수신하는 동안의 대기 시간을 설정(3초)
        jmsTemplate.setTimeToLive(1000 * 60 * 30);  // 메시지의 유효 기간을 설정(30분)
        return jmsTemplate;
    }

    /**
     * JmsListenerContainerFactory을 위한 빈을 생성
     *
     * @return JmsTemplate
     */
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory() throws Exception {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory());
        factory.setMessageConverter(jacksonJmsMessageConverter());
        return factory;
    }

    /**
     * MessageConverter을 위한 빈을 생성
     *
     * @return MessageConverter
     */
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        // ObjectMapper 설정
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
        converter.setObjectMapper(objectMapper);

        return converter;
    }
}
