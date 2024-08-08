import React from 'react';
import styled from 'styled-components';
import Slider from 'react-slick';
import {FaCaretLeft, FaCaretRight} from 'react-icons/fa';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

const SlickArrowLeft = styled(FaCaretLeft)`
  color: black; // Change color to black
`;

const SlickArrowRight = styled(FaCaretRight)`
  color: black; // Change color to black
`;

const settings = {
  dots: true,
  infinite: true,
  speed: 1000,
  slidesToShow: 1,
  slidesToScroll: 1,
  autoplay: true,
  autoplaySpeed: 5000,
  prevArrow: <SlickArrowLeft/>,
  nextArrow: <SlickArrowRight/>,
};

const Carousel = () => {
  return (
      <StyledSlider {...settings}>
        {thumbnail.map(item => {
          return (
              <ImageContainer key={item.id}>
                <Img src={item.url} $backgroundColor={item.background}/>
              </ImageContainer>
          );
        })}
      </StyledSlider>
  );
};

export default Carousel;

const StyledSlider = styled(Slider)`
  .slick-list {
    width: 100%;
    height: 100%;
    margin: 0 auto;
    overflow-x: hidden;
  }

  .slick-slide div {
    outline: none;
    cursor: pointer;
  }

  .slick-dots {
    bottom: 1.25rem;
    margin-top: 12.5rem;
  }

  .slick-prev {
    z-index: 1;
    left: 3.125rem;
    width: 2.5rem;
    height: 5rem;
    opacity: 0.5;
  }

  .slick-next {
    right: 3.125rem;
    width: 2.5rem;
    height: 5rem;
    opacity: 0.5;
  }
`;

const Img = styled.img`
  max-width: 100%;
  max-height: 100%;
  width: 100%;
  height: 30rem;
  object-fit: contain;
  background: ${props => props.$backgroundColor};
`;

const ImageContainer = styled.div`
  margin: 0;
`;

const thumbnail = [
  {id: 1, url: '/images/banner/hanhwa.png', background: 'rgb(249, 249, 249)'},
  {id: 2, url: '/images/ours/성찬.png', background: 'rgb(249, 249, 249)'},
  {
    id: 3,
    url: '/images/ours/장현.png',
    background: 'rgb(249, 249, 249)'
  },
  {id: 4, url: '/images/ours/순모.png', background: 'rgb(249, 249, 249)'},
  {id: 5, url: '/images/ours/동우님.png', background: 'rgb(249, 249, 249)'},
  {id: 6, url: '/images/ours/정빈.png', background: 'rgb(249, 249, 249)'},
  {id: 7, url: '/images/banner/nbcamp.png', background: 'rgb(249, 249, 249)'}
];
