import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled, { css } from 'styled-components';

const Nav = ({ userRole }) => {
  const [isLogin, setIsLogin] = useState(false);
  const itsmineToken = localStorage.getItem('Authorization');
  const navigate = useNavigate();

  useEffect(() => {
    if (itsmineToken) {
      setIsLogin(true);
    }
  }, [itsmineToken]);

  const logout = () => {
    if (isLogin) {
      localStorage.removeItem('Authorization');
      alert('로그아웃 되었습니다');
      setIsLogin(false);
      navigate('/itsmine/login'); // 로그아웃 후 로그인 페이지로 이동
    }
  };

  const goToMain = () => {
    if (isLogin) {
      navigate(`/itsmine`);
    }
  };

  const handleNavigation = (path) => {
    if (isLogin) {
      navigate(path);
    } else {
      alert('로그인이 필요합니다.');
      navigate('/itsmine/login');
    }
  };

  return (
      <Container>
        <Logo onClick={goToMain}>ITSMINE</Logo>
        <ButtonContainer>
          <Button login={isLogin} onClick={() => handleNavigation('/products')}>SELL</Button>
          <Button login={isLogin} onClick={() => handleNavigation('/about')}>ABOUT</Button>
          {isLogin ? (
              <>
                <Button login={isLogin} onClick={() => handleNavigation('/profile')}>MYPAGE</Button>
                <Button logout onClick={logout}>LOGOUT</Button>
                {userRole === 'MANAGER' && (
                    <Button login={isLogin} onClick={() => handleNavigation('/admin')}>ADMIN</Button>
                )}
              </>
          ) : (
              <Button login={isLogin} onClick={() => navigate('/itsmine/login')}>LOGIN</Button>
          )}
        </ButtonContainer>
      </Container>
  );
};

const Container = styled.div`
  ${props => props.theme.flex.flexBox('_', 'center', 'space-between')}
  position: fixed;
  top: 0;
  width: 100%;
  height: 4.5rem; /* 컨테이너 높이 유지 */
  padding: 0 ${props => props.theme.paddings.xxxl};
  background-color: white;
  z-index: 2000;
`;

const Logo = styled.h1`
  color: black;
  font-size: ${props => props.theme.fontSizes.titleSize}; /* 로고의 크기를 더 키움 */
  cursor: pointer;
  font-weight: bold; /* 로고를 두껍게 유지 */
`;

const ButtonContainer = styled.div``;

const Button = styled.button`
  margin: 0 ${props => props.theme.margins.base};
  border: 0;
  font-size: ${props => props.theme.fontSizes.lg}; /* 버튼 글씨 크기를 유지 */
  background: none;
  cursor: pointer;
  color: ${({ login, theme }) => (login ? theme.colors.green : 'black')};
  font-weight: ${({ login }) => (login ? 'bold' : 'normal')}; /* 로그인 시 글꼴 두께를 bold로 설정 */

  &:hover {
    transform: scale(1.05);
    transition: transform 0.2s;
  }

  &:active {
    transform: scale(0.95);
    transition: transform 0.2s;
  }

  ${({ logout, theme }) =>
      logout &&
      css`
        color: ${theme.colors.gray};
        font-weight: bold; /* 로그아웃 버튼도 두껍게 */
      `}
`;

export default Nav;
