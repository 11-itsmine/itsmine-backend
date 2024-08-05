import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // Ensure this path is correct
import styled from 'styled-components';
import Link from '@mui/material/Link';

const SignUp = () => {
  const [signupRequest, setSignupRequest] = useState({
    name: '',
    username: '',
    email: '',
    password: '',
    address: '',
    nickname: '',
  });

  const [errorMessage, setErrorMessage] = useState('');

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    try {
      await axiosInstance.post('/users', signupRequest);

      console.log('Signup successful!');
      navigate('/itsmine/login');
    } catch (error) {
      console.error('Signup failed:', error);
      if (error.response && error.response.data) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage('회원가입에 실패했습니다. 다시 시도해주세요.');
      }
    }
  };

  const handleChange = (e) => {
    const {name, value} = e.target;
    setSignupRequest({...signupRequest, [name]: value});
  };

  return (
      <Container>
        <Logo>ItsMine</Logo>
        <Form onSubmit={handleSubmit}>
          <SignUpForm>
            <Label htmlFor="name">Name</Label>
            <Input
                id="name"
                name="name"
                placeholder="Enter your name"
                value={signupRequest.name}
                onChange={handleChange}
                autoComplete="name"
                autoFocus
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="username">Username</Label>
            <Input
                id="username"
                name="username"
                placeholder="Enter your username"
                value={signupRequest.username}
                onChange={handleChange}
                autoComplete="username"
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="email">Email Address</Label>
            <Input
                id="email"
                name="email"
                placeholder="Enter your email"
                value={signupRequest.email}
                onChange={handleChange}
                autoComplete="email"
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="password">Password</Label>
            <Input
                id="password"
                name="password"
                type="password"
                placeholder="Enter your password"
                value={signupRequest.password}
                onChange={handleChange}
                autoComplete="new-password"
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="address">Address</Label>
            <Input
                id="address"
                name="address"
                placeholder="Enter your address"
                value={signupRequest.address}
                onChange={handleChange}
                autoComplete="address"
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="nickname">Nickname</Label>
            <Input
                id="nickname"
                name="nickname"
                placeholder="Enter your nickname"
                value={signupRequest.nickname}
                onChange={handleChange}
                autoComplete="nickname"
            />
          </SignUpForm>
          <FormControlLabel>
            <Checkbox
                type="checkbox"
                value="allowExtraEmails"
                color="primary"
            />
            I want to receive inspiration, marketing promotions and updates via
            email.
          </FormControlLabel>
          {errorMessage && <ErrorText>{errorMessage}</ErrorText>}
          <SignUpBtn type="submit">Sign Up</SignUpBtn>
          <GridContainer>
            <GridItem>
              <Link href="/itsmine/login">Already have an account? Sign
                in</Link>
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

const SignUpForm = styled.div`
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

const SignUpBtn = styled.button`
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

export default SignUp;
