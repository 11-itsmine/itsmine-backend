// Chat.js

import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstance';
import ChatRoom from './ChatRoom';
import './Chat.css';
import { useChat } from './ChatContext';
import ChatWindow from './ChatWindow';

const Chat = () => {
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { isModalOpen, selectedRoomId, closeModal } = useChat();

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await axiosInstance.get('/v1/chatrooms');
        console.log('방 목록 응답:', response);

        const { data } = response;

        if (data && Array.isArray(data.data)) {
          if (!areArraysEqual(rooms, data.data)) {
            console.log('채팅방 데이터가 배열입니다. 상태 업데이트:', data.data);
            setRooms(data.data);
          }
        } else {
          console.error('예상치 못한 데이터 형식:', data);
          setRooms([]);
        }
      } catch (error) {
        console.error('채팅방 목록을 불러오는 중 오류 발생:', error);
        setError('채팅방 목록을 불러오는 중 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchRooms();
  }, []);

  const areArraysEqual = (arr1, arr2) => {
    if (arr1.length !== arr2.length) return false;
    for (let i = 0; i < arr1.length; i++) {
      if (arr1[i].roomId !== arr2[i].roomId) return false;
    }
    return true;
  };

  const renderRooms = () => {
    return rooms.map((room) => (
        <ChatRoom
            key={room.roomId}
            room={room}
        />
    ));
  };

  return (
      <div className="chat">
        <h2>Chat Rooms</h2>
        {loading ? (
            <p>로딩 중...</p>
        ) : error ? (
            <p>{error}</p>
        ) : rooms.length === 0 ? (
            <p>참여 중인 채팅방이 없습니다.</p>
        ) : (
            <ul className="chat-room-list">
              {renderRooms()}
            </ul>
        )}

        {isModalOpen && selectedRoomId && <ChatWindow roomId={selectedRoomId} onClose={closeModal} />}
      </div>
  );
};

export default Chat;
