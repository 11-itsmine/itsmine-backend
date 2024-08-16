import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axiosInstance from '../../api/axiosInstance';
import styled from 'styled-components';

const ChangePassword = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { username, name, email } = location.state || {};

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handlePasswordChange = async (e) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      alert('새 비밀번호가 일치하지 않습니다.');
      return;
    }

    try {
      await axiosInstance.post('/v1/users/change-password', {
        username,
        name,
        email,
        currentPassword,
        newPassword,
        confirmPassword
      });

      alert('비밀번호가 성공적으로 변경되었습니다.');
      navigate('/itsmine/login');
    } catch (error) {
      console.error('Error changing password:', error);
      alert('비밀번호 변경에 실패했습니다. 다시 시도해주세요.');
    }
  };

  return (
      <Container>
        <Form onSubmit={handlePasswordChange}>
          <h2>비밀번호 변경</h2>
          <Input
              type="password"
              placeholder="임시 비밀번호"
              value={currentPassword}
              onChange={(e) => setCurrentPassword(e.target.value)}
              required
          />
          <Input
              type="password"
              placeholder="새 비밀번호"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
          />
          <Input
              type="password"
              placeholder="비밀번호 확인"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
          />
          <SubmitButton type="submit">비밀번호 변경</SubmitButton>
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

const Form = styled.form`
  width: 100%;
`;

const Input = styled.input`
  width: 100%;
  padding: 1rem;
  margin: 1rem 0;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 1rem;
`;

const SubmitButton = styled.button`
  background: #333333;
  width: 100%;
  padding: 1rem;
  border: none;
  border-radius: 10px;
  font-size: 1rem;
  color: #fff;
  cursor: pointer;

  &:hover {
    background-color: #555555;
  }

  &:active {
    background-color: #111111;
  }
`;

export default ChangePassword;
