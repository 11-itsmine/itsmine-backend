package com.sparta.itsmine.domain.product.scheduler;

import static com.sparta.itsmine.global.common.config.RabbitConfig.DLX_EXCHANGE_NAME;
import static com.sparta.itsmine.global.common.config.RabbitConfig.PRODUCT_ROUTING_KEY;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSenderService {

    private final AmqpTemplate amqpTemplate;

    /**
     * 지정된 시간만큼 지연 후 메시지를 전송합니다.
     *
     * @param productId   전송할 메시지의 상품 ID
     * @param delayMillis 메시지를 지연시킬 시간 (밀리초 단위)
     */
    public void sendMessage(Long productId, long delayMillis) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("x-delay", delayMillis);

        Message message = new Message(productId.toString().getBytes(), messageProperties);
        amqpTemplate.send(DLX_EXCHANGE_NAME,
                PRODUCT_ROUTING_KEY, message);
    }
}
