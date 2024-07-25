package com.sparta.itsmine.chat.repository;

import com.sparta.itsmine.chat.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByJoinChatId(Long joinChatId);

}
