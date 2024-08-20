import React, {useEffect, useState} from 'react';
import axiosInstance from '../api/axiosInstance';
import styled from 'styled-components';
import Carousel from "../components/carousel/Carousel";
import ItemList from "../components/item/ItemList";
import Board from '../components/chat/Board';
import {useLocation} from 'react-router-dom'; // useLocation hook 추가

function Main() {
  const [items, setItems] = useState([]);
  const location = useLocation(); // 현재 위치 정보 가져오기

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

  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    const status = queryParams.get('status');

    if (status === 'success') {
      alert('결제 완료되였습니다. 메인화면으로 돌아갑니다.');
    } else if (status === 'cancel') {
      alert('결제 취소되었습니다. 메인화면으로 돌아갑니다.');
    } else if (status === 'fail') {
      alert('결제 실패하였습니다. 메인화면으로 돌아갑니다.');
    }

  }, [location]);

  return (
      <MainWrapper>
        {/*<Carousel/>*/}
        <ItemList items={items}/>
        <Board currentUserId={12345}/>
      </MainWrapper>
  );
}

export default Main;

const MainWrapper = styled.div`
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  position: relative;
  padding-bottom: 20px;
`;
