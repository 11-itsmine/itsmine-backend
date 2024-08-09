import React from 'react';
import ChatWindow from './ChatWindow'; // ChatWindow 컴포넌트 불러오기
import { ChatProvider, useChat } from './ChatContext'; // ChatProvider 및 useChat 훅 가져오기
import styled from 'styled-components';
import Modal from './Modal'; // Modal 컴포넌트 불러오기

const ParentComponent = () => {
  return (
      // ChatProvider로 감싸서 하위 컴포넌트가 Context에 접근할 수 있도록 설정
      <ChatProvider>
        <ChatModalManager />
      </ChatProvider>
  );
};

const ChatModalManager = () => {
  const { isModalOpen, selectedRoomId, openModal, closeModal } = useChat(); // Context에서 상태와 함수 가져오기

  return (
      <div>
        {/* 여러 채팅방을 위한 버튼들 */}
        <button onClick={() => openModal('room-id-1')}>채팅방 1 열기</button>
        <button onClick={() => openModal('room-id-2')}>채팅방 2 열기</button>

        {/* 모달이 열렸을 때 ChatWindow 컴포넌트 표시 */}
        {isModalOpen && (
            <Modal isOpen={isModalOpen} onClose={closeModal}>
              <ChatWindow /> {/* ChatWindow에 Context에서 가져온 상태와 함수 전달 */}
            </Modal>
        )}
      </div>
  );
};

export default ParentComponent;

// 스타일링 정의
const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

const ModalContainer = styled.div`
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  max-width: 700px;
  width: 90%;
  position: relative;
`;
