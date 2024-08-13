import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance'; // Ensure this path is correct
import styled from 'styled-components';

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
  const [refundPolicyAccepted, setRefundPolicyAccepted] = useState(false); // 환불 정책 동의 상태
  const [privacyPolicyAccepted, setPrivacyPolicyAccepted] = useState(false); // 개인정보 수집 및 이용 동의 상태

  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (!refundPolicyAccepted || !privacyPolicyAccepted) {
      setErrorMessage('모든 약관에 동의해야 합니다.');
      return;
    }

    try {
      await axiosInstance.post('/v1/users', signupRequest);

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
    const { name, value } = e.target;
    setSignupRequest({ ...signupRequest, [name]: value });
  };

  const handleRefundPolicyChange = (e) => {
    setRefundPolicyAccepted(e.target.checked);
  };

  const handlePrivacyPolicyChange = (e) => {
    setPrivacyPolicyAccepted(e.target.checked);
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

          <TermsContainer>
            <TermsTitle>이용약관(필수)</TermsTitle>
            <TermsContent>
              <h3>제 1 조 환불</h3>
              <p>
                본 약관은 주식회사 [ITSMINE]이(가) 제공하는 서비스와 관련하여, 회사와 회원 간의 권리, 의무, 책임사항 및 절차를 규정하는 것을 목적으로 합니다.
              </p>

              <h4 style={{ marginTop: '20px' }}>1. 상품 등록 후 수정 및 삭제</h4> {/* 여기서 margin-top으로 간격 추가 */}
              <p>
                회원이 상품을 등록한 이후에는 해당 상품의 수정이나 삭제가 불가하므로, 등록 전에 모든 내용을 신중히 검토하여야 합니다. 이를 통해 발생하는 모든 책임은 회원 본인에게 있습니다.
              </p>

              <h4 style={{ marginTop: '20px' }}>2. 낙찰 및 환불 정책</h4> {/* 여기서도 margin-top으로 간격 추가 */}
              <p>
                다른 사용자가 회원이 등록한 상품에 대해 낙찰을 받은 경우, 해당 상품에 입찰했던 다른 회원들은 낙찰 결과에 따라 자동으로 환불 처리됩니다. 그러나 회원 본인이 낙찰자로 선정된 경우, 어떠한 사유로도 해당 낙찰 건에 대한 환불은 불가능함을 명시합니다. 따라서 입찰 시에는 신중한 결정이 요구됩니다.
              </p>
            </TermsContent>
            <CheckboxContainer>
              <input
                  type="checkbox"
                  checked={refundPolicyAccepted}
                  onChange={handleRefundPolicyChange}
              />
              <label>이용약관에 동의합니다.</label>
            </CheckboxContainer>
          </TermsContainer>

          <TermsContainer>
            <TermsTitle>개인정보 수집 및 이용</TermsTitle>
            <TermsContent>
              <p>
                회사는 회원의 개인정보를 중요시하며, 정보통신망 이용촉진 및 정보보호 등에 관한 법률을 준수하고 있습니다. 회사는 개인정보보호정책을 통해 회원이 제공하는 개인정보가 어떠한 용도와 방식으로 이용되고 있으며, 개인정보보호를 위해 어떠한 조치가 취해지고 있는지 알려드립니다.
              </p>
            </TermsContent>
            <CheckboxContainer>
              <input
                  type="checkbox"
                  checked={privacyPolicyAccepted}
                  onChange={handlePrivacyPolicyChange}
              />
              <label>개인정보 수집 및 이용에 동의합니다.</label>
            </CheckboxContainer>
          </TermsContainer>

          {errorMessage && <ErrorText>{errorMessage}</ErrorText>}
          <SignUpBtn type="submit">Sign Up</SignUpBtn>
        </Form>
      </Container>
  );
};

// 스타일 컴포넌트는 이전 코드와 동일
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

const TermsContainer = styled.div`
  margin-bottom: ${props => props.theme.margins.xl};
`;

const TermsTitle = styled.h2`
  font-size: ${props => props.theme.fontSizes.base};
  margin-bottom: ${props => props.theme.margins.small};
`;

const TermsContent = styled.div`
  background-color: #f9f9f9;
  padding: ${props => props.theme.paddings.small};
  border: 1px solid #ddd;
  border-radius: 4px;
  max-height: 150px;
  overflow-y: scroll;
  margin-bottom: ${props => props.theme.margins.small};
`;

const CheckboxContainer = styled.div`
  display: flex;
  align-items: center;
`;

const ErrorText = styled.p`
  color: ${props => props.theme.palette.error.main};
  font-size: ${props => props.theme.fontSizes.small};
`;

const SignUpBtn = styled.button`
  background: #ebebeb;
  width: 100%;
  margin-top: 0.8rem;
  margin-bottom: 0.5rem;
  padding: ${props => props.theme.paddings.large};
  border: none;
  border-radius: 10px;
  font-size: ${props => props.theme.fontSizes.base};
  color: ${props => props.theme.colors.white};
  cursor: pointer;
`;

export default SignUp;