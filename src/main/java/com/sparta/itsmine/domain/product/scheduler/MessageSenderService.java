package com.sparta.itsmine.domain.product.scheduler;

import static com.sparta.itsmine.global.common.config.RabbitConfig.MAIN_EXCHANGE_NAME;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSenderService {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 지정된 시간만큼 지연 후 메시지를 전송합니다.
     *
     * @param productId   전송할 메시지의 상품 ID
     * @param delayMillis 메시지를 지연시킬 시간 (밀리초 단위)
     */
    public void sendMessage(Long productId, long delayMillis) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration(String.valueOf(delayMillis)); // TTL 설정

        log.info("Product ID : {}, Delay : {}", productId, delayMillis);

        Message message = new Message(productId.toString().getBytes(), messageProperties);
        rabbitTemplate.send(MAIN_EXCHANGE_NAME, "product.routing.key", message);
    }
}
