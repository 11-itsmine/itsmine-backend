import React from 'react';
import styled from 'styled-components';

const Banner = ({ text, src, category, selectCategory }) => {
  const handleBannerClick = () => {
    if (category && selectCategory && typeof selectCategory === 'function') {
      console.log('Banner clicked:', category); // Debugging line
      selectCategory(category);
    } else {
      console.warn('Category is undefined or selectCategory is not a function');
    }
  };

  return (
      <BannerItem onClick={handleBannerClick}>
        <BannerImg src={src} />
        <BannerText>{text}</BannerText>
      </BannerItem>
  );
};

export default Banner;

const BannerItem = styled.li`
  ${props => props.theme.flex.flexBox('column')}
  cursor: pointer;
  padding: 10px;

  &:hover {
    background-color: #f0f0f0;
  }
`;

const BannerImg = styled.img`
  width: 5rem;
  height: 5rem;
  object-fit: contain;
`;

const BannerText = styled.p`
  font-size: ${props => props.theme.fontSizes.small};
`;
