import React, { useState, useEffect } from 'react';
import styled, { keyframes } from 'styled-components';

const fadeIn = keyframes`
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
`;

const fadeOut = keyframes`
  0% {
    opacity: 1;
  }
  100% {
    opacity: 0;
  }
`;

const LoadingScreen = ({ onFinish }) => {
  const [loadingText, setLoadingText] = useState("IT'S YOURS?");

  useEffect(() => {
    const timer1 = setTimeout(() => {
      setLoadingText("NO, IT'S MINE");
    }, 1500); // 1.5초 후 텍스트 변경

    const timer2 = setTimeout(() => {
      onFinish();
    }, 3000); // 3초 후 로딩 완료

    return () => {
      clearTimeout(timer1);
      clearTimeout(timer2);
    };
  }, [onFinish]);

  return (
      <LoadingContainer>
        <LoadingLogo isFadingOut={loadingText === "NO, IT'S MINE"}>
          {loadingText}
        </LoadingLogo>
      </LoadingContainer>
  );
};

export default LoadingScreen;

const LoadingContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: white;
  z-index: 9999;
`;

const LoadingLogo = styled.h1`
  font-size: 3rem; /* 글씨 크기를 조정 */
  font-weight: bold;
  animation: ${({ isFadingOut }) => isFadingOut ? fadeOut : fadeIn} 1.5s ease-in-out;
  transition: opacity 1.5s ease-in-out; // 전환을 더 부드럽게 함
  white-space: nowrap; /* 긴 텍스트가 잘리지 않도록 설정 */
  opacity: ${({ isFadingOut }) => (isFadingOut ? 0 : 1)};
`;
