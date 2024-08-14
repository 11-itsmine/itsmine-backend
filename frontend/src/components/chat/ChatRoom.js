import React from 'react';
import styled from 'styled-components';

const ChatRoom = ({room, onOpenChat}) => {
  const isFromUser = room.fromUserId === room.userDetailId;
  const otherUserNickname = isFromUser ? room.toUserNickname
      : room.fromUserNickname;
  const otherUserStatus = isFromUser ? room.toUserStatus : room.fromUserStatus;

  // fromUserStatus가 'END'일 경우만 렌더링하지 않음
  if (room.fromUserStatus === 'END') {
    return null;
  }

  return (
      <ChatRoomItem onClick={onOpenChat}>
        <ChatDetails>
          <RoomName>
            {otherUserNickname} ({otherUserStatus}) {/* 상태를 항상 표시 */}
          </RoomName>
          <LastMessage>{room.productName}</LastMessage>
        </ChatDetails>
      </ChatRoomItem>
  );
};

export default ChatRoom;

const ChatRoomItem = styled.li`
  background-color: #f5f5f5;
  padding: 15px;
  margin-bottom: 8px;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.3s ease;

  &:last-child {
    margin-bottom: 0;
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
  font-size: 0.85em;
  color: #333;
  font-weight: bold;
`;

const LastMessage = styled.p`
  margin: 0;
  font-size: 0.75em;
  color: #777;
`;
