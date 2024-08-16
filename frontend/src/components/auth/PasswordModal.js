import React from 'react';
import styled from 'styled-components';

const PasswordModal = ({ message, onClose }) => {
  return (
      <ModalOverlay>
        <ModalContent>
          <p>{message}</p>
          <CloseButton onClick={onClose}>닫기</CloseButton>
        </ModalContent>
      </ModalOverlay>
  );
};

const ModalOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
`;

const ModalContent = styled.div`
  background-color: white;
  padding: 2rem;
  border-radius: 10px;
  text-align: center;
  max-width: 400px;
  width: 100%;
`;

const CloseButton = styled.button`
  background: #333333;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;

  &:hover {
    background-color: #555555;
  }

  &:active {
    background-color: #111111;
    transform: scale(0.98);
  }
`;

export default PasswordModal;
