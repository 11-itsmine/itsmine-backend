// ChatRoom.js

import React from 'react';
import styled from 'styled-components';

const ChatRoom = ({ room, onOpenChat }) => {
  console.log(room);

  // 현재 로그인한 사용자가 발신자인지 수신자인지를 판단
  const isCurrentUserSender = room.fromUserId === room.userDetailId;

  // 나 또는 상대방으로 표현하기
  const senderLabel = isCurrentUserSender ? '나' : '상대방';
  const receiverLabel = !isCurrentUserSender ? '나' : '상대방';

  // 상대방과 나의 정보를 한 번에 출력하기 위한 변수
  const otherUserNickname = isCurrentUserSender ? room.toUserNickname : room.fromUserNickname;
  const otherUserStatus = isCurrentUserSender ? room.toUserStatus : room.fromUserStatus;
  const currentUserNickname = isCurrentUserSender ? room.fromUserNickname : room.toUserNickname;
  const currentUserStatus = isCurrentUserSender ? room.fromUserStatus : room.toUserStatus;

  return (
      <ChatRoomItem onClick={onOpenChat}> {/* 클릭 시 채팅 열기 */}
        {/* 채팅방 ID는 필요할 경우 여기에 추가 */}
        {/* <RoomId>채팅방 ID: {room.roomId}</RoomId> */}
        <UserInfo>
          상대방 : ({otherUserNickname}) (상태: {otherUserStatus})
        </UserInfo>
        <UserInfo>
          나 : ({currentUserNickname}) (상태: {currentUserStatus})
        </UserInfo>
      </ChatRoomItem>
  );
};

export default ChatRoom;

// 스타일 정의
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
  display: none; /* 채팅방 ID는 보이지 않도록 설정 */
`;

const UserInfo = styled.p`
  margin: 5px 0;
  font-size: 0.95em;
  color: #333333;
`;
