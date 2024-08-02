import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstance'; // 인증 헤더가 포함된 axios 인스턴스
import ChatRoom from './ChatRoom';
import './Chat.css';

const Chat = () => {
  const [rooms, setRooms] = useState([]);

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        // 인증된 요청을 사용하여 방 목록을 가져옵니다.
        const response = await axiosInstance.get('/chatrooms');
        setRooms(response.data);
      } catch (error) {
        console.error('Error fetching chat rooms', error);
      }
    };

    fetchRooms();
  }, []);

  return (
      <div className="chat">
        <h2>Chat Rooms</h2>
        <ul className="chat-room-list">
          {rooms.map((room) => (
              <ChatRoom key={room.id} room={room} />
          ))}
        </ul>
      </div>
  );
};

export default Chat;
