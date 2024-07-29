//package com.sparta.itsmine.chat.repository;
//
//import com.sparta.itsmine.chat.entity.Message;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class MessageAdapter {
//
//    private final MessageRepository messageRepository;
//
//    public List<Message> findAllByJoinChatId(Long joinChatId) {
//        return messageRepository.findAllByJoinChatId(joinChatId);
//    }
//
//    public Message save(Message message) {
//        return messageRepository.save(message);
//    }
//}
