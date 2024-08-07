// ChatContext.js

import React, { createContext, useContext, useState } from 'react';

// ChatContext 생성
const ChatContext = createContext();

// ChatProvider 컴포넌트 생성
export const ChatProvider = ({ children }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedRoomId, setSelectedRoomId] = useState(null);

  // 모달 열기 함수
  const openModal = (roomId) => {
    setSelectedRoomId(roomId);
    setIsModalOpen(true);
  };

  // 모달 닫기 함수
  const closeModal = () => {
    setIsModalOpen(false);
    setSelectedRoomId(null);
  };

  // Context Provider에서 상태 및 함수 제공
  return (
      <ChatContext.Provider value={{ isModalOpen, selectedRoomId, openModal, closeModal }}>
        {children}
      </ChatContext.Provider>
  );
};

// useChat 커스텀 훅 생성
export const useChat = () => {
  const context = useContext(ChatContext);
  if (!context) {
    throw new Error('useChat must be used within a ChatProvider');
  }
  return context;
};
