import React from 'react';
import styled from 'styled-components';

const ItemNotFound = () => {
  return (

    <Wrapper>
      <MainText>검색하신 결과가 없습니다.</MainText>
      <SubText>
        상품 등록 요청은 <SubSpan>앱 1:1 문의하기</SubSpan>로 요청해주세요.
      </SubText>
    </Wrapper>
  );
};

export default ItemNotFound;

const Wrapper = styled.div`
  ${props => props.theme.flex.flexBox('column')};
  margin-top: ${props => props.theme.margins.xxxl};
`;

const MainText = styled.p`
  font-size: ${props => props.theme.fontSizes.xl};
  margin-bottom: ${props => props.theme.margins.large};
`;

const SubText = styled.p`
  color: ${props => props.theme.colors.gray};
`;

const SubSpan = styled.span`
  font-weight: ${props => props.theme.fontWeights.bold};

`;