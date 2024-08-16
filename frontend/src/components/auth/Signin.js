import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // 경로 수정
import styled from 'styled-components';
import Link from '@mui/material/Link';
import { KAKAO_AUTH_URL } from "./LoginData";
import kakaoLoginImage from "../auth/kakao_login_medium_wide.png";

const SignIn = ({ onLogin }) => {
  const [loginRequest, setLoginRequest] = useState({
    username: '',
    password: ''
  });

  const [errorMessage, setErrorMessage] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await axiosInstance.post('/v1/users/login', loginRequest);

      // 응답 바디에서 토큰 추출
      const token = response.data.data;
      console.log('Login successful!', token);

      // 토큰을 localStorage에 저장
      localStorage.setItem('Authorization', token);
      console.log('Token stored in localStorage:', localStorage.getItem('Authorization'));

      // 부모 컴포넌트에 로그인 상태 변경 알리기
      onLogin();

      // 페이지를 리다이렉트하거나 상태를 업데이트할 수 있습니다.
      navigate('/itsmine');
    } catch (error) {
      // 로그인 실패 시 처리 로직
      console.error('Login failed:', error);
      if (error.response && error.response.data) {
        alert('비밀번호를 다시 확인해주세요.'); // alert 창으로 변경
      } else {
        alert('Failed to Sign-in. Try this again later.'); // 예외 처리
      }
    }
  };

  const handleKakaoButtonClick = () => {
    window.location.href = KAKAO_AUTH_URL;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLoginRequest({ ...loginRequest, [name]: value });
  };

  return (
      <Container>
        <Logo>ItsMine</Logo>
        <Form onSubmit={handleSubmit}>
          <LoginForm>
            <Label htmlFor="username">아이디</Label>
            <Input
                id="username"
                name="username"
                placeholder="아이디를 입력해주세요"
                value={loginRequest.username}
                onChange={handleChange}
                autoComplete="username"
                autoFocus
            />
          </LoginForm>
          <LoginForm>
            <Label htmlFor="password">비밀번호</Label>
            <Input
                id="password"
                name="password"
                type="password"
                placeholder="비밀번호를 입력해주세요"
                value={loginRequest.password}
                onChange={handleChange}
                autoComplete="current-password"
            />
          </LoginForm>
          <FormControlLabel>
            <Checkbox
                type="checkbox"
                value="remember"
                color="primary"
            />
            로그인 상태 유지
          </FormControlLabel>

          <LoginBtn type="submit">로그인</LoginBtn>
          <KakaoBtn onClick={handleKakaoButtonClick}>
            <KakaoLoginImage src={kakaoLoginImage} alt="Login with Kakao" />
          </KakaoBtn>

          <GridContainer>
            <GridItem>
              <Link href="/itsmine/find-password">비밀번호 찾기</Link>
            </GridItem>
            <GridItem>
              <Link href="/signup">회원가입 하기</Link>
            </GridItem>
          </GridContainer>
        </Form>
      </Container>
  );
};

const Container = styled.section`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 9.375rem auto auto;
  max-width: 25rem;
`;

const Logo = styled.h1`
  margin-bottom: 2.8rem;
  font-size: ${props => props.theme.fontSizes.titleSize};
  font-weight: bold;
`;

const Form = styled.form`
  width: 100%;
`;

const LoginForm = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: ${props => props.theme.margins.xxl};
  width: 100%;
  height: 4.375rem;
`;

const Label = styled.label`
  font-size: ${props => props.theme.fontSizes.small};
  padding-bottom: ${props => props.theme.paddings.base};
`;

const Input = styled.input`
  border: none;
  border-bottom: 1px solid #ebebeb;
  padding: ${props => props.theme.paddings.base} 0;
  font-size: ${props => props.theme.fontSizes.small};

  &:focus {
    outline: none;
    border-bottom: 2px solid black;

    ::placeholder {
      opacity: 0;
    }
  }

  ::placeholder {
    color: #bbb;
    opacity: 1;
  }
`;

const FormControlLabel = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: ${props => props.theme.margins.large};
`;

const Checkbox = styled.input`
  margin-right: ${props => props.theme.margins.base};
`;

const LoginBtn = styled.button`
  background: #333333; /* 더 진한 색상으로 설정 */
  width: 100%;
  margin-top: 0.8rem;
  margin-bottom: 0.5rem;
  padding: ${props => props.theme.paddings.large};
  border: none;
  border-radius: 10px;
  font-size: ${props => props.theme.fontSizes.base};
  color: ${props => props.theme.colors.white};
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s; /* 클릭 시 변화를 위해 트랜지션 추가 */

  &:hover {
    background-color: #555555; /* 호버 시 약간 더 밝은 색으로 변경 */
  }

  &:active {
    background-color: #111111; /* 클릭 시 더 진한 색상으로 변경 */
    transform: scale(0.98); /* 클릭 시 약간 눌리는 효과 */
  }
`;

const KakaoBtn = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 3.375rem; // Login button height
  margin-bottom: 1rem;
  border: ${props => props.theme.borders.lightGray};
  background: #ffe501; /* 기본 카카오 버튼 색상 */
  text-decoration: none;
  cursor: pointer;
  border-radius: 10px;
  transition: background-color 0.3s, transform 0.2s; /* 클릭 시 변화를 위해 트랜지션 추가 */

  &:hover {
    background-color: #ffd400; /* 호버 시 색상 변경 (조금 더 진하게) */
  }

  &:active {
    background-color: #e5b800; /* 클릭 시 더 진한 색상으로 변경 */
    transform: scale(0.98); /* 클릭 시 약간 눌리는 효과 */
  }
`;

const KakaoLoginImage = styled.img`
  height: 100%;
`;

const GridContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
`;

const GridItem = styled.div`
  margin-top: 2rem;
  font-size: ${props => props.theme.fontSizes.small};
`;

export default SignIn;