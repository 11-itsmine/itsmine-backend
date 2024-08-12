import React, {useEffect, useState} from 'react';
import axiosInstance from '../../api/axiosInstance';
import ChatRoom from './ChatRoom';
import ChatWindow from './ChatWindow';
import ChatRoomListContainer from './ChatRoomListContainer';
import styled from 'styled-components';

const Board = ({currentUserId}) => {
  const [chatRooms, setChatRooms] = useState([]);
  const [isChatWindowVisible, setIsChatWindowVisible] = useState(false);
  const [selectedRoom, setSelectedRoom] = useState(null);
  const [isMinimized, setIsMinimized] = useState(false); // 최소화 상태를 관리하는 상태 추가

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        const response = await axiosInstance.get('/v1/chatrooms');
        setChatRooms(response.data.data || []);
      } catch (error) {
        console.error('Failed to fetch chat rooms:', error);
      }
    };

    fetchRooms();
  }, []);

  const openChatWindow = (room) => {
    setSelectedRoom(room);
    setIsChatWindowVisible(true);
  };

  const closeChatWindow = () => {
    setIsChatWindowVisible(false);
    setSelectedRoom(null);
  };

  const toggleMinimize = () => {
    setIsMinimized(!isMinimized); // 최소화 버튼을 누르면 상태를 토글
  };

  return (
      <BoardContainer isMinimized={isMinimized}>
        <Header>
          <MinimizeButton onClick={toggleMinimize}>
            {isMinimized ? '▲' : '▼'}
          </MinimizeButton>
        </Header>
        {!isMinimized && !isChatWindowVisible && (
            <ChatRoomListContainer>
              <h2 style={{color: '#7289da'}}>Chat Rooms</h2>
              {chatRooms.map((room) => (
                  <ChatRoom key={room.roomId} room={room}
                            onOpenChat={() => openChatWindow(room)}/>
              ))}
            </ChatRoomListContainer>
        )}

        {!isMinimized && isChatWindowVisible && selectedRoom && (
            <ChatWindowContainer>
              <ChatWindow room={selectedRoom} currentUserId={currentUserId}
                          onClose={closeChatWindow} onLeave={closeChatWindow}/>
            </ChatWindowContainer>
        )}
      </BoardContainer>
  );
};

export default Board;

const BoardContainer = styled.div`
  display: flex;
  flex-direction: column;
  position: fixed;
  bottom: ${({isMinimized}) => (isMinimized ? '-460px'
      : '0')}; /* 최소화 상태에 따라 보드 위치 조정 */
  left: 0;
  width: 300px;
  height: 500px;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 1000;
  border-radius: 10px 10px 0 0;
  overflow: hidden; /* 컨텐츠가 영역을 벗어날 경우 숨김 처리 */
  transition: bottom 0.3s ease; /* 위치가 변할 때 부드러운 애니메이션 */
`;

const Header = styled.div`
  display: flex;
  justify-content: flex-end;
  padding: 5px;
`;

const MinimizeButton = styled.button`
  background: none;
  border: none;
  color: #fff;
  cursor: pointer;
  font-size: 16px;
  padding: 0;
  margin: 0;
`;

const ChatWindowContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex; /* 추가 */
  flex-direction: column; /* 추가 */
  overflow: hidden; /* overflow 속성을 추가하여 컨텐츠가 영역을 벗어나지 않도록 설정 */
`;
