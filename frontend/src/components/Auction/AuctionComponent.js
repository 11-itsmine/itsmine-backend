import React, { useEffect, useState, useCallback } from "react";
import axiosInstance from "../../api/axiosInstance";
import styled from "styled-components";
import { useNavigate, useParams } from "react-router-dom";
import ChatWindow from "../chat/ChatWindow";
import Modal from "../chat/Modal";
import ReportForm from "../backOffice/ReportForm";
import QnAList from "../qna/QnAList";

const AuctionComponent = ({ userId, userRole }) => {
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
  const { productId } = useParams();

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

      console.log("Like status response:", response.data);
      setIsLiked(response.data.data.liked);
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

  const handleStartChat = async () => {
    try {
      const token = localStorage.getItem("Authorization");
      const response = await axiosInstance.post(
          `/v1/chatrooms`,
          { userId: product.userId },
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
    }, 3000);
  };

  const handleBid = async () => {
    if (!bidPrice) {
      showError("입찰 가격을 입력하세요.");
      return;
    }
    const token = localStorage.getItem("Authorization");
    try {
      const response = await axiosInstance.post(
          `/v1/kakaopay/ready/${productId}`,
          { bidPrice },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );

      const { next_redirect_pc_url } = response.data.data;
      window.open(next_redirect_pc_url, "_blank", "width=600,height=800");

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

  const handleReportSubmit = async (reportData) => {
    try {
      const token = localStorage.getItem("Authorization");
      await axiosInstance.post(
          `/v1/report`,
          {
            ...reportData,
            productId,
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
      );
      setIsReportOpen(false);
      alert("신고가 접수되었습니다.");
    } catch (err) {
      console.error("Error submitting report:", err);
      showError("신고 접수에 실패했습니다. 다시 시도하세요.");
    }
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
        </LeftColumn>
        <RightColumn>
          <ProductTitle>
            {product.productName}
            <LikeButton onClick={toggleLike} isLiked={isLiked}>
              {isLiked ? "♥" : "♡"}
            </LikeButton>
          </ProductTitle>
          <Description>{product.description}</Description>
          <PriceInfo>
            <PriceButton onClick={handleBuyNow} primary>
              <PriceTitle>즉시구매가</PriceTitle>
              <PriceValue>{product.auctionNowPrice.toLocaleString()}원</PriceValue>
            </PriceButton>
            <PriceButton onClick={handleCurrentPrice}>
              <PriceTitle>현재 입찰가</PriceTitle>
              <PriceValue>{product.currentPrice.toLocaleString()}원</PriceValue>
            </PriceButton>
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
          </BidSection>
          <ChatAndReportContainer>
            <ChatButton onClick={handleStartChat}>채팅으로 문의하기</ChatButton>
            <ReportButton onClick={() => setIsReportOpen(true)}>
              신고하기
            </ReportButton>
          </ChatAndReportContainer>
          {/* Q&A Section */}
          <QnAList productId={productId} userId={userId} userRole={userRole} />
        </RightColumn>
        <Modal isOpen={isChatOpen} onClose={toggleChatWindow}>
          {chatRoomInfo && <ChatWindow room={chatRoomInfo} onClose={toggleChatWindow} />}
        </Modal>
        <Modal isOpen={isReportOpen} onClose={() => setIsReportOpen(false)}>
          <ReportForm onSubmit={handleReportSubmit} />
        </Modal>
      </StyledContainer>
  );
};

export default AuctionComponent;

// Styled Components
const StyledContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 100px auto;
  max-width: 1200px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  padding: 40px;
`;

const LeftColumn = styled.div`
  flex: 2;
  max-width: 50%;
  padding-right: 20px;
`;

const RightColumn = styled.div`
  flex: 3;
  padding-left: 20px;
  max-width: 50%;
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
`;

const MainImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
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

const ProductTitle = styled.h1`
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
`;

const Description = styled.p`
  font-size: 1rem;
  color: #555;
  margin-bottom: 20px;
`;

const PriceInfo = styled.div`
  display: flex;
  justify-content: space-between;
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
  margin-top: 30px;
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

const LikeButton = styled.button`
  background-color: transparent;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: ${({ isLiked }) => (isLiked ? "#e74c3c" : "#ccc")};
  &:hover {
    color: ${({ isLiked }) => (isLiked ? "#c0392b" : "#888")};
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
  margin-top: 50px;
`;
