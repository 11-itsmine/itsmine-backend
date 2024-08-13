import React, { useState } from 'react';
import {
  Box,
  Button,
  TextField,
  Typography,
  IconButton,
  InputAdornment,
} from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import axiosInstance from '../../api/axiosInstance';

const QnAItem = ({ qna, qnaId, productId, userId, userRole, onQnAUpdated, onQnADeleted, onReply }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [updatedTitle, setUpdatedTitle] = useState(qna?.title || '');
  const [updatedContent, setUpdatedContent] = useState(qna?.content || '');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showButtons, setShowButtons] = useState(true);
  const [isReplying, setIsReplying] = useState(false);
  const [commentContent, setCommentContent] = useState('');

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
      console.error('Error updating QnA:', error);
      alert('수정에 실패했습니다. 패스워드를 확인해주세요.');
    }
  };

  const handleDelete = async () => {
    try {
      await axiosInstance.delete(`/v1/products/${productId}/qnas/${qnaId}`, {
        data: { password: password },
      });
      onQnADeleted(qnaId);
    } catch (error) {
      console.error('Error deleting QnA:', error);
      alert('삭제에 실패했습니다. 패스워드를 확인해주세요.');
    }
  };

  const handleReply = async () => {
    try {
      const response = await axiosInstance.post(`/v1/qnas/${qnaId}/comments`, {
        content: commentContent,
      });

      onReply(); // 답글이 추가된 후 부모 컴포넌트에 알려줍니다.
      setCommentContent('');
      setIsReplying(false); // 답글 폼을 닫습니다.
    } catch (error) {
      console.error('Error adding comment:', error);
      alert('댓글 작성에 실패했습니다.');
    }
  };

  const handleClickShowPassword = () => {
    setShowPassword(!showPassword);
  };

  return (
      <Box sx={{ mt: 2, p: 2, border: '1px solid #ccc', borderRadius: '8px', display: 'flex', justifyContent: 'space-between' }}>
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
              {(userId === qna.userId || userRole === 'MANAGER') && (
                  <>
                    <TextField
                        fullWidth
                        label="비밀번호"
                        type={showPassword ? 'text' : 'password'}
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        sx={{ mt: 1 }}
                        InputProps={{
                          endAdornment: (
                              <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowPassword}
                                    edge="end"
                                >
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
                  </>
              )}
            </Box>
        ) : (
            <Box sx={{ flex: 1 }}>
              <Typography variant="h6">{qna.title}</Typography>
              <Typography>{qna.content}</Typography>
              <Typography variant="caption" display="block" sx={{ mt: 1 }}>
                작성일: {new Date(qna.createdAt).toLocaleDateString()} {/* 작성일 표시 */}
              </Typography>
            </Box>
        )}
        {showButtons && (
            <Box sx={{ display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
              {(userId === qna.userId || userRole === 'MANAGER') && (
                  <>
                    <Button variant="outlined" onClick={() => { setIsEditing(true); setShowButtons(false); }} sx={{ mb: 1 }}>
                      수정
                    </Button>
                    <Button variant="contained" color="error" onClick={handleDelete}>
                      삭제
                    </Button>
                  </>
              )}
              {(userId === qna.userId || userRole === 'MANAGER') && !isReplying && (
                  <Button variant="contained" onClick={() => setIsReplying(true)} sx={{ mt: 2 }}>
                    답글
                  </Button>
              )}
              {isReplying && (
                  <Box sx={{ mt: 2 }}>
                    <TextField
                        fullWidth
                        label="답글 작성"
                        value={commentContent}
                        onChange={(e) => setCommentContent(e.target.value)}
                        sx={{ mt: 2 }}
                        multiline
                        rows={2}
                    />
                    <Button variant="contained" onClick={handleReply} sx={{ mt: 2 }}>
                      답글 등록
                    </Button>
                  </Box>
              )}
            </Box>
        )}
      </Box>
  );
};

export default QnAItem;
