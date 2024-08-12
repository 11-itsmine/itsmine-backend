import React from 'react';
import styled from 'styled-components';

const ChatRoom = ({room, onOpenChat}) => {
  console.log(room);

  // 상대방의 닉네임과 상태를 가져옵니다.
  const isFromUser = room.fromUserId === room.userDetailId;
  const otherUserNickname = isFromUser ? room.toUserNickname
      : room.fromUserNickname;
  const otherUserStatus = isFromUser ? room.toUserStatus : room.fromUserStatus;

  // 상품명을 가져옵니다.
  const productName = room.prductName;

  return (
      <ChatRoomItem onClick={onOpenChat}>
        <ChatDetails>
          <RoomName>
            {otherUserNickname} ({otherUserStatus}) {/* 상대방의 닉네임과 상태 */}
          </RoomName>
          <LastMessage>{productName}</LastMessage> {/* 상품명 표시 */}
        </ChatDetails>
        <MessageDate>{/* 여기에 필요에 따라 날짜를 추가할 수 있음 */}</MessageDate>
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
