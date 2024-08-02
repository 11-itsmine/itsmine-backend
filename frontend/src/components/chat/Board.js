import React from 'react';
import Chat from '../chat/Chat';
import './Board.css'
const Board = () => {
  return (
      <div className="board">
        <h1>Welcome to the Board</h1>
        <Chat/> {/* Chat 컴포넌트를 포함하여 채팅 기능 제공 */}
        {/* 다른 기능이나 컴포넌트를 추가할 수 있습니다. */}
      </div>
  );
};

export default Board;
