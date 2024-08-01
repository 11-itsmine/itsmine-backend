package com.sparta.itsmine.global.common.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
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

@Configuration
@EnableRabbit
public class RabbitConfig {

    public static final String PRODUCT_ROUTING_KEY = "product.routing.key";
    public static final String PRODUCT_QUEUE_NAME = "product.queue";
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    protected static final String DELAYED_QUEUE_NAME = "delayed.queue";
    private static final String CHAT_QUEUE_NAME = "chat.queue";
    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    private static final String CHAT_ROUTING_KEY = "*.room.*";

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

    /**
     * productQueue를 생성합니다.
     *
     * @return 생성된 Queue 객체
     */
    @Bean
    @Qualifier("productQueue")
    public Queue productQueue() {
        return new Queue(PRODUCT_QUEUE_NAME, true);
    }

    /**
     * delayedQueue를 생성합니다.
     *
     * @return 생성된 Queue 객체
     */
    @Bean
    @Qualifier("delayedQueue")
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE_NAME).build();
    }

    /**
     * chatQueue를 생성합니다.
     *
     * @return 생성된 Queue 객체
     */
    @Bean
    @Qualifier("chatQueue")
    public Queue chatQueue() {
        return new Queue(CHAT_QUEUE_NAME, true);
    }

    /**
     * 지연된 메시지 전송을 위한 커스텀 교환기를 생성합니다.
     *
     * @return 생성된 CustomExchange 객체
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    /**
     * chatExchange를 생성합니다.
     *
     * @return 생성된 TopicExchange 객체
     */
    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange(CHAT_EXCHANGE_NAME, true, false);
    }

    /**
     * productQueue와 delayedExchange를 바인딩합니다.
     *
     * @param productQueue    바인딩할 Queue 객체
     * @param delayedExchange 바인딩할 CustomExchange 객체
     * @return 생성된 Binding 객체
     */
    @Bean
    public Binding productBinding(@Qualifier("productQueue") Queue productQueue,
            CustomExchange delayedExchange) {
        return BindingBuilder.bind(productQueue).to(delayedExchange).with(PRODUCT_ROUTING_KEY)
                .noargs();
    }

    /**
     * delayedQueue와 delayedExchange를 바인딩합니다.
     *
     * @param delayedQueue    바인딩할 Queue 객체
     * @param delayedExchange 바인딩할 CustomExchange 객체
     * @return 생성된 Binding 객체
     */
    @Bean
    public Binding delayedBinding(@Qualifier("delayedQueue") Queue delayedQueue,
            CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(PRODUCT_ROUTING_KEY)
                .noargs();
    }

    /**
     * chatQueue와 chatExchange를 바인딩합니다.
     *
     * @param chatQueue    바인딩할 Queue 객체
     * @param chatExchange 바인딩할 TopicExchange 객체
     * @return 생성된 Binding 객체
     */
    @Bean
    public Binding chatBinding(@Qualifier("chatQueue") Queue chatQueue,
            TopicExchange chatExchange) {
        return BindingBuilder.bind(chatQueue).to(chatExchange).with(CHAT_ROUTING_KEY);
    }

    /**
     * RabbitMQ 메시지 리스너 컨테이너 팩토리를 생성합니다.
     *
     * @param connectionFactory 연결 팩토리 객체
     * @return 생성된 SimpleRabbitListenerContainerFactory 객체
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
     * RabbitTemplate 객체를 생성합니다.
     *
     * @param connectionFactory 연결 팩토리 객체
     * @return 생성된 RabbitTemplate 객체
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * RabbitMQ 연결 팩토리를 생성합니다.
     *
     * @return 생성된 ConnectionFactory 객체
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
     * JSON 메시지 변환기를 생성합니다.
     *
     * @return 생성된 Jackson2JsonMessageConverter 객체
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
