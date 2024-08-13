import React, {useEffect, useRef, useState} from 'react';
import styled from 'styled-components';
import {Stomp} from '@stomp/stompjs';
import axiosInstance from '../../api/axiosInstance';
import {v4 as uuidv4} from 'uuid';
import ReportForm from './ReportForm';

const ChatWindow = ({room, onClose, onLeave}) => {
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [isReportFormVisible, setIsReportFormVisible] = useState(false);
  const [isBlocked, setIsBlocked] = useState(false); // 상대방이 차단했는지 여부
  const [isUserBlocked, setIsUserBlocked] = useState(false); // 본인이 차단당했는지 여부
  const stompClient = useRef(null);
  const messageListRef = useRef(null);

  useEffect(() => {
    const checkUserStatus = async () => {
      try {
        // 상대방 상태 확인
        const toUserResponse = await axiosInstance.get(
            `/v1/users/${room.toUserId}/status`);
        if (toUserResponse.data.status === 'BLOCKED') {
          alert('차단당한 유저입니다.');
          onClose(); // 팝업을 띄운 후 채팅방을 닫음
          return;
        }

        // 본인 상태 확인
        const fromUserResponse = await axiosInstance.get(
            `/v1/users/${room.userDetailId}/status`);
        if (fromUserResponse.data.status === 'BLOCKED') {
          setIsUserBlocked(true); // 본인이 차단당했으면 입력 필드를 비활성화
        }

      } catch (error) {
        console.error('Failed to check user status:', error);
      }
    };

    checkUserStatus();

    if (!room?.roomId) {
      return;
    }

    const fetchMessages = async () => {
      try {
        const response = await axiosInstance.get(
            `/v1/chatrooms/${room.roomId}`);
        setMessages(response.data.data || []);
      } catch (error) {
        console.error('Failed to fetch messages:', error);
      }
    };
    fetchMessages();

    const socket = new WebSocket('wss://itsyours.store/ws');
    stompClient.current = Stomp.over(socket);
    stompClient.current.connect(
        {},
        (frame) => {
          console.log('Connected: ' + frame);
          stompClient.current.subscribe(`/topic/chat.message/${room.roomId}`,
              (message) => {
                const newMsg = JSON.parse(message.body);
                setMessages((prevMessages) => [...prevMessages, newMsg]);
              });
        },
        (error) => {
          console.error('Connection error: ', error);
        }
    );

    return () => {
      if (stompClient.current) {
        stompClient.current.disconnect(() => {
          console.log('Disconnected from WebSocket');
          stompClient.current = null;
        });
      }
    };
  }, [room?.roomId]);

  useEffect(() => {
    if (messageListRef.current) {
      messageListRef.current.scrollTop = messageListRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSendMessage = () => {
    if (newMessage.trim() === '' || !room?.roomId || isUserBlocked) {
      return;
    }
    const messageObject = {
      messageId: uuidv4(),
      message: newMessage,
      fromUserId: room.userDetailId,
      roomId: room.roomId,
      time: new Date().toISOString(),
    };
    stompClient.current.send(`/app/chat.message/${room.roomId}`, {},
        JSON.stringify(messageObject));
    setNewMessage('');
  };

  const handleKeyPress = (event) => {
    if (event.key === 'Enter') {
      handleSendMessage();
      event.preventDefault();
    }
  };

  const handleLeaveRoom = async () => {
    if (stompClient.current && stompClient.current.connected) {
      stompClient.current.disconnect(() => {
        console.log('Disconnected from WebSocket');
        stompClient.current = null;
      });
    }
    try {
      await axiosInstance.delete(`/v1/chatrooms/${room.roomId}`);
      onLeave(); // 성공 시 onLeave 콜백 호출
    } catch (error) {
      console.error('Failed to leave the chat room:', error);
    } finally {
      // 페이지 리프레시
      window.location.reload();
    }
  };

  const handleBackToList = () => {
    onClose();
  };

  const handleReportClick = () => {
    setIsReportFormVisible(true);
  };

  const handleCloseReportForm = () => {
    setIsReportFormVisible(false);
  };

  const formatTime = (isoString) => {
    const date = new Date(isoString);
    return date.toLocaleTimeString('ko-KR',
        {hour: '2-digit', minute: '2-digit'});
  };

  const otherUserNickname = room.fromUserId === room.userDetailId
      ? room.toUserNickname
      : room.fromUserNickname;
  const toUserId = room.fromUserId === room.userDetailId ? room.toUserId
      : room.fromUserId;

  return (
      <ChatWindowContainer>
        <Header>
          <ChatTitle>{otherUserNickname}</ChatTitle>
          <ButtonContainer>
            <BackButton onClick={handleBackToList}>Back</BackButton>
            <ReportButton onClick={handleReportClick}>Report</ReportButton>
            <LeaveButton onClick={handleLeaveRoom}>Exit</LeaveButton>
          </ButtonContainer>
        </Header>
        <MessageList ref={messageListRef}>
          {messages.map((msg, index) => (
              <MessageItem key={index}
                           isOwnMessage={msg.fromUserId === room.userDetailId}>
                <MessageBubble
                    isOwnMessage={msg.fromUserId === room.userDetailId}>
                  <MessageText>{msg.message}</MessageText>
                  <MessageTime>{formatTime(msg.time)}</MessageTime>
                </MessageBubble>
              </MessageItem>
          ))}
        </MessageList>
        <MessageInputContainer>
          <MessageInput
              type="text"
              value={newMessage}
              onChange={(e) => setNewMessage(e.target.value)}
              placeholder={isUserBlocked ? '차단되어 메시지를 보낼 수 없습니다'
                  : '메시지를 입력하세요...'}
              onKeyPress={handleKeyPress}
              disabled={isUserBlocked} // 본인이 차단당했을 경우 입력 비활성화
          />
          <SendButton onClick={handleSendMessage} disabled={isUserBlocked}>
            보내기
          </SendButton>
        </MessageInputContainer>
        {isReportFormVisible && (
            <ReportForm
                onClose={handleCloseReportForm}
                toUserId={toUserId} // 신고 대상 사용자의 ID 전달
            />
        )}
      </ChatWindowContainer>
  );
};

export default ChatWindow;

// 스타일 컴포넌트 정의는 이전과 동일

// 스타일 컴포넌트 정의
const ChatWindowContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #2f3136;
  color: #ffffff;
  border-radius: 10px;
  width: 100%;
  height: 100%;
  position: relative;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #2f3136;
  border-bottom: 1px solid #555;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
`;

const ChatTitle = styled.h2`
  font-size: 1rem;
  margin: 0;
  color: #fff;
`;

const ButtonContainer = styled.div`
  display: flex;
  gap: 8px;
`;

const BackButton = styled.button`
  padding: 6px 10px;
  background-color: #7289da;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 0.85rem;

  &:hover {
    background-color: #5b6eae;
  }
`;

const LeaveButton = styled.button`
  padding: 6px 10px;
  background-color: #ff4d4f;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 0.85rem;

  &:hover {
    background-color: #ff7875;
  }
`;

const ReportButton = styled.button`
  padding: 6px 10px;
  background-color: #ff9800;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 0.85rem;

  &:hover {
    background-color: #f57c00;
  }
`;

const MessageList = styled.ul`
  flex: 1;
  list-style-type: none;
  padding: 10px;
  margin: 0;
  overflow-y: auto;
  background-color: #2f3136;
`;

const MessageItem = styled.li`
  display: flex;
  justify-content: ${(props) => (props.isOwnMessage ? 'flex-end'
      : 'flex-start')};
  margin-bottom: 10px;
`;

const MessageBubble = styled.div`
  max-width: 60%;
  padding: 10px 15px;
  background-color: ${(props) => (props.isOwnMessage ? '#FFEB3B' : '#03A9F4')};
  border-radius: 15px;
  position: relative;
  color: #000;
  text-align: left;

  &::after {
    content: '';
    position: absolute;
    top: 0;
    ${(props) => (props.isOwnMessage ? 'right' : 'left')}: -10px;
    width: 0;
    height: 0;
    border: 10px solid transparent;
    border-top-color: ${(props) => (props.isOwnMessage ? '#FFEB3B'
        : '#03A9F4')};
    border-bottom: 0;
    margin-top: 10px;
  }
`;

const MessageText = styled.div`
  white-space: pre-wrap;
`;

const MessageTime = styled.div`
  margin-top: 5px;
  font-size: 0.75rem;
  color: #888888;
  text-align: right;
`;

const MessageInputContainer = styled.div`
  display: flex;
  padding: 10px;
  background-color: #23272a;
  border-top: 1px solid #444;
`;

const MessageInput = styled.input`
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 4px;
  margin-right: 10px;
  background-color: #40444b;
  color: #fff;
`;

const SendButton = styled.button`
  padding: 10px 20px;
  background-color: #7289da;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #5b6eae;
  }
`;
