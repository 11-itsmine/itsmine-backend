import React, {useEffect, useState} from 'react';
import axiosInstance from '../api/axiosInstance';
import styled from 'styled-components';
import Carousel from "../components/carousel/Carousel";
import ItemList from "../components/item/ItemList";
import Board from '../components/chat/Board'; // Board 컴포넌트 임포트

function Main() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    const fetchItems = async () => {
      try {
        const response = await axiosInstance.get('/v1/items');
        setItems(response.data.data || []);
      } catch (error) {
        console.error('Failed to fetch items:', error);
      }
    };

    fetchItems();
  }, []);

  return (
      <MainWrapper>
        <Carousel/>
        <ItemList items={items}/>
        <Board currentUserId={12345}/> {/* Board 컴포넌트 추가 */}
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
