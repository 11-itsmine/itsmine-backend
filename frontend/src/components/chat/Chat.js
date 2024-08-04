// Chat.js

import React, { useEffect, useState } from 'react';
import axiosInstance from '../../api/axiosInstance'; // 인증 헤더가 포함된 axios 인스턴스
import ChatRoom from './ChatRoom';
import './Chat.css';
import { useChat } from './ChatContext'; // ChatContext에서 useChat 훅 가져오기
import ChatWindow from './ChatWindow'; // ChatWindow 컴포넌트 가져오기

const Chat = () => {
  const [rooms, setRooms] = useState([]); // 초기값으로 빈 배열 설정
  const [loading, setLoading] = useState(true); // 로딩 상태 관리
  const [error, setError] = useState(null); // 오류 상태 관리
  const { isModalOpen, selectedRoomId, closeModal } = useChat(); // 전역 상태 가져오기

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await axiosInstance.get('/chatrooms');
        console.log('방 목록 응답:', response);

        const { data } = response;

        // 응답의 data 필드가 배열인지 확인하고 상태 업데이트
        if (data && Array.isArray(data.data)) {
          // 상태가 이전과 동일한 경우 업데이트하지 않음
          if (!areArraysEqual(rooms, data.data)) {
            console.log('채팅방 데이터가 배열입니다. 상태 업데이트:', data.data);
            setRooms(data.data);
          }
        } else {
          console.error('예상치 못한 데이터 형식:', data);
          setRooms([]); // 배열이 아닐 경우 빈 배열로 설정
        }
      } catch (error) {
        console.error('채팅방 목록을 불러오는 중 오류 발생:', error);
        setError('채팅방 목록을 불러오는 중 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchRooms();
  }, []); // rooms 의존성 제거

  // 배열 비교 함수
  const areArraysEqual = (arr1, arr2) => {
    if (arr1.length !== arr2.length) return false;
    for (let i = 0; i < arr1.length; i++) {
      if (arr1[i].roomId !== arr2[i].roomId) return false;
    }
    return true;
  };

  // 채팅방 렌더링
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
              {renderRooms()} {/* renderRooms 함수 호출 */}
            </ul>
        )}

        {/* 선택된 채팅방이 있으면 ChatWindow 컴포넌트 표시 */}
        {isModalOpen && selectedRoomId && <ChatWindow roomId={selectedRoomId} onClose={closeModal} />}
      </div>
  );
};

export default Chat;
