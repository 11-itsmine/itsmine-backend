import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import Carousel from "../components/carousel/Carousel";
import ItemList from "../components/item/ItemList";
import Board from "../components/chat/Board"
import axiosInstance from '../api/axiosInstance'; // Axios 인스턴스 가져오기

function Main() {
  const [items, setItems] = useState([]);
  const [chatRooms, setChatRooms] = useState([]);

  useEffect(() => {
    // Board에 필요한 데이터 가져오기
    axiosInstance.get('/chatrooms')
    .then(response => {
      setChatRooms(response.data);
    })
    .catch(error => {
      console.error('Failed to fetch chat rooms', error);
    });
  }, []);

  return (
      <MainWrapper>
        <Carousel />
        <ItemList items={items} />
        <Board chatRooms={chatRooms} />

      </MainWrapper>
  );
}

const MainWrapper = styled.div`
  margin-top: 5rem;
`;

export default Main;