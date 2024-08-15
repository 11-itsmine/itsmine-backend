import React, {useState} from 'react';
import styled from 'styled-components';
import axiosInstance from '../../api/axiosInstance';

const ReportForm = ({onClose, toUserId}) => {
  const [title, setTitle] = useState('');
  const [reportType, setReportType] = useState('CHAT');
  const [content, setContent] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);

    try {
      await axiosInstance.post('/v1/report', {
        title,          // 신고 제목
        typeId: toUserId,  // 신고할 대상 사용자의 ID
        content,        // 신고 내용
        reportType,     // 신고 유형
      });
      alert('신고가 접수되었습니다.');
      onClose();
    } catch (error) {
      console.error('Failed to submit report:', error);
      alert('신고 제출에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
      <FormContainer>
        <FormHeader>
          <h2>신고하기</h2>
          <CloseButton onClick={onClose}>닫기</CloseButton>
        </FormHeader>
        <FormBody onSubmit={handleSubmit}>
          <Label htmlFor="title">신고 제목</Label>
          <Input
              id="title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="신고 제목을 입력하세요"
          />

          <Label htmlFor="reportType">신고 유형</Label>
          <Select
              id="reportType"
              value={reportType}
              onChange={(e) => setReportType(e.target.value)}
          >
            <option value="CHAT">채팅 메시지</option>
            <option value="USER">사용자</option>
            <option value="ETC">기타</option>
          </Select>

          <Label htmlFor="content">신고 내용</Label>
          <Textarea
              id="content"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="신고 내용을 입력하세요..."
          />

          <ButtonContainer>
            <SubmitButton type="submit" disabled={isSubmitting}>
              {isSubmitting ? '제출 중...' : '제출'}
            </SubmitButton>
          </ButtonContainer>
        </FormBody>
      </FormContainer>
  );
};

export default ReportForm;

// 스타일 컴포넌트 정의
const FormContainer = styled.div`
  display: flex;
  flex-direction: column;
  background-color: #fff;
  color: #000;
  border-radius: 8px;
  width: 80%;
  max-width: 350px;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  position: fixed;
  top: 76%;
  left: 46%;
  transform: translate(-50%, -50%);
  min-height: 52vh;
  max-height: 90vh;
  overflow-y: auto;
  z-index: 1000;
`;

const FormHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
`;

const CloseButton = styled.button`
  background-color: #c7d3f6; /* 제출 버튼과 동일한 배경색 */
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 0.875rem; /* 제출 버튼과 크기 통일 */
  transition: background-color 0.2s;

  &:hover {
    background-color: #5b6eae;
  }
`;

const FormBody = styled.form`
  display: flex;
  flex-direction: column;
`;

const Label = styled.label`
  margin-bottom: 5px;
  font-weight: bold;
`;

const Input = styled.input`
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
`;

const Select = styled.select`
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
`;

const Textarea = styled.textarea`
  margin-bottom: 5px;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
  min-height: 175px;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 10px;
`;

const SubmitButton = styled.button`
  background-color: #bfcdf8;
  color: #fff;
  border: none;
  border-radius: 4px;
  padding: 10px 20px;
  cursor: pointer;
  font-size: 0.875rem;
  transition: background-color 0.2s;
  margin-top: 20px;

  &:hover {
    background-color: #5b6eae;
  }

  &:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }
`;
