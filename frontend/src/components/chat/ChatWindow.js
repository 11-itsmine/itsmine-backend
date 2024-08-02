import React, {useEffect, useState} from 'react';
import MessageInput from './MessageInput';
import SockJS from 'sockjs-client';
import {Client} from '@stomp/stompjs';
import axiosInstance from '../../api/axiosInstance'; // 인증 헤더가 포함된 axios 인스턴스
import './Chat.css';

const ChatWindow = ({roomId, onLeaveRoom}) => {
  const [messages, setMessages] = useState([]);
  const [stompClient, setStompClient] = useState(null);

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws');
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });

    client.onConnect = () => {
      console.log('Connected to WebSocket');
      client.subscribe(`/exchange/chat.exchange/room.${roomId}`, (message) => {
        if (message.body) {
          const newMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, newMessage]);
        }
      });
    };

    client.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    client.activate();
    setStompClient(client);

    return () => {
      if (client.connected) {
        client.deactivate();
      }
    };
  }, [roomId]);

  const handleSendMessage = (message) => {
    if (stompClient && stompClient.connected) {
      stompClient.publish({
        destination: `/pub/chat.message.${roomId}`,
        body: JSON.stringify({
          content: message,
          roomId,
          sender: 'You',
        }),
      });
    }
  };

  const handleLeaveRoom = async () => {
    try {
      await axiosInstance.delete(`/chatrooms/${roomId}`); // DELETE 요청으로 방 나가기
      onLeaveRoom(); // 부모 컴포넌트에 방 나갔음을 알림
    } catch (error) {
      console.error('Error leaving room:', error);
    }
  };

  return (
      <div className="chat-window">
        <h2>Chat Room {roomId}</h2>
        <button onClick={handleLeaveRoom}>Leave Room</button>
        {/* 나가기 버튼 */}
        <div className="messages">
          {messages.map((msg, index) => (
              <div key={index}>
                <strong>{msg.sender}</strong>: {msg.content}
              </div>
          ))}
        </div>
        <MessageInput onSend={handleSendMessage}/>
      </div>
  );
};

export default ChatWindow;
