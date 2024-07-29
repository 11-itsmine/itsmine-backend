package com.sparta.itsmine.domain.product.utils.rabbitmq;

public interface ProducerService {

    // 메시지를 큐로 전송 합니다.
    void sendMessage(MessageDto messageDto);
}