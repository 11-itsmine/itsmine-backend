package com.sparta.itsmine.domain.product.utils.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

    @RabbitListener(queues = "hello.queue")
    public void receiveMessage(String msg) {
        log.debug("결과값을 받는다 :: " + msg);
    }
}