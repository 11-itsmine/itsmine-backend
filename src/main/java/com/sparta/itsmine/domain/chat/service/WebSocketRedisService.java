package com.sparta.itsmine.domain.chat.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.sparta.itsmine.domain.chat.Entity.ChatMessage;

@Service
public class WebSocketRedisService {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private RedisMessagePublisher redisMessagePublisher;

	public void sendMessage(ChatMessage chatMessage) {
		redisMessagePublisher.publish(chatMessage.getContent());
	}

	public void handleMessageFromRedis(String message) {
		messagingTemplate.convertAndSend("/topic/public", message);
	}
}
