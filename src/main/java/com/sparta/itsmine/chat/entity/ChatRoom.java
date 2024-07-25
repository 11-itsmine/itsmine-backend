package com.sparta.itsmine.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;

@Getter
@Entity
public class ChatRoom extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ChatStatus chatStatus = ChatStatus.ACTIVE;

    @OneToMany(mappedBy = "chatRoom")
    private List<JoinChat> chatRoomList;

    public ChatRoom() {

    }

    public static ChatRoom createRoom() {
        return new ChatRoom();
    }
}
