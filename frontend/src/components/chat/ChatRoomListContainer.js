// ChatRoomListContainer.js
import styled from 'styled-components';

const ChatRoomListContainer = styled.div`
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  background-color: #2f3136;
  padding: 10px 10px 50px;
  border-radius: 10px;
  overflow-y: auto; /* 스크롤 가능하게 설정 */
  height: 100%; /* 높이를 부모 요소에 맞게 설정 */
  box-sizing: border-box; /* 패딩이 전체 높이에 포함되도록 설정 */
`;

export default ChatRoomListContainer;