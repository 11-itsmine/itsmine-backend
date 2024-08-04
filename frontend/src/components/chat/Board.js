// Board.js

import React, { useState, useEffect } from 'react';
import axiosInstance from '../../api/axiosInstance'; // axios 인스턴스 가져오기
import ChatRoom from './ChatRoom';
import ChatWindow from './ChatWindow'; // ChatWindow 컴포넌트 가져오기
import Modal from './Modal'; // Modal 컴포넌트 가져오기
import styled from 'styled-components';

const Board = () => {
  const [chatRooms, setChatRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedRoomId, setSelectedRoomId] = useState(null);

  // 채팅방 목록 가져오기
  const fetchRooms = async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await axiosInstance.get('/chatrooms');
      const { data } = response;

      if (data && Array.isArray(data.data)) {
        setChatRooms(data.data);
      } else {
        console.error('Unexpected data format:', data);
        setChatRooms([]);
      }
    } catch (error) {
      console.error('Failed to fetch chat rooms:', error);
      setError('채팅방 목록을 불러오는 중 오류가 발생했습니다.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRooms();
  }, []);

  // 모달 열기 함수
  const openModal = (roomId) => {
    setSelectedRoomId(roomId);
    setIsModalOpen(true);
  };

  // 모달 닫기 함수
  const closeModal = () => {
    setSelectedRoomId(null);
    setIsModalOpen(false);
    fetchRooms(); // 채팅방 목록 새로고침
  };

  // 채팅방 렌더링
  const renderRooms = () => {
    return chatRooms.map((room) => (
        <ChatRoom
            key={room.roomId}
            room={room}
            onOpenChat={() => openModal(room.roomId)} // openModal 함수 전달
        />
    ));
  };

  return (
      <BoardContainer>
        <h2>Chat Rooms</h2>
        {loading ? (
            <p>로딩 중...</p>
        ) : error ? (
            <ErrorMessage>{error}</ErrorMessage>
        ) : chatRooms.length === 0 ? (
            <p>참여 중인 채팅방이 없습니다.</p>
        ) : (
            <ChatRoomList>
              {renderRooms()} {/* renderRooms 함수 호출 */}
            </ChatRoomList>
        )}

        {/* 모달을 통해 ChatWindow 컴포넌트 표시 */}
        <Modal isOpen={isModalOpen} onClose={closeModal}>
          {selectedRoomId && <ChatWindow roomId={selectedRoomId} />} {/* ChatWindow에 roomId 전달 */}
        </Modal>
      </BoardContainer>
  );
};

export default Board;

// 스타일링 정의
const BoardContainer = styled.div`
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const ChatRoomList = styled.ul`
  list-style-type: none;
  padding: 0;
`;

const ErrorMessage = styled.p`
  color: #d9534f;
  font-weight: bold;
`;
