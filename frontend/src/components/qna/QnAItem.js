import React, { useState, useEffect } from "react";
import { Box, TextField, Button, Typography, IconButton, InputAdornment } from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import axiosInstance from "../../api/axiosInstance";

const QnAItem = ({ qna, qnaId, productId, userId, userRole, onQnAUpdated, onQnADeleted, onReply }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [updatedTitle, setUpdatedTitle] = useState(qna?.title || "");
  const [updatedContent, setUpdatedContent] = useState(qna?.content || "");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [showButtons, setShowButtons] = useState(true);
  const [error, setError] = useState("");
  const [comment, setComment] = useState(null); // 단일 댓글 상태 추가
  const [showComment, setShowComment] = useState(false); // 댓글 표시 여부

  useEffect(() => {
    const fetchComment = async () => {
      try {
        const response = await axiosInstance.get(`/v1/qnas/${qnaId}/comments`);
        console.log("Fetched Comment:", response.data.data); // 데이터를 확인합니다.
        setComment(response.data.data || null); // 백엔드에서 받은 데이터를 상태로 설정
      } catch (error) {
        console.error("Error fetching comment:", error);
      }
    };

    if (showComment) {
      fetchComment();
    }
  }, [qnaId, showComment]);

  const handleUpdate = async () => {
    try {
      const response = await axiosInstance.put(`/v1/products/${productId}/qnas/${qnaId}`, {
        title: updatedTitle,
        content: updatedContent,
        password: password,
      });

      onQnAUpdated(response.data.data);
      setIsEditing(false);
      setShowButtons(true);
    } catch (error) {
      console.error("Error updating QnA:", error);
      setError("수정에 실패했습니다. 패스워드를 확인해주세요.");
    }
  };

  const handleDelete = async () => {
    try {
      await axiosInstance.delete(`/v1/products/${productId}/qnas/${qnaId}`, {
        data: { password: password },
      });
      onQnADeleted(qnaId);
    } catch (error) {
      console.error("Error deleting QnA:", error);
      setError("삭제에 실패했습니다. 패스워드를 확인해주세요.");
    }
  };

  const handleReplyClick = () => {
    onReply(qnaId);
  };

  const handleClickShowPassword = () => {
    setShowPassword(!showPassword);
  };

  const toggleShowComment = () => {
    setShowComment(!showComment);
  };

  return (
      <Box sx={{ mt: 2, p: 2, border: "1px solid #ccc", borderRadius: "8px", display: "flex", flexDirection: "column" }}>
        {isEditing ? (
            <Box sx={{ flex: 1 }}>
              <TextField
                  fullWidth
                  label="제목"
                  value={updatedTitle}
                  onChange={(e) => setUpdatedTitle(e.target.value)}
                  sx={{ mt: 1 }}
              />
              <TextField
                  fullWidth
                  label="질문"
                  value={updatedContent}
                  onChange={(e) => setUpdatedContent(e.target.value)}
                  multiline
                  rows={4}
                  sx={{ mt: 1 }}
              />
              <TextField
                  fullWidth
                  label="비밀번호"
                  type={showPassword ? "text" : "password"}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  sx={{ mt: 1 }}
                  InputProps={{
                    endAdornment: (
                        <InputAdornment position="end">
                          <IconButton aria-label="toggle password visibility" onClick={handleClickShowPassword} edge="end">
                            {showPassword ? <VisibilityOff /> : <Visibility />}
                          </IconButton>
                        </InputAdornment>
                    ),
                  }}
              />
              <Button variant="contained" onClick={handleUpdate} sx={{ mt: 2, mr: 1 }}>
                수정 완료
              </Button>
              <Button variant="outlined" onClick={() => { setIsEditing(false); setShowButtons(true); }} sx={{ mt: 2 }}>
                취소
              </Button>
              {error && <Typography color="error">{error}</Typography>}
            </Box>
        ) : (
            <Box sx={{ flex: 1 }}>
              <Typography variant="h6">{qna.title}</Typography>
              <Typography>{qna.content}</Typography>
              <Typography variant="caption">작성일: {new Date(qna.createdAt).toLocaleString()}</Typography>
            </Box>
        )}
        {showButtons && (
            <Box sx={{ display: "flex", flexDirection: "column", justifyContent: "center" }}>
              {(userId === qna.userId || userRole === "MANAGER") && (
                  <>
                    <Button variant="outlined" onClick={() => { setIsEditing(true); setShowButtons(false); }} sx={{ mb: 1 }}>
                      수정
                    </Button>
                    <Button variant="contained" color="error" onClick={handleDelete}>
                      삭제
                    </Button>
                  </>
              )}
              {(userId === qna.userId || userRole === "MANAGER") && (
                  <Button variant="contained" onClick={handleReplyClick} sx={{ mt: 2 }}>
                    답글
                  </Button>
              )}
              <Button variant="text" onClick={toggleShowComment} sx={{ mt: 2 }}>
                {showComment ? "답글 숨기기" : "답글 보기"}
              </Button>
            </Box>
        )}
        {showComment && comment && (
            <Box sx={{ mt: 2, pl: 4, borderLeft: "2px solid #ccc" }}>
              <Typography variant="body2"><strong>{comment.author}</strong>:</Typography>
              <Typography variant="body2">{comment.content}</Typography>
              <Typography variant="caption">작성일: {new Date(comment.createdAt).toLocaleString()}</Typography>
            </Box>
        )}
      </Box>
  );
};

export default QnAItem;
