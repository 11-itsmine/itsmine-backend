import React, {useEffect, useState} from 'react';
import axiosInstance from '../api/axiosInstance';
import styled from 'styled-components';
import Carousel from "../components/carousel/Carousel";
import ItemList from "../components/item/ItemList";
import ChatRoom from '../components/chat/ChatRoom';
import ChatWindow from '../components/chat/ChatWindow';

function Main() {
  const [items, setItems] = useState([]);
  const [isBoardVisible, setIsBoardVisible] = useState(false);
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

  const toggleBoard = () => {
    setIsBoardVisible(!isBoardVisible);
  };

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
      <MainWrapper>
        <Carousel/>
        <ItemList items={items}/>
        <ButtonContainer>
          <ChatButton onClick={toggleBoard}>
            {isBoardVisible ? '채팅 닫기' : '채팅 열기'}
          </ChatButton>
        </ButtonContainer>
        {isBoardVisible && (
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
                        currentUserId={12345}
                        onClose={closeChatWindow}
                        onLeave={closeChatWindow}
                    />
                  </ChatWindowContainer>
              )}
            </BoardContainer>
        )}
      </MainWrapper>
  );
}

export default Main;

const MainWrapper = styled.div`
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  padding-bottom: 20px; /* Footer와 겹치지 않도록 여백 추가 */
`;

const ButtonContainer = styled.div`
  margin: 20px;
  text-align: center;
`;

const ChatButton = styled.button`
  position: fixed;
  top: 850px;
  left: 100px;
  padding: 10px 20px;
  background-color: #c6cad0;
  color: #2a2121;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  z-index: 1100;

  &:hover {
    background-color: #b5b6be;
  }
`;

const BoardContainer = styled.div`
  position: fixed; /* fixed로 설정하여 화면에 고정 */
  top: 85px; /* 상단에서 85px 아래에 위치 */
  right: 0; /* 오른쪽에 위치 */
  display: flex;
  height: 850px;
  width: 850px;
  background-color: #2f3136;
  z-index: 1000;
`;

const Sidebar = styled.div`
  width: 400px;
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
  height: 100%;
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
