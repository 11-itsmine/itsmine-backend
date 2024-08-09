package com.sparta.itsmine.global.common.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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

    public static final String MAIN_EXCHANGE_NAME = "main.exchange";
    public static final String DLX_EXCHANGE_NAME = "dlx.exchange";
    public static final String PRODUCT_ROUTING_KEY = "product.routing.key";
    public static final String PRODUCT_QUEUE_NAME = "product.queue";
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";

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

    @Bean
    public DirectExchange mainExchange() {
        return new DirectExchange(MAIN_EXCHANGE_NAME, true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE_NAME, true, false);
    }

    @Bean
    @Qualifier("productQueue")
    public Queue productQueue() {
        return QueueBuilder.durable(PRODUCT_QUEUE_NAME).build();
    }

    @Bean
    @Qualifier("delayedQueue")
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE_NAME)
                .withArgument("x-dead-letter-exchange", MAIN_EXCHANGE_NAME)
                .withArgument("x-dead-letter-routing-key", PRODUCT_ROUTING_KEY)
                .build();
    }

    @Bean
    @Qualifier("chatQueue")
    public Queue chatQueue() {
        return QueueBuilder.durable(CHAT_QUEUE_NAME).build();
    }

    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange(CHAT_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding productBinding(@Qualifier("productQueue") Queue productQueue,
            @Qualifier("mainExchange") DirectExchange mainExchange) {
        return BindingBuilder.bind(productQueue).to(mainExchange).with(PRODUCT_ROUTING_KEY);
    }

    @Bean
    public Binding delayedBinding(@Qualifier("delayedQueue") Queue delayedQueue,
            @Qualifier("dlxExchange") DirectExchange dlxExchange) {
        return BindingBuilder.bind(delayedQueue).to(dlxExchange).with(PRODUCT_ROUTING_KEY);
    }

    @Bean
    public Binding chatBinding(@Qualifier("chatQueue") Queue chatQueue,
            TopicExchange chatExchange) {
        return BindingBuilder.bind(chatQueue).to(chatExchange).with(CHAT_ROUTING_KEY);
    }

    @Bean
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO); // 자동 처리 메세지 삭제 설정
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory()
            throws NoSuchAlgorithmException, KeyManagementException {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(rabbitHost);
        factory.setPort(rabbitPort);
        factory.setUsername(rabbitUser);
        factory.setPassword(rabbitPwd);
        factory.setVirtualHost(rabbitVh);
        factory.getRabbitConnectionFactory().useSslProtocol();
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        objectMapper.registerModule(dateTimeModule());

        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Module dateTimeModule() {
        return new JavaTimeModule();
    }
}
