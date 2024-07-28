//package com.sparta.itsmine.chat.entity;
//
//import com.sparta.itsmine.domain.user.entity.User;
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
//import java.util.List;
//import lombok.AccessLevel;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class JoinChat extends Timestamp {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long Id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    private ChatRoom chatRoom;
//
//    @OneToMany(mappedBy = "joinChat", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Message> messageList;
//
//    @Builder
//    public JoinChat(ChatRoom chatRoom, User user) {
//        this.chatRoom = chatRoom;
//        this.user = user;
//    }
//
//    public static JoinChat createChat(ChatRoom room, User user) {
//        return JoinChat.builder()
//                .chatRoom(room)
//                .user(user)
//                .build();
//    }
//}
