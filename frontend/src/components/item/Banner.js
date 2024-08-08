import React from 'react';
import styled from 'styled-components';


const Banner = ({text, src}) => {
  return (
      <BannerItem>
        <BannerImg src={src}/>

        <BannerText>{text}</BannerText>
      </BannerItem>
  );
};

export default Banner;

const BannerItem = styled.li`
  ${props => props.theme.flex.flexBox('column')}
`;

const BannerImg = styled.img`
  width: 5rem;
  height: 5rem;
  object-fit: contain;
`;

const BannerText = styled.p`
  font-size: ${props => props.theme.fontSizes.small};
`;
