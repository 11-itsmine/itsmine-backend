import React, {useEffect, useState} from 'react';
import axiosInstance from '../../api/axiosInstance';
import ChatRoom from './ChatRoom';
import ChatWindow from './ChatWindow';
import styled from 'styled-components';

const Board = ({currentUserId}) => {
  const [chatRooms, setChatRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isChatWindowVisible, setIsChatWindowVisible] = useState(false);
  const [selectedRoom, setSelectedRoom] = useState(null);

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        setLoading(true);
        setError(null);

        const response = await axiosInstance.get('/v1/chatrooms');
        const {data} = response;

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

    fetchRooms();
  }, []);

  const openChatWindow = (room) => {
    setSelectedRoom(room);
    setIsChatWindowVisible(true);
  };

  const closeChatWindow = () => {
    setIsChatWindowVisible(false);
    setSelectedRoom(null); // 채팅방 선택 해제
  };

  const renderRooms = () => {
    return chatRooms.map((room) => (
        <ChatRoom
            key={room.roomId}
            room={room}
            onOpenChat={() => openChatWindow(room)}
        />
    ));
  };

  return (
      <BoardContainer>
        <Sidebar>
          <h2>Chat Rooms</h2>
          {loading ? (
              <LoadingMessage>로딩 중...</LoadingMessage>
          ) : error ? (
              <ErrorMessage>{error}</ErrorMessage>
          ) : chatRooms.length === 0 ? (
              <NoRoomsMessage>참여 중인 채팅방이 없습니다.</NoRoomsMessage>
          ) : (
              <ChatRoomList>{renderRooms()}</ChatRoomList>
          )}
        </Sidebar>
        {isChatWindowVisible && selectedRoom && (
            <ChatWindowContainer>
              <ChatWindow
                  room={selectedRoom}
                  currentUserId={currentUserId}
                  onClose={closeChatWindow}
                  onLeave={closeChatWindow}
              />
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
  top: 85px; /* 상단에서 85px 아래에 위치 */
  right: 0;
  height: 850px; /* 높이를 850px로 설정 */
  width: 850px; /* 너비를 850px로 설정 */
  background-color: #2f3136;
  z-index: 1000;
`;

const Sidebar = styled.div`
  width: 400px; /* Sidebar 너비 설정 */
  background-color: #202225;
  color: #ffffff;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  overflow-y: auto;

  h2 {
    font-size: 1.5rem;
    margin-bottom: 20px;
    color: #7289da;
  }
`;

const ChatWindowContainer = styled.div`
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  background-color: #36393f;
  color: #ffffff;
  border-left: 1px solid #555;
  overflow-y: auto;
  width: 400px;
  height: 100%; /* 부모 요소에 맞춰 높이 설정 */
`;

const LoadingMessage = styled.p`
  color: #b9bbbe;
`;

const ErrorMessage = styled.p`
  color: #f04747;
`;

const NoRoomsMessage = styled.p`
  color: #b9bbbe;
`;

const ChatRoomList = styled.ul`
  list-style-type: none;
  padding: 0;
  width: 100%;
  overflow-y: auto;
`;
