import React, {useState} from "react";
import axiosInstance from "../../api/axiosInstance";
import styled from "styled-components";
import {useNavigate} from "react-router-dom";

const ReportForm = ({productId, onClose}) => {
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");
  const [reportType, setReportType] = useState("PRODUCT"); // 기본값 "PRODUCT"
  const [error, setError] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("Authorization");
      await axiosInstance.post(
          "/v1/report",
          {
            title,
            content,
            reportType,
            typeId: productId,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );
      alert("신고가 접수되었습니다.");
      onClose(); // 폼 닫기
    } catch (err) {
      setError("신고 접수에 실패했습니다. 다시 시도하세요.");
      console.error("Error reporting:", err);
    }
  };

  return (
      <FormContainer>
        <FormTitle>신고하기</FormTitle>
        <form onSubmit={handleSubmit}>
          <Input
              type="text"
              placeholder="제목"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
          />
          <Textarea
              placeholder="내용"
              value={content}
              onChange={(e) => setContent(e.target.value)}
              required
          />
          <Select
              value={reportType}
              onChange={(e) => setReportType(e.target.value)}
          >
            <option value="USER">사용자</option>
            <option value="PRODUCT">상품</option>
            <option value="CHAT">채팅</option>
            <option value="ETC">기타</option>
          </Select>
          <SubmitButton type="submit">신고하기</SubmitButton>
          {error && <ErrorText>{error}</ErrorText>}
        </form>
      </FormContainer>
  );
};

export default ReportForm;

// 스타일 컴포넌트 정의
const FormContainer = styled.div`
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  width: 400px;
  max-width: 100%;
  margin: 0 auto;
`;

const FormTitle = styled.h2`
  margin-bottom: 15px;
  font-size: 1.5rem;
`;

const Input = styled.input`
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const Textarea = styled.textarea`
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: vertical;
`;

const Select = styled.select`
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const SubmitButton = styled.button`
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #e74c3c;
  color: #fff;
  font-size: 1rem;
  cursor: pointer;

  &:hover {
    background-color: #c0392b;
  }
`;

const ErrorText = styled.div`
  color: red;
  font-size: 0.875rem;
  margin-top: 10px;
`;
