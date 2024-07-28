//package com.sparta.itsmine.chat.entity;
//
//import com.sparta.itsmine.domain.user.entity.User;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Message extends Timestamp {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "join_chat_id")
//    private JoinChat joinChat;
//
//    private String content;
//
//    public static Message createMessage(ChatRoom room, User user, JoinChat message) {
//        return new Message();
//    }
//}
