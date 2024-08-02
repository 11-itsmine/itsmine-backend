import React from 'react';
import {useNavigate} from 'react-router-dom';

const ChatRoom = ({room}) => {
  const navigate = useNavigate(); // 페이지 이동을 위한 훅

  // 방에 들어가는 함수
  const handleEnterRoom = () => {
    navigate(`/chatroom/${room.id}`); // 클릭 시 해당 방으로 이동
  };

  return (
      <li className="chat-room" onClick={handleEnterRoom}>
        <div className="chat-room-info">
          <h3>{room.name}</h3>
          <button onClick={handleEnterRoom}>Enter</button>
          {/* 버튼으로 방 입장 */}
        </div>
      </li>
  );
};

export default ChatRoom;
