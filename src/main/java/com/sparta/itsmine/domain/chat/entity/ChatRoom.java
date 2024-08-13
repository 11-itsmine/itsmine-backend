package com.sparta.itsmine.domain.chat.entity;

import com.sparta.itsmine.domain.product.entity.Product;
import com.sparta.itsmine.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "chat_room", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"roomId"})
})
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    @Enumerated(EnumType.STRING)
    private ChatStatus fromUserStatus;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "prduct_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private ChatStatus toUserStatus;

    private LocalDateTime deletedAt;

    public ChatRoom(User fromUser, User toUser, Product product) {
        this.roomId = UUID.randomUUID().toString();
        this.fromUser = fromUser;
        this.fromUserStatus = ChatStatus.TALK;
        this.toUser = toUser;
        this.toUserStatus = ChatStatus.TALK;
        this.product = product;
    }

    public void userStatusUpdate(User user) {
        if (user.getId().equals(fromUser.getId())) {
            this.fromUserStatus = ChatStatus.END;
        } else {
            this.toUserStatus = ChatStatus.END;
        }
    }

    public void blockChatRoom(User user) {
        this.deletedAt = LocalDateTime.now();
        if (user.getId().equals(fromUser.getId())) {
            this.fromUserStatus = ChatStatus.BLOCK;
        } else {
            this.toUserStatus = ChatStatus.BLOCK;
        }
    }

}
