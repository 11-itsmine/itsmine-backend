// ChatRoom.js
import React from 'react';
import styled from 'styled-components';

const ChatRoom = ({room, onOpenChat}) => {
  const isFromUser = room.fromUserId === room.userDetailId;
  const otherUserNickname = isFromUser ? room.toUserNickname
      : room.fromUserNickname;
  const otherUserStatus = isFromUser ? room.toUserStatus : room.fromUserStatus;
  const productName = room.prductName;

  return (
      <ChatRoomItem onClick={onOpenChat}>
        <ChatDetails>
          <RoomName>
            {otherUserNickname} ({otherUserStatus})
          </RoomName>
          <LastMessage>{productName}</LastMessage>
        </ChatDetails>
      </ChatRoomItem>
  );
};

export default ChatRoom;

const ChatRoomItem = styled.li`
  background-color: #f5f5f5;
  padding: 15px;
  margin-bottom: 8px; /* 아이템 간격 조정 */
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:last-child {
    margin-bottom: 0; /* 마지막 아이템의 하단 여백 제거 */
  }

  &:hover {
    background-color: #e0e0e0;
  }
`;

const ChatDetails = styled.div`
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow: hidden;
`;

const RoomName = styled.h4`
  margin: 0;
  font-size: 0.85em; /* 폰트 크기를 줄여서 텍스트가 더 잘 맞도록 설정 */
  color: #333;
  font-weight: bold;
`;

const LastMessage = styled.p`
  margin: 0;
  font-size: 0.75em; /* 폰트 크기를 줄여서 공간을 절약 */
  color: #777;
`;