package com.sparta.itsmine.domain.product.utils.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {

    private final AmqpTemplate amqpTemplate;

    public MessageSenderService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(Long productId, long delayMillis) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("x-delay", delayMillis);

        Message message = new Message(productId.toString().getBytes(), messageProperties);
        amqpTemplate.send(RabbitConfig.DELAYED_EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, message);
    }
}
