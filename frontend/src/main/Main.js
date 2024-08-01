import React from 'react';
import styled from 'styled-components';

import Carousel from "../Components/carousel/Carousel";
import ItemList from "../Components/item/ItemList";

function Main() {
  return (
      <MainWrapper>
        <Carousel/>
        <ItemList/>
      </MainWrapper>
  );
}

const MainWrapper = styled.div`
  margin-top: 5rem;
`;

export default Main;
