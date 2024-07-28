package com.sparta.itsmine.chat.entity;

import com.sparta.itsmine.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    @OneToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @OneToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JoinChat> joinChats = new ArrayList<>();

    public ChatRoom(User user, User fromUser) {
        this.roomId = UUID.randomUUID().toString();
        this.toUser = user;
        this.fromUser = fromUser;
        Stream.of(user, fromUser)
                .map(streamUser -> JoinChat.createChat(this, streamUser))
                .forEach(joinChats::add);
    }
}
