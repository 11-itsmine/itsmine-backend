// Modal.js

import React from 'react';
import styled from 'styled-components';

const Modal = ({ isOpen, children, onClose }) => {
  if (!isOpen) return null;

  return (
      <ModalOverlay>
        <ModalContent>
          <CloseButton onClick={onClose}>X</CloseButton>
          {children}
        </ModalContent>
      </ModalOverlay>
  );
};

export default Modal;

// Styles
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

const ModalContent = styled.div`
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  max-width: 700px;
  width: 90%;
  position: relative;
`;

const CloseButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  background-color: transparent;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  transition: color 0.2s;
  &:hover {
    color: #ff0000;
  }
`;
