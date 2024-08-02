import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstance'; // 인증 헤더가 포함된 axios 인스턴스
import ChatRoom from './ChatRoom';
import './Chat.css';

const Chat = () => {
  const [rooms, setRooms] = useState([]); // 빈 배열로 초기화

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        // 인증된 요청을 사용하여 방 목록을 가져옵니다.
        const response = await axiosInstance.get('/chatrooms');
        console.log('방 목록 응답:', response);

        // 응답 데이터가 배열인지 확인
        const { data } = response;
        if (Array.isArray(data)) {
          setRooms(data); // 배열일 경우 상태 업데이트
          console.log('채팅방 데이터가 배열입니다. 상태 업데이트:', data);
        } else {
          console.error('예상치 못한 데이터 형식:', data);
          setRooms([]); // 배열이 아닐 경우 빈 배열로 설정
        }
      } catch (error) {
        console.error('채팅방 목록을 불러오는 중 오류 발생:', error);
      }
    };

    fetchRooms(); // 데이터 가져오기 함수 호출
  }, []);

  // for 루프를 사용하여 채팅방 렌더링
  const renderRooms = () => {
    const roomElements = [];
    for (let i = 0; i < rooms.length; i++) {
      const room = rooms[i];
      roomElements.push(<ChatRoom key={room.id} room={room} />);
    }
    return roomElements;
  };

  return (
      <div className="chat">
        <h2>Chat Rooms</h2>
        <ul className="chat-room-list">
          {rooms.length === 0 ? (
              <p>참여 중인 채팅방이 없습니다.</p> // 빈 배열일 때 메시지
          ) : (
              renderRooms() // .map 대신 renderRooms 함수 호출
          )}
        </ul>
      </div>
  );
};

export default Chat;
