// 클라이언트 측 ChatWindow.js
import React, { useEffect, useState, useRef } from 'react';
import styled from 'styled-components';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import axiosInstance from '../../api/axiosInstance';
import { v4 as uuidv4 } from 'uuid'; // UUID 생성 라이브러리

const ChatWindow = ({ room, onClose }) => {
  const { roomId, userDetailId, fromUserId, fromUserNickname, toUserId, toUserNickname } = room;
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const stompClient = useRef(null);
  const messageListRef = useRef(null);

  useEffect(() => {
    if (!roomId) {
      console.error('Room ID is not available');
      return;
    }

    // 초기 메시지 로드
    const fetchMessages = async () => {
      try {
        const response = await axiosInstance.get(`/v1/chatrooms/${roomId}`);
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

          // 서버에서 전송하는 메시지를 구독합니다.
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

  useEffect(() => {
    if (messageListRef.current) {
      messageListRef.current.scrollTop = messageListRef.current.scrollHeight;
    }
  }, [messages]);

  // 메시지 전송 함수
  const handleSendMessage = () => {
    if (newMessage.trim() === '') return;

    const messageObject = {
      messageId: uuidv4(), // 고유 메시지 ID 생성
      message: newMessage,
      fromUserId: userDetailId, // 현재 로그인한 사용자 ID 사용
      roomId: roomId,
      time: new Date().toISOString(),
    };

    // 메시지 전송
    stompClient.current.send(`/app/chat.message/${roomId}`, {}, JSON.stringify(messageObject));
    setNewMessage(''); // 입력 필드 초기화
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleSendMessage();
      event.preventDefault();
    }
  };

  // WebSocket 연결 해제 함수
  const disconnectWebSocket = () => {
    if (stompClient.current && stompClient.current.connected) {
      stompClient.current.disconnect(() => {
        console.log('Disconnected from WebSocket');
        stompClient.current = null;
      });
    }
  };

  // 채팅방 나가기 및 페이지 새로고침 함수
  const handleLeaveAndRefresh = async () => {
    try {
      await axiosInstance.delete(`/v1/chatrooms/${roomId}`);
      console.log('Successfully left the chat room.');
    } catch (error) {
      console.error('Failed to leave the chat room:', error);
    } finally {
      disconnectWebSocket();
      onClose();
    }
  };

  // 상대방의 닉네임을 구하기
  const otherUserNickname = fromUserId === userDetailId ? toUserNickname : fromUserNickname;

  return (
      <ChatWindowContainer>
        <Header>
          <h2>{otherUserNickname}와의 채팅</h2> {/* 상대방의 닉네임으로 표시 */}
          <CloseButton onClick={handleLeaveAndRefresh}>채팅방 나가기</CloseButton>
        </Header>
        <MessageList ref={messageListRef}>
          {messages.map((msg, index) => (
              <MessageItem key={index} isOwnMessage={msg.fromUserId === userDetailId}>
                <strong>
                  {msg.fromUserId === userDetailId
                      ? '나'
                      : msg.fromUserId === fromUserId
                          ? fromUserNickname // 발신자가 상대방일 경우
                          : toUserNickname}
                  :
                </strong>{' '}
                {msg.message}
              </MessageItem>
          ))}
        </MessageList>
        <MessageInputContainer>
          <MessageInput
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder="메시지를 입력하세요..."
              onKeyPress={handleKeyPress}
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
  background-color: ${(props) =>
    props.isOwnMessage ? '#daf8e3' : '#f1f1f1'}; /* 발신자와 수신자의 배경색을 다르게 설정 */
  border-radius: 5px;
  text-align: ${(props) =>
    props.isOwnMessage ? 'right' : 'left'}; /* 발신자 메시지는 오른쪽 정렬 */
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
