// ChatRoom.js

import React from 'react';
import styled from 'styled-components';

const ChatRoom = ({ room, onOpenChat }) => {
  console.log(room)
  return (

      <ChatRoomItem onClick={onOpenChat}> {/* 클릭 시 부모에서 전달된 함수 호출 */}
        <RoomId>채팅방 ID: {room.roomId}</RoomId>
        <UserInfo>
          발신자: {room.fromUserNickname} (상태: {room.fromUserStatus})
        </UserInfo>
        <UserInfo>
          수신자: {room.toUserNickname} (상태: {room.toUserStatus})
        </UserInfo>
      </ChatRoomItem>
  );
};

export default ChatRoom;

// 스타일링 정의
const ChatRoomItem = styled.li`
  background-color: #ffffff;
  margin-bottom: 15px;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s;
  cursor: pointer;

  &:hover {
    transform: translateY(-5px);
  }
`;

const RoomId = styled.h3`
  margin-bottom: 10px;
  color: #007bff;
  font-size: 1.1em;
`;

const UserInfo = styled.p`
  margin: 5px 0;
  font-size: 0.95em;
  color: #333333;
`;
