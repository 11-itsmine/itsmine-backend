import React from 'react';
import {useNavigate} from 'react-router-dom';
import styled from 'styled-components';

const Item = ({
  productName,
  description,
  currentPrice,
  thumbnail_url,
  productId
}) => {
  const navigate = useNavigate();

  const goToDetail = () => {
    navigate(`/products/${productId}`);
  };

  return (
      <ItemComponent onClick={goToDetail}>
        <ItemImg src={thumbnail_url} alt={`${productName} image`}/>
        <ItemDetails>
          <ItemName>{productName}</ItemName>
          <ItemDescription>{description}</ItemDescription>
          <ItemPrice>{currentPrice}원</ItemPrice>
        </ItemDetails>
      </ItemComponent>
  );
};

export default Item;

const ItemComponent = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 2rem;
  cursor: pointer;
  transition: transform 0.2s;

  &:hover {
    transform: translateY(-5px);
  }
`;

const ItemImg = styled.img`
  width: 14rem;
  height: 14rem;
  object-fit: cover;
  border-radius: 10px;
  background-color: #f0f0f0;
`;

const ItemDetails = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 1rem;
  text-align: center;
`;

const ItemName = styled.div`
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
`;

const ItemDescription = styled.div`
  font-size: 1rem;
  color: gray;
  margin-bottom: 0.5rem;
`;

const ItemPrice = styled.div`
  font-size: 1.2rem;
  font-weight: bold;
  color: #ff4500;
`;
