package com.sparta.itsmine.global.common.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 메시징을 설정하는 구성 클래스입니다.
 * <p>
 * 이 클래스는 RabbitMQ와의 연결, 큐, 교환기, 바인딩 및 메시지 변환기 등을 설정합니다.
 */
@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitConfig {

    private static final String CHAT_QUEUE_NAME = "chat.queue";
    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    private static final String ROUTING_KEY = "*.room.*";

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUser;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPwd;

    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitVh;
//    @Value("${spring.rabbitmq.port}")
//    private int rabbitPort;

    /**
     * 큐 설정
     * <p>
     * CHAT_QUEUE_NAME이라는 이름의 큐를 생성합니다. true 매개변수는 큐가 지속되도록 설정합니다.
     */
    @Bean
    @Qualifier("chatQueue")
    public Queue queue() {
        return new Queue(CHAT_QUEUE_NAME, true);
    }

    /**
     * 교환기 설정
     * <p>
     * CHAT_EXCHANGE_NAME이라는 이름의 Topic Exchange를 생성합니다.
     * <p>
     * false는 자동 삭제되지 않도록 설정합니다.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(CHAT_EXCHANGE_NAME, true, false);
    }

    /**
     * 바인딩 설정
     * <p>
     * 큐와 교환기를 라우팅 키(ROUTING_KEY)를 사용하여 바인딩합니다.
     */
    @Bean
    public Binding binding(@Qualifier("chatQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * RabbitListener 컨테이너 팩토리 설정
     * <p>
     * 연결 팩토리를 설정하고, JSON 메시지 변환기를 사용하도록 설정합니다.
     */
    @Bean
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    /**
     * RabbitTemplate 설정
     * <p>
     * 연결 팩토리를 사용하여 RabbitTemplate를 설정하고, JSON 메시지 변환기를 사용하도록 설정합니다.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setRoutingKey(ROUTING_KEY);
        return rabbitTemplate;
    }

    /**
     * 연결 팩토리 설정
     * <p>
     * RabbitMQ 서버에 연결하기 위한 CachingConnectionFactory를 설정합니다.
     * <p>
     * 호스트, 포트, 사용자 이름, 비밀번호 및 가상 호스트를 설정합니다.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setVirtualHost(rabbitVh);
        factory.setUsername(rabbitUser);
        factory.setPassword(rabbitPwd);
        factory.setPort(rabbitPort);
        return factory;
    }

    /**
     * JSON 메시지 변환기 설정
     * <p>
     * Jackson ObjectMapper를 사용하여 JSON 메시지 변환기를 설정합니다.
     * <p>
     * 날짜 및 시간 모듈을 등록하여 날짜를 타임스탬프로 쓰도록 설정합니다.
     */
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.registerModule(dateTimeModule());

        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * JavaTimeModule 설정
     * <p>
     * 날짜 및 시간 API를 지원하는 모듈을 등록합니다
     */
    @Bean
    public Module dateTimeModule() {
        return new JavaTimeModule();
    }
}
