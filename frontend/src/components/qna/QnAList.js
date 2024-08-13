import React, { useEffect, useState } from "react";
import { Box, Typography, Button } from "@mui/material";
import axiosInstance from "../../api/axiosInstance";
import QnAItem from "./QnAItem";
import QnAForm from "./QnAForm";
import ReplyForm from "./ReplyForm";

const QnAList = ({ productId, userId, userRole }) => {
  const [qnaList, setQnAList] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [itemsPerPage] = useState(10);
  const [replyToQnaId, setReplyToQnaId] = useState(null);
  const [isProductOwner, setIsProductOwner] = useState(false);

  useEffect(() => {
    const fetchQnAList = async () => {
      try {
        const response = await axiosInstance.get(`/v1/products/${productId}/qnas`);
        console.log("QnA List:", response.data.data);
        if (response.data && response.data.data) {
          const filteredQnAList = response.data.data.filter((qna) => qna && qna.id);
          setQnAList(filteredQnAList);
          setIsProductOwner(response.data.productOwnerId === userId || userRole === 'MANAGER');
        }
      } catch (error) {
        console.error("Error fetching QnA list:", error);
      }
    };

    fetchQnAList();
  }, [productId, userId, userRole]);

  const handleQnAUpdated = (updatedQnA) => {
    setQnAList((prevQnAList) =>
        prevQnAList.map((qna) => (qna && qna.id === updatedQnA.id ? updatedQnA : qna))
    );
  };

  const handleQnADeleted = (deletedQnAId) => {
    setQnAList((prevQnAList) => prevQnAList.filter((qna) => qna && qna.id !== deletedQnAId));
  };

  const handleAddQnA = (newQnA) => {
    setQnAList((prevQnAList) => [...prevQnAList, newQnA]);
  };

  const handleReply = (qnaId) => {
    setReplyToQnaId(qnaId);
  };

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = qnaList.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(qnaList.length / itemsPerPage);

  return (
      <Box sx={{ mt: 4 }}>
        <Typography variant="h6">Q&A</Typography>
        <QnAForm productId={productId} onAddQnA={handleAddQnA} />
        {currentItems.length > 0 ? (
            currentItems.map((qna) =>
                qna && qna.id ? (
                    <Box key={qna.id}>
                      <QnAItem
                          qna={qna}
                          qnaId={qna.id}
                          productId={productId}
                          userId={userId}
                          userRole={userRole}
                          onQnAUpdated={handleQnAUpdated}
                          onQnADeleted={handleQnADeleted}
                          onReply={handleReply}
                          isProductOwner={isProductOwner}
                      />
                      {replyToQnaId === qna.id && isProductOwner && (
                          <ReplyForm
                              qnaId={qna.id}
                              author={userId}
                              onReplyAdded={() => setReplyToQnaId(null)}
                          />
                      )}
                    </Box>
                ) : null
            )
        ) : (
            <Typography sx={{ mt: 2 }}>아직 질문이 없습니다.</Typography>
        )}
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <Button
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
          >
            이전
          </Button>
          <Typography>
            {currentPage} / {totalPages}
          </Typography>
          <Button
              disabled={currentPage === totalPages}
              onClick={() => setCurrentPage((prev) => Math.min(prev + 1, totalPages))}
          >
            다음
          </Button>
        </Box>
      </Box>
  );
};

export default QnAList;
