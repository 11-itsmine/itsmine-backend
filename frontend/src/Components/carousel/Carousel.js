import React from 'react';
import styled from 'styled-components';
import Slider from 'react-slick';
import {FaCaretLeft, FaCaretRight} from 'react-icons/fa';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

const SlickArrowLeft = ({currentSlide, slideCount, ...props}) => (
    <FaCaretLeft {...props} />
);
const SlickArrowRight = ({currentSlide, slideCount, ...props}) => (
    <FaCaretRight {...props} />
);

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
                <Img src={item.url} backgroundColor={item.background}/>
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
    color: ${props => props.theme.colors.red};
    opacity: 0.5;
  }

  .slick-next {
    right: 3.125rem;
    width: 2.5rem;
    height: 5rem;
    color: ${props => props.theme.colors.red};
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
  {id: 1, url: '/images/banner/appliance.png', background: 'rgb(94, 0, 215)'},
  {id: 2, url: '/images/banner/babyAndKids.png', background: 'rgb(0, 198, 91)'},
  {
    id: 3,
    url: '/images/banner/cookingPot.png',
    background: 'rgb(249, 249, 249)'
  },
  {id: 4, url: '/images/banner/couch.png', background: 'rgb(12, 129, 237)'},
  {id: 5, url: '/images/banner/makeup.png', background: 'rgb(243, 241, 236)'},
  {id: 6, url: '/images/banner/pc.png', background: 'rgb(255, 238, 244)'},
];
