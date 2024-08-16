import React, { useState } from 'react';
import axiosInstance from '../../api/axiosInstance';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import PasswordModal from "./PasswordModal"; // Modal 컴포넌트 경로를 맞춰주세요

const FindPassword = () => {
  const [formRequest, setFormRequest] = useState({
    username: '',
    name: '',
    email: '',
  });

  const [errors, setErrors] = useState({});
  const [touched, setTouched] = useState({});
  const [temporaryPassword, setTemporaryPassword] = useState('');
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  const validateFormRequest = () => {
    const { username, name, email } = formRequest;
    let newErrors = {};

    if (!username) {
      newErrors.username = "아이디를 입력해주세요.";
    }

    if (!name) {
      newErrors.name = "이름을 입력해주세요.";
    }

    if (!email) {
      newErrors.email = "이메일을 입력해주세요.";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
      newErrors.email = "유효한 이메일 주소를 입력해주세요.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0 ? null : newErrors;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const validationErrors = validateFormRequest();
    if (validationErrors) {
      return;
    }

    try {
      const response = await axiosInstance.post('/v1/users/find-password', formRequest);

      if (response.status === 200 && response.data.data) {
        setTemporaryPassword(response.data.data); // 임시 비밀번호 설정
        setShowModal(true); // 모달 표시
      } else if (response.status === 404) {
        alert('입력하신 정보와 일치하는 계정이 없습니다.');
      }
    } catch (error) {
      console.error('Password find failed:', error);
      alert('비밀번호 찾기에 실패했습니다. 다시 시도해주세요.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormRequest({ ...formRequest, [name]: value });
    setErrors({ ...errors, [name]: null });
  };

  const handleBlur = (e) => {
    const { name } = e.target;
    setTouched({ ...touched, [name]: true });
    validateFormRequest();
  };

  const handleCloseModal = () => {
    setShowModal(false);
    // username, name, email을 state로 전달하면서 이동
    navigate('/itsmine/change-password', {
      state: {
        username: formRequest.username,
        name: formRequest.name,
        email: formRequest.email,
      }
    });
  };

  const getInputBorderStyle = (fieldName) => {
    if (touched[fieldName] && errors[fieldName]) {
      return 'red';
    } else if (touched[fieldName] && !errors[fieldName]) {
      return 'green';
    } else {
      return '#ebebeb';
    }
  };

  return (
      <Container>
        <Logo>Find Password</Logo>
        <Form onSubmit={handleSubmit}>
          <FormField>
            <Label htmlFor="username">아이디 (필수)</Label>
            <Input
                id="username"
                name="username"
                placeholder="아이디를 입력하세요"
                value={formRequest.username}
                onChange={handleChange}
                onBlur={handleBlur}
                autoComplete="username"
                borderColor={getInputBorderStyle('username')}
            />
            {errors.username && <Error>{errors.username}</Error>}
          </FormField>
          <FormField>
            <Label htmlFor="name">이름 (필수)</Label>
            <Input
                id="name"
                name="name"
                placeholder="이름을 입력하세요"
                value={formRequest.name}
                onChange={handleChange}
                onBlur={handleBlur}
                autoComplete="name"
                borderColor={getInputBorderStyle('name')}
            />
            {errors.name && <Error>{errors.name}</Error>}
          </FormField>
          <FormField>
            <Label htmlFor="email">이메일 (필수)</Label>
            <Input
                id="email"
                name="email"
                placeholder="이메일을 입력하세요"
                value={formRequest.email}
                onChange={handleChange}
                onBlur={handleBlur}
                autoComplete="email"
                borderColor={getInputBorderStyle('email')}
            />
            {errors.email && <Error>{errors.email}</Error>}
          </FormField>

          <SubmitButton type="submit">비밀번호 찾기</SubmitButton>
        </Form>

        {showModal && (
            <PasswordModal
                message={`임시 비밀번호는 ${temporaryPassword} 입니다.`}
                onClose={handleCloseModal}
            />
        )}
      </Container>
  );
};

// 스타일 컴포넌트 정의는 동일합니다
export default FindPassword;

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

const FormField = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: ${props => props.theme.margins.xl};
  width: 100%;
`;

const Label = styled.label`
  font-size: ${props => props.theme.fontSizes.small};
  padding-bottom: ${props => props.theme.paddings.base};
`;

const Input = styled.input`
  border: none;
  border-bottom: 2px solid ${props => props.borderColor || '#ebebeb'};
  padding: ${props => props.theme.paddings.base} 0;
  font-size: ${props => props.theme.fontSizes.small};

  &:focus {
    outline: none;
    border-bottom: 2px solid ${props => props.borderColor || 'black'};

    ::placeholder {
      opacity: 0;
    }
  }

  ::placeholder {
    color: #bbb;
    opacity: 1;
  }
`;

const Error = styled.div`
  color: red;
  font-size: ${props => props.theme.fontSizes.xs};
  margin-top: 0.5rem;
`;

const SubmitButton = styled.button`
  background: #333333;
  width: 100%;
  margin-top: 0.8rem;
  margin-bottom: 0.5rem;
  padding: ${props => props.theme.paddings.large};
  border: none;
  border-radius: 10px;
  font-size: ${props => props.theme.fontSizes.base};
  color: ${props => props.theme.colors.white};
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;

  &:hover {
    background-color: #555555;
  }

  &:active {
    background-color: #111111;
    transform: scale(0.98);
  }
`;