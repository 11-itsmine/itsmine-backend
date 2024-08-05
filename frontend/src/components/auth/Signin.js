import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // 경로 수정
import styled from 'styled-components';
import Link from '@mui/material/Link';

const SignIn = ({onLogin}) => {
  const [loginRequest, setLoginRequest] = useState({
    username: '',
    password: ''
  });

  const [errorMessage, setErrorMessage] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      const response = await axiosInstance.post('/users/login', loginRequest);

      // 응답 바디에서 토큰 추출
      const token = response.data.data;
      console.log('Login successful!', token);

      // 토큰을 localStorage에 저장
      localStorage.setItem('Authorization', token);
      console.log('Token stored in localStorage:',
          localStorage.getItem('Authorization'));

      // 부모 컴포넌트에 로그인 상태 변경 알리기
      onLogin();

      // 페이지를 리다이렉트하거나 상태를 업데이트할 수 있습니다.
      navigate('/itsmine');
    } catch (error) {
      // 로그인 실패 시 처리 로직
      console.error('Login failed:', error);
      if (error.response && error.response.data) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage('로그인에 실패했습니다. 다시 시도해주세요.');
      }
    }
  };

  const handleChange = (e) => {
    const {name, value} = e.target;
    setLoginRequest({...loginRequest, [name]: value});
  };

  return (
      <Container>
        <Logo>ItsMine</Logo>
        <Form onSubmit={handleSubmit}>
          <LoginForm>
            <Label htmlFor="username">Username</Label>
            <Input
                id="username"
                name="username"
                placeholder="Enter your username"
                value={loginRequest.username}
                onChange={handleChange}
                autoComplete="username"
                autoFocus
            />
          </LoginForm>
          <LoginForm>
            <Label htmlFor="password">Password</Label>
            <Input
                id="password"
                name="password"
                type="password"
                placeholder="Enter your password"
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
            Remember me
          </FormControlLabel>
          {errorMessage && <ErrorText>{errorMessage}</ErrorText>}
          <LoginBtn type="submit">Log In</LoginBtn>
          <GridContainer>
            <GridItem>
              <Link href="#">Forgot password?</Link>
            </GridItem>
            <GridItem>
              <Link href="/signup">Don't have an account? Sign Up</Link>
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

const ErrorText = styled.p`
  color: ${props => props.theme.palette.error.main};
  font-size: ${props => props.theme.fontSizes.small};
`;

const LoginBtn = styled.button`
  background: #ebebeb;
  width: 100%;
  margin: ${props => props.theme.margins.xl};
  padding: ${props => props.theme.paddings.large};
  border: none;
  border-radius: 10px;
  font-size: ${props => props.theme.fontSizes.base};
  color: ${props => props.theme.colors.white};
  cursor: pointer;
`;

const GridContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
`;

const GridItem = styled.div`
  font-size: ${props => props.theme.fontSizes.small};
`;

export default SignIn;
