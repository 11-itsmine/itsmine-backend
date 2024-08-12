import React, { useEffect, useState, useCallback } from "react";
import axiosInstance from "../../api/axiosInstance";
import styled from "styled-components";
import {useNavigate, useParams} from "react-router-dom";
import ChatWindow from "../chat/ChatWindow";
import Modal from "../chat/Modal";
import ReportForm from "../backOffice/ReportForm";

const AuctionComponent = () => {
  const [product, setProduct] = useState(null);
  const [bidPrice, setBidPrice] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [isLiked, setIsLiked] = useState(false);
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [chatRoomInfo, setChatRoomInfo] = useState(null);
  const [isReportOpen, setIsReportOpen] = useState(false);

  const navigate = useNavigate();
  const {productId} = useParams();

  const fetchProduct = useCallback(async () => {
    try {
      const response = await axiosInstance.get(`/v1/products/${productId}`);
      setProduct(response.data.data);
    } catch (err) {
      setError("제품 정보를 가져오는데 실패했습니다.");
      console.error("Error fetching product data:", err);
      showError("제품 정보를 가져오는데 실패했습니다.");
    }
  }, [productId]);
  
  const fetchLikeStatus = useCallback(async () => {
    try {
      const token = localStorage.getItem("Authorization");
      const response = await axiosInstance.get(
          `/v1/users/products/${productId}/likes`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );

      console.log("Like status response:", response.data); // 응답 데이터 로그 출력
      setIsLiked(response.data.data.liked); // 서버에서 받아온 좋아요 상태로 설정
    } catch (err) {
      console.error("Error fetching like status:", err);
    }
  }, [productId]);

  useEffect(() => {
    fetchProduct();
    fetchLikeStatus();
  }, [fetchProduct, fetchLikeStatus]);

  const toggleLike = async () => {
    try {
      const token = localStorage.getItem("Authorization");
      await axiosInstance.post(
          `/v1/users/products/${productId}/likes`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );
      setIsLiked((prevIsLiked) => !prevIsLiked);
    } catch (err) {
      setError("좋아요 변경에 실패했습니다. 다시 시도하세요.");
      console.error("Error toggling like status:", err);
      showError("좋아요 변경에 실패했습니다. 다시 시도하세요.");
    }
  };

  const handleReport = async (reportData) => {
    const token = localStorage.getItem("Authorization");
    try {
      await axiosInstance.post(
          `/v1/report`,
          {
            ...reportData,
            typeId: productId, // 신고 데이터에 상품 ID 포함
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );
      alert("신고가 접수되었습니다.");
      setIsReportOpen(false);
    } catch (err) {
      setError("신고에 실패했습니다. 다시 시도하세요.");
      console.error("Error reporting content:", err);
      showError("신고에 실패했습니다. 다시 시도하세요.");
    }
  };

  const handleBid = async () => {
    if (!bidPrice) {
      showError("입찰 가격을 입력하세요.");
      return;
    }
    const token = localStorage.getItem("Authorization");
    try {
      const response = await axiosInstance.post(
          `/v1/kakaopay/ready/${productId}`, // 카카오페이 결제 준비 API 경로로 변경
          { bidPrice }, // 입찰 가격을 요청 바디에 포함
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );

      // 응답에서 카카오페이 결제 페이지 URL을 추출
      const { next_redirect_pc_url } = response.data.data;

      // 카카오페이 결제 페이지를 팝업 창으로 열기
      window.open(next_redirect_pc_url, "_blank", "width=600,height=800");

      // 입찰 완료 알림
      alert("입찰이 성공적으로 완료되었습니다.\n홈 화면으로 이동합니다.");
      setMessage("입찰이 성공적으로 완료되었습니다.");
      setError("");
      navigate("/itsmine");

    } catch (err) {
      setError("입찰에 실패했습니다. 다시 시도하세요.");
      console.error("Error creating auction:", err);
      showError("입찰에 실패했습니다. 다시 시도하세요.");
    }
  };

  const nextImage = () => {
    setCurrentImageIndex((prevIndex) =>
        prevIndex === product.imagesUrl.length - 1 ? 0 : prevIndex + 1
    );
  };

  const prevImage = () => {
    setCurrentImageIndex((prevIndex) =>
        prevIndex === 0 ? product.imagesUrl.length - 1 : prevIndex - 1
    );
  };

  const handleBuyNow = () => {
    setBidPrice(product.auctionNowPrice);
  };

  const handleCurrentPrice = () => {
    setBidPrice(product.currentPrice);
  };

  const toggleChatWindow = () => {
    setIsChatOpen(!isChatOpen);
  };

  const handleStartChat = async () => {
    try {
      const token = localStorage.getItem("Authorization");
      const response = await axiosInstance.post(
          `/v1/chatrooms`,
          {userId: product.userId},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );

      setChatRoomInfo(response.data.data);
      setIsChatOpen(true);
    } catch (err) {
      setError("채팅 방 생성에 실패했습니다. 다시 시도하세요.");
      console.error("Error creating chat room:", err);
      showError("채팅 방 생성에 실패했습니다. 다시 시도하세요.");
    }
  };

  const showError = (message) => {
    setError(message);
    setTimeout(() => {
      setError("");
    }, 3000); // 3초 후에 오류 메시지 삭제
  };

  if (error) {
    return <ErrorText>{error}</ErrorText>;
  }

  if (!product) {
    return <LoadingText>Loading...</LoadingText>;
  }

  return (
      <StyledContainer>
        <LeftColumn>
          <ImageSlider>
            <Arrow left onClick={prevImage}>
              &lt;
            </Arrow>
            <MainImage src={product.imagesUrl[currentImageIndex]} alt="Product" />
            <Arrow onClick={nextImage}>&gt;</Arrow>
          </ImageSlider>
          <ThumbnailContainer>
            {product.imagesUrl.map((url, index) => (
                <Thumbnail
                    key={index}
                    src={url}
                    onClick={() => setCurrentImageIndex(index)}
                    isActive={index === currentImageIndex}
                />
            ))}
          </ThumbnailContainer>
        </LeftColumn>
        <RightColumn>
          <ProductTitle>{product.productName}</ProductTitle>
          <LikeButton onClick={toggleLike} isLiked={isLiked}>
            {isLiked ? "♥" : "♡"}
          </LikeButton>
          <Description>{product.description}</Description>
          <PriceInfo>
            <ActionButtons>
              <PriceButton onClick={handleBuyNow} primary>
                <PriceTitle>즉시구매가</PriceTitle>
                <PriceValue>{product.auctionNowPrice.toLocaleString()}원</PriceValue>
              </PriceButton>
              <PriceButton onClick={handleCurrentPrice}>
                <PriceTitle>현재 입찰가</PriceTitle>
                <PriceValue>{product.currentPrice.toLocaleString()}원</PriceValue>
              </PriceButton>
            </ActionButtons>
          </PriceInfo>
          <BidSection>
            <h2>입찰하기</h2>
            <BidContainer>
              <BidInput
                  type="number"
                  value={bidPrice}
                  onChange={(e) => setBidPrice(e.target.value)}
                  placeholder="입찰 가격"
                  min={product.currentPrice + 1}
              />
              <BidButton onClick={handleBid}>입찰</BidButton>
            </BidContainer>
            {message && <SuccessText>{message}</SuccessText>}
            {error && <ErrorText>{error}</ErrorText>}
          </BidSection>
          <ChatAndReportContainer>
            <ChatButton onClick={handleStartChat}>채팅으로 문의하기</ChatButton>
            <ReportButton onClick={() => setIsReportOpen(true)}>신고하기</ReportButton>
          </ChatAndReportContainer>
        </RightColumn>
        <Modal isOpen={isChatOpen} onClose={toggleChatWindow}>
          {chatRoomInfo && (
              <ChatWindow room={chatRoomInfo} onClose={toggleChatWindow} />
          )}
        </Modal>
        <Modal isOpen={isReportOpen} onClose={() => setIsReportOpen(false)}>
          {product && (
              <ReportForm
                  onSubmit={handleReport}
                  onClose={() => setIsReportOpen(false)}
                  productId={product.id} // 제품 ID 전달
              />
          )}
        </Modal>
      </StyledContainer>
  );
};

export default AuctionComponent;
// src/components/auction/AuctionComponent.js

/* Styled Components */
const StyledContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 100px auto;
  max-width: 1200px;
`;

const LeftColumn = styled.div`
  flex: 1;
  max-width: 50%;
  padding-right: 20px;
`;

const RightColumn = styled.div`
  flex: 1;
  padding-left: 20px;
  max-width: 50%;
  position: relative;
`;

const ImageSlider = styled.div`
  width: 100%;
  height: 400px;
  background-color: #f2f2f2;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  border-radius: 10px;
  margin-bottom: 20px;
`;

const MainImage = styled.img`
  width: 100%;
  border-radius: 10px;
  object-fit: cover;
`;

const ThumbnailContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

const Thumbnail = styled.img`
  width: 80px;
  height: 80px;
  object-fit: cover;
  border: ${({ isActive }) =>
      isActive ? "2px solid #007bff" : "2px solid transparent"};
  border-radius: 5px;
  cursor: pointer;
  transition: border 0.3s ease;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  &:hover {
    border: 2px solid #007bff;
  }
`;

const ProductTitle = styled.h1`
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 20px;
`;

const Dot = styled.div`
  width: 8px;
  height: 8px;
  background-color: ${({isActive}) => (isActive ? "#000" : "#bbb")};
  border-radius: 50%;
  margin: 0 5px;
  transition: background-color 0.3s;
`;

const Description = styled.p`
  font-size: 1rem;
  color: #555;
  margin-bottom: 20px;
`;

const PriceInfo = styled.div`
  display: flex;
  align-items: baseline;
  margin-bottom: 20px;
`;

const PriceButton = styled.button`
  flex: 1;
  background-color: ${({ primary }) => (primary ? "#e74c3c" : "#27ae60")};
  padding: 15px;
  color: white;
  border-radius: 10px;
  text-align: left;
  margin-right: ${({ primary }) => (primary ? "10px" : "0")};
  border: none;
  cursor: pointer;
  &:hover {
    background-color: ${({ primary }) => (primary ? "#c0392b" : "#2ecc71")};
  }
`;

const PriceTitle = styled.div`
  font-size: 1.2rem;
  font-weight: bold;
`;

const PriceValue = styled.div`
  font-size: 1rem;
`;

const ActionButtons = styled.div`
  display: flex;
  width: 100%;
  margin-bottom: 20px;
`;

const BidSection = styled.div`
  margin-top: 20px;
`;

const BidContainer = styled.div`
  display: flex;
  align-items: center;
`;

const BidInput = styled.input`
  flex: 3;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  margin-right: 15px;
`;

const BidButton = styled.button`
  flex: 1;
  background-color: #27ae60;
  border: none;
  padding: 15px;
  color: white;
  cursor: pointer;
  border-radius: 5px;
  font-size: 1rem;
  &:hover {
    background-color: #2ecc71;
  }
`;

const ChatAndReportContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
`;

const ChatButton = styled.button`
  background-color: #007bff;
  border: none;
  padding: 15px;
  color: white;
  cursor: pointer;
  border-radius: 5px;
  font-size: 1rem;
  width: 48%;
  &:hover {
    background-color: #0056b3;
  }
`;

const ReportButton = styled.button`
  background-color: #e74c3c;
  border: none;
  padding: 15px;
  color: white;
  cursor: pointer;
  border-radius: 5px;
  font-size: 1rem;
  width: 48%;
  &:hover {
    background-color: #c0392b;
  }
`;

const SuccessText = styled.div`
  color: green;
  font-size: 1rem;
  margin-top: 10px;
`;

const ErrorText = styled.div`
  color: red;
  font-size: 1rem;
  margin-top: 10px;
`;

const LoadingText = styled.div`
  font-size: 1.2rem;
  text-align: center;
`;

const LikeButton = styled.button`
  position: absolute;
  top: 10px;
  right: 10px;
  padding: 10px;
  font-size: 1.5rem;
  background-color: transparent;
  color: ${({isLiked}) => (isLiked ? "#e74c3c" : "#bbb")};
  border: none;
  cursor: pointer;

  &:hover {
    color: ${({isLiked}) => (isLiked ? "#c0392b" : "#888")};
  }
`;

const Arrow = styled.div`
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: transparent;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 1.5rem;
  user-select: none;
  color: #fff;
  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }

  ${({ left }) => (left ? `left: 10px;` : `right: 10px;`)}
`;
