import React, { useState } from 'react';
import styled from 'styled-components';
import axiosInstance from "../../api/axiosInstance";

function KakaoPayTidForm() {
  const [username, setUsername] = useState('');
  const [productName, setProductName] = useState('');
  const [bidPrice, setBidPrice] = useState('');
  const [tid, setTid] = useState('');
  const [error, setError] = useState(null);

  // TID를 조회하는 함수
  const handleFetchTid = async () => {
    if (!username || !productName || !bidPrice) {
      setError('모든 필드를 입력해주세요.');
      return;
    }

    try {
      const response = await axiosInstance.post('/v1/kakaopay/tid', {
        username,
        productName,
        bidPrice: parseInt(bidPrice),
      });

      if (response.data && response.data.data) {
        setTid(response.data.data.tid);
        setError(null);
      } else {
        setError('TID를 가져오는데 실패했습니다.');
      }
    } catch (err) {
      setError('TID를 가져오는 동안 오류가 발생했습니다.');
      console.error(err);
    }
  };

  // Refund 요청을 보내는 함수
  const handleRefund = async () => {
    if (!tid) {
      setError('TID를 먼저 가져와주세요.');
      return;
    }

    try {
      await axiosInstance.post(`/v1/kakaopay/refund?tid=${tid}`);
      setError(null);
      alert('환불이 성공적으로 완료되었습니다.');
    } catch (err) {
      setError('환불 요청 중 오류가 발생했습니다.');
      console.error(err);
    }
  };

// Bid Cancel 요청을 보내는 함수
  const handleBidCancel = async () => {
    if (!tid) {
      setError('TID를 먼저 가져와주세요.');
      return;
    }

    try {
      await axiosInstance.post(`/v1/kakaopay/bidCancel?tid=${tid}`);
      setError(null);
      alert('입찰 취소가 성공적으로 완료되었습니다.');
    } catch (err) {
      setError('입찰 취소 요청 중 오류가 발생했습니다.');
      console.error(err);
    }
  };


  return (
      <Container>
        <h1>카카오페이 TID 조회 및 처리</h1>
        <Form>
          <Input
              type="text"
              placeholder="사용자 이름"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
          />
          <Input
              type="text"
              placeholder="상품 이름"
              value={productName}
              onChange={(e) => setProductName(e.target.value)}
          />
          <Input
              type="number"
              placeholder="입찰 가격"
              value={bidPrice}
              onChange={(e) => setBidPrice(e.target.value)}
          />
          <Button onClick={handleFetchTid}>TID 조회</Button>
        </Form>
        {tid && (
            <Form>
              <Input
                  type="text"
                  placeholder="조회된 TID"
                  value={tid}
                  readOnly
              />
              <Button onClick={handleRefund}>환불</Button>
              <Button onClick={handleBidCancel}>입찰 취소</Button>
            </Form>
        )}
        {error && <ErrorText>{error}</ErrorText>}
      </Container>
  );
}

export default KakaoPayTidForm;

// 스타일 컴포넌트 정의
const Container = styled.div`
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 600px;
  margin: 0 auto;
`;

const Form = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
`;

const Input = styled.input`
  padding: 10px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 16px;
  color: #343a40;
`;

const Button = styled.button`
  padding: 10px;
  background-color: #1c7ed6;
  color: #ffffff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  &:hover {
    background-color: #1971c2;
  }
`;

const ErrorText = styled.p`
  margin-top: 20px;
  color: #e03131;
  font-size: 18px;
  font-weight: bold;
`;
