import React from 'react';
import styled from 'styled-components';

const ChatRoom = ({room, onOpenChat}) => {
  console.log(room);

  // 상대방의 닉네임과 마지막 메시지, 날짜를 가져옵니다.
  const otherUserNickname = room.fromUserId === room.userDetailId
      ? room.toUserNickname : room.fromUserNickname;
  const lastMessage = room.lastMessage || 'No messages yet'; // 가상의 속성, 실제로는 서버에서 전달받는 데이터 사용
  const lastMessageDate = new Date(
      room.lastMessageTime || Date.now()).toLocaleDateString('ko-KR', {
    month: 'short',
    day: 'numeric',
  }); // 가상의 속성, 실제로는 서버에서 전달받는 데이터 사용

  return (
      <ChatRoomItem onClick={onOpenChat}>
        <ChatDetails>
          <RoomName>{otherUserNickname}</RoomName>
          <LastMessage>{lastMessage}</LastMessage>
        </ChatDetails>
        <MessageDate>{lastMessageDate}</MessageDate>
      </ChatRoomItem>
  );
};

export default ChatRoom;

// 스타일 정의
const ChatRoomItem = styled.li`
  background-color: #f5f5f5;
  padding: 15px;
  border-bottom: 1px solid #ddd;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:hover {
    background-color: #e0e0e0;
  }
`;

const ChatDetails = styled.div`
  display: flex;
  flex-direction: column;
`;

const RoomName = styled.h4`
  margin: 0;
  font-size: 1em;
  color: #333;
  font-weight: bold;
`;

const LastMessage = styled.p`
  margin: 0;
  font-size: 0.9em;
  color: #777;
`;

const MessageDate = styled.div`
  font-size: 0.85em;
  color: #999;
`;
