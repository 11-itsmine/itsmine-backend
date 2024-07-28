package com.sparta.itsmine.chat.repository;

import com.sparta.itsmine.chat.entity.Message;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findAllByRoomId(String roodId);
}
