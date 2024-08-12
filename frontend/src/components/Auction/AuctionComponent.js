// src/components/auction/AuctionComponent.js
import React, {useEffect, useState} from "react";
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

  const fetchLikeStatus = async () => {
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
      setIsLiked(response.data.data.liked);
    } catch (err) {
      console.error("Error fetching like status:", err);
    }
  };

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
      alert("좋아요 변경에 실패했습니다. 다시 시도하세요.");
      setError("좋아요 변경에 실패했습니다. 다시 시도하세요.");
      console.error("Error toggling like status:", err);
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
      alert("신고에 실패했습니다. 다시 시도하세요.");
      console.error("Error reporting content:", err);
    }
  };

  useEffect(() => {
    fetchProduct();
    fetchLikeStatus();
  }, [productId]);

  const handleBid = async () => {
    if (!bidPrice) {
      setError("입찰 가격을 입력하세요.");
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
      alert("입찰이 완료되었습니다.");

    } catch (err) {
      alert("입찰에 실패했습니다. 다시 시도하세요.");
      setError("입찰에 실패했습니다. 다시 시도하세요.");
      console.error("Error creating auction:", err);
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
      alert("채팅 방 생성에 실패했습니다. 다시 시도하세요.");
      console.error("Error creating chat room:", err);
    }
  };

  if (error) {
    return <ErrorText>{error}</ErrorText>;
  }

  if (!product) {
    return <LoadingText>Loading...</LoadingText>;
  }

  return (
      <Container>
        {product.imagesUrl && product.imagesUrl.length > 0 && (
            <>
              <ImageSlider>
                <Arrow left onClick={prevImage}>
                  &lt;
                </Arrow>
                <ProductImage
                    src={product.imagesUrl[currentImageIndex]}
                    alt={`Product ${currentImageIndex}`}
                />
                <Arrow onClick={nextImage}>&gt;</Arrow>
              </ImageSlider>
              <Indicator>
                {product.imagesUrl.map((_, index) => (
                    <Dot key={index} isActive={index === currentImageIndex}/>
                ))}
              </Indicator>
            </>
        )}
        <Details>
          <Title>{product.productName}</Title>
          <LikeButton onClick={toggleLike} isLiked={isLiked}>
            {isLiked ? "♥" : "♡"}
          </LikeButton>
          <Description>{product.description}</Description>
        </Details>
        <AdditionalInfo>
          <InfoText>경매 시작가: {product.startPrice}원</InfoText>
          <InfoText>마감일: {new Date(product.dueDate).toLocaleString()}</InfoText>
        </AdditionalInfo>
        <ButtonContainer>
          <PriceButton className="buy-btn" onClick={handleBuyNow}>
            구매 {product.auctionNowPrice.toLocaleString()}원 즉시 구매가
          </PriceButton>
          <PriceButton className="bid-btn" onClick={handleCurrentPrice}>
            입찰 {product.currentPrice.toLocaleString()}원 현재 입찰가
          </PriceButton>
        </ButtonContainer>
        <BidSection>
          <h2>입찰하기</h2>
          <BidInput
              type="number"
              value={bidPrice}
              onChange={(e) => setBidPrice(e.target.value)}
              placeholder="입찰 가격"
              min={product.currentPrice + 1}
          />
          <BidButton onClick={handleBid}>입찰</BidButton>
          {message && <SuccessText>{message}</SuccessText>}
          {error && <ErrorText>{error}</ErrorText>}
        </BidSection>
        <ChatButton onClick={handleStartChat}>채팅으로 문의하기</ChatButton>
        <ReportButton onClick={() => setIsReportOpen(true)}>신고하기</ReportButton>
        <Modal isOpen={isChatOpen} onClose={toggleChatWindow}>
          {chatRoomInfo && <ChatWindow room={chatRoomInfo}
                                       onClose={toggleChatWindow}/>}
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
      </Container>
  );
};

export default AuctionComponent;
// src/components/auction/AuctionComponent.js

// ... (기존 코드 생략)

const Container = styled.div`
  width: 100%;
  max-width: 600px;
  margin: 100px auto;
  background-color: #f9f9f9;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const ImageSlider = styled.div`
  width: 100%;
  height: 300px;
  background-color: #ddd;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  margin-bottom: 10px;
`;

const ProductImage = styled.img`
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
    background-color: rgba(0, 0, 0, 0.2);
  }

  ${({left}) => (left ? `left: 10px;` : `right: 10px;`)}
`;

const Indicator = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 10px;
`;

const Dot = styled.div`
  width: 8px;
  height: 8px;
  background-color: ${({isActive}) => (isActive ? "#000" : "#bbb")};
  border-radius: 50%;
  margin: 0 5px;
  transition: background-color 0.3s;
`;

const Details = styled.div`
  width: 100%;
  text-align: center;
  padding: 10px;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 10px;
  position: relative;
`;

const Title = styled.h1`
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 5px;
`;

const Description = styled.p`
  color: #666;
  margin-top: 10px;
`;

const AdditionalInfo = styled.div`
  width: 100%;
  text-align: center;
  padding: 10px;
  background-color: #fff;
  border-radius: 8px;
  margin-bottom: 20px;
`;

const InfoText = styled.p`
  color: #999;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 20px;
`;

const PriceButton = styled.button`
  padding: 15px;
  border: none;
  border-radius: 5px;
  color: #fff;
  cursor: pointer;
  font-size: 16px;
  width: 48%;
  font-weight: bold;

  &.buy-btn {
    background-color: #e74c3c;
  }

  &.bid-btn {
    background-color: #27ae60;
  }
`;

const BidSection = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
`;

const BidInput = styled.input`
  width: 100%;
  max-width: 200px;
  margin-bottom: 10px;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
`;

const BidButton = styled.button`
  padding: 10px 20px;
  font-size: 1rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #45a049;
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
  margin-top: 50px;
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

const ChatButton = styled.button`
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #0056b3;
  }
`;

const ReportButton = styled.button`
  margin-top: 20px;
  padding: 10px 20px;
  font-size: 1rem;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;

  &:hover {
    background-color: #c82333;
  }
`;
