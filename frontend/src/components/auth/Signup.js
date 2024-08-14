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

  const [refundPolicyAccepted, setRefundPolicyAccepted] = useState(false); // 환불 정책 동의 상태
  const [privacyPolicyAccepted, setPrivacyPolicyAccepted] = useState(false); // 개인정보 수집 및 이용 동의 상태

  const navigate = useNavigate();

  const validateSignupRequest = () => {
    const { name, username, password, nickname } = signupRequest;

    // 필수 필드 체크
    if (!name || !username || !password || !nickname) {
      return "모든 필수 항목을 입력해주세요.";
    }

    // 유효성 검사
    if (name === nickname) {
      return "닉네임과 이름은 같을 수 없습니다.";
    }

    if (!/^[가-힣]{1,4}$/.test(name)) {
      return "이름은 한글로만 최대 4글자까지 입력할 수 있습니다.";
    }

    if (!/^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{10,20}$/.test(username)) {
      return "아이디는 영문 소문자와 숫자를 포함하여 최소 10자 이상, 최대 20자 이하여야 합니다.";
    }

    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{10,}$/.test(password)) {
      return "비밀번호는 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함하여 최소 10자 이상이어야 합니다.";
    }

    return null;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const validationError = validateSignupRequest();
    if (validationError) {
      alert(validationError);
      return;
    }

    if (!refundPolicyAccepted || !privacyPolicyAccepted) {
      const missingAgreements = [];
      if (!refundPolicyAccepted) missingAgreements.push('이용약관');
      if (!privacyPolicyAccepted) missingAgreements.push('개인정보 수집 및 이용');
      alert(`다음 항목에 동의해주세요: ${missingAgreements.join(', ')}`);
      return;
    }

    try {
      await axiosInstance.post('/v1/users', signupRequest);

      alert('회원가입이 성공적으로 완료되었습니다!');
      navigate('/itsmine/login');
    } catch (error) {
      if (error.response && error.response.status === 409) {
        // 409 Conflict 상태 코드를 받으면 아이디 중복으로 처리
        alert('존재하는 아이디입니다.');
      } else {
        console.error('Signup failed:', error);
        alert('회원가입에 실패했습니다. 다시 시도해주세요.');
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
            <Label htmlFor="name">이름 (필수)</Label>
            <Input
                id="name"
                name="name"
                placeholder="이름을 입력하세요"
                value={signupRequest.name}
                onChange={handleChange}
                autoComplete="name"
                autoFocus
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="username">아이디 (필수)</Label>
            <Input
                id="username"
                name="username"
                placeholder="아이디를 입력하세요"
                value={signupRequest.username}
                onChange={handleChange}
                autoComplete="username"
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="email">이메일 주소</Label>
            <Input
                id="email"
                name="email"
                placeholder="이메일을 입력하세요"
                value={signupRequest.email}
                onChange={handleChange}
                autoComplete="email"
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="password">비밀번호 (필수)</Label>
            <Input
                id="password"
                name="password"
                type="password"
                placeholder="비밀번호를 입력하세요"
                value={signupRequest.password}
                onChange={handleChange}
                autoComplete="new-password"
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="address">주소</Label>
            <Input
                id="address"
                name="address"
                placeholder="주소를 입력하세요"
                value={signupRequest.address}
                onChange={handleChange}
                autoComplete="address"
            />
          </SignUpForm>
          <SignUpForm>
            <Label htmlFor="nickname">닉네임 (필수)</Label>
            <Input
                id="nickname"
                name="nickname"
                placeholder="닉네임을 입력하세요"
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

              <h4 style={{ marginTop: '20px' }}>1. 상품 등록 후 수정 및 삭제</h4>
              <p>
                회원이 상품을 등록한 이후에는 해당 상품의 수정이나 삭제가 불가하므로, 등록 전에 모든 내용을 신중히 검토하여야 합니다. 이를 통해 발생하는 모든 책임은 회원 본인에게 있습니다.
              </p>

              <h4 style={{ marginTop: '20px' }}>2. 낙찰 및 환불 정책</h4>
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

          <SignUpBtn type="submit">회원가입</SignUpBtn>
        </Form>
      </Container>
  );
};

// 스타일 컴포넌트 정의
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

const SignUpBtn = styled.button`
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
    background-color: #555555; /* 호버 시 색상 변경 */
  }

  &:active {
    background-color: #111111; /* 클릭 시 더 진한 색상 */
    transform: scale(0.98); /* 클릭 시 눌리는 효과 */
  }
`;

export default SignUp;