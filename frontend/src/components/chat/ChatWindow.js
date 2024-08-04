// ChatWindow.js

import React, { useEffect, useState, useRef } from 'react';
import styled from 'styled-components';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import axiosInstance from '../../api/axiosInstance'; // axios 인스턴스 가져오기

const ChatWindow = ({ roomId }) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const stompClient = useRef(null);
  const messageListRef = useRef(null); // 메시지 리스트 스크롤을 위한 ref

  useEffect(() => {
    if (!roomId) {
      console.error('Room ID is not available');
      return;
    }

    // 초기 메시지 로드
    const fetchMessages = async () => {
      try {
        const response = await axiosInstance.get(`/chatrooms/${roomId}`);
        setMessages(response.data.data || []);
      } catch (error) {
        console.error('Failed to fetch messages:', error);
      }
    };

    fetchMessages();

    // SockJS 및 STOMP 연결 설정
    const socket = new SockJS('http://localhost:8080/ws');
    stompClient.current = Stomp.over(socket);

    stompClient.current.connect(
        {
          login: 'admin',
          passcode: 'admin',
        },
        (frame) => {
          console.log('Connected: ' + frame);

          // 서버가 전송하는 경로에 대한 구독 설정
          stompClient.current.subscribe(`/topic/chat.message/${roomId}`, (message) => {
            const newMsg = JSON.parse(message.body);
            setMessages((prevMessages) => [...prevMessages, newMsg]);
          });
        },
        (error) => {
          console.error('Connection error: ', error);
        }
    );

    // 컴포넌트 언마운트 시 연결 해제
    return () => {
      disconnectWebSocket();
    };
  }, [roomId]);

  // 메시지 로드 시 스크롤을 맨 아래로 이동
  useEffect(() => {
    if (messageListRef.current) {
      messageListRef.current.scrollTop = messageListRef.current.scrollHeight;
    }
  }, [messages]); // messages가 변경될 때마다 스크롤 위치 업데이트

  // 메시지 전송 함수
  const handleSendMessage = () => {
    if (newMessage.trim() === '') return;

    const messageObject = {
      message: newMessage,
      fromUserId: 1, // 실제 사용자 ID로 대체해야 함
      roomId: roomId,
      time: new Date().toISOString(), // ISO 8601 형식으로 전송
    };

    // 메시지 전송
    stompClient.current.send(`/app/chat.message/${roomId}`, {}, JSON.stringify(messageObject));
    setNewMessage(''); // 입력 필드 초기화
  };

  // 엔터 키로 메시지 전송 함수
  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleSendMessage(); // 엔터 키가 눌리면 메시지 전송
      event.preventDefault(); // 기본 엔터 키 동작 방지 (줄바꿈 등)
    }
  };

  // WebSocket 연결 해제 함수
  const disconnectWebSocket = () => {
    if (stompClient.current && stompClient.current.connected) {
      stompClient.current.disconnect(() => {
        console.log('Disconnected from WebSocket');
        stompClient.current = null; // 연결 종료 후 참조 해제
      });
    }
  };

  // 채팅방 나가기 및 페이지 새로고침 함수
  const handleLeaveAndRefresh = async () => {
    try {
      // DELETE 요청을 통해 채팅방 나가기
      await axiosInstance.delete(`/chatrooms/${roomId}`);
      console.log('Successfully left the chat room.');
    } catch (error) {
      console.error('Failed to leave the chat room:', error);
    } finally {
      disconnectWebSocket(); // WebSocket 연결 해제
      window.location.reload(); // 페이지 새로고침
    }
  };

  return (
      <ChatWindowContainer>
        <Header>
          <h2>채팅방 {roomId}</h2>
          <CloseButton onClick={handleLeaveAndRefresh}>채팅방 나가기</CloseButton> {/* 나가기 버튼 */}
        </Header>
        <MessageList ref={messageListRef}> {/* ref 추가 */}
          {messages.map((msg, index) => (
              <MessageItem key={index}>
                <strong>{msg.fromUserId}:</strong> {msg.message}
              </MessageItem>
          ))}
        </MessageList>
        <MessageInputContainer>
          <MessageInput
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="메시지를 입력하세요..."
              onKeyPress={handleKeyPress} // 엔터 키 입력 감지
          />
          <SendButton onClick={handleSendMessage}>보내기</SendButton>
        </MessageInputContainer>
      </ChatWindowContainer>
  );
};

export default ChatWindow;

// 스타일링 정의
const ChatWindowContainer = styled.div`
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
`;

const CloseButton = styled.button`
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #c82333;
  }
`;

const MessageList = styled.ul`
  list-style-type: none;
  padding: 0;
  margin-bottom: 20px;
  max-height: 400px;
  overflow-y: auto;
`;

const MessageItem = styled.li`
  padding: 10px;
  margin-bottom: 10px;
  background-color: #f1f1f1;
  border-radius: 5px;
`;

const MessageInputContainer = styled.div`
  display: flex;
`;

const MessageInput = styled.input`
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
`;

const SendButton = styled.button`
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #0056b3;
  }
`;
