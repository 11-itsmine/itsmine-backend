import React, {useEffect, useState} from "react";
import axiosInstance from "../../api/axiosInstance";
import styled from "styled-components";
import {useNavigate, useParams} from "react-router-dom";
import Modal from "../chat/Modal";
import ReportForm from "../backOffice/ReportForm";
import {Container} from "@mui/material"; // ReportForm import

const AuctionComponent = () => {
  const [product, setProduct] = useState(null);
  const [bidPrice, setBidPrice] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [isLiked, setIsLiked] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [chatRoomInfo, setChatRoomInfo] = useState(null);
  const [isReportOpen, setIsReportOpen] = useState(false); // 신고 모달 상태 추가

  const navigate = useNavigate();
  const {productId} = useParams();

  const fetchProduct = async () => {
    try {
      const response = await axiosInstance.get(`/v1/products/${productId}`);
      setProduct(response.data.data);
    } catch (err) {
      alert("제품 정보를 가져오는데 실패했습니다.");
      setError("제품 정보를 가져오는데 실패했습니다.");
      console.error("Error fetching product data:", err);
    }
  };

  const handleReport = async (reportData) => {
    const token = localStorage.getItem("Authorization");

    try {
      const response = await axiosInstance.post(
          `/v1/report`,
          reportData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );

      alert("신고가 접수되었습니다.");
      setIsReportOpen(false);
    } catch (err) {
      alert("신고에 실패했습니다. 다시 시도하세요.");
      console.error("Error reporting content:", err);
    }
  };

  useEffect(() => {
    fetchProduct();
  }, [productId]);

  if (error) {
    return <ErrorText>{error}</ErrorText>;
  }

  if (!product) {
    return <LoadingText>Loading...</LoadingText>;
  }

  return (
      <Container>
        {/* 기타 코드 생략 */}

        <ReportButton onClick={() => setIsReportOpen(true)}>
          신고하기
        </ReportButton>

        <Modal isOpen={isReportOpen} onClose={() => setIsReportOpen(false)}>
          <ReportForm onSubmit={handleReport}/>
        </Modal>

        {/* 기타 코드 생략 */}
      </Container>
  );
};

export default AuctionComponent;

// 스타일 컴포넌트 정의
const ReportButton = styled.button`
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 1rem;
  background-color: #ff4d4d;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #cc0000;
  }
`;

const ErrorText = styled.div`
  color: red;
  font-size: 1rem;
  margin-top: 10px;
`;

const LoadingText = styled.div`
  font-size: 1.2rem;
  margin-top: 50px;
`;
