package com.sparta.itsmine.domain.chat.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

	@Override
	public void onMessage(Message message, byte[] pattern) {
		System.out.println("받은 메세지: " + new String(message.getBody()));
	}
}
