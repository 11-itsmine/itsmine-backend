import React, { useState } from 'react';
import {
  Box,
  Button,
  TextField,
  FormControlLabel,
  Checkbox,
  Typography,
} from '@mui/material';
import axiosInstance from '../../api/axiosInstance';

const QnAForm = ({ productId, onAddQnA }) => {
  const [author, setAuthor] = useState('');
  const [password, setPassword] = useState('');
  const [title, setTitle] = useState('');
  const [question, setQuestion] = useState('');
  const [secretQna, setSecretQna] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const qnaData = {
        author,
        password,
        title,
        content: question,
        secretQna,
      };

      const response = await axiosInstance.post(`/v1/products/${productId}/qnas`, qnaData);

      if (response.data && response.data.data) {
        onAddQnA(response.data.data); // 새로운 QnA를 추가합니다.
        setAuthor(''); // 작성자 필드 초기화
        setPassword(''); // 비밀번호 필드 초기화
        setTitle(''); // 제목 필드 초기화
        setQuestion(''); // 질문 필드 초기화
        setSecretQna(false); // 비밀글 체크박스 초기화
      }
    } catch (error) {
      console.error('Error adding QnA:', error);
    }
  };

  return (
      <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
        <Typography variant="h6">문의하기</Typography>
        <TextField
            fullWidth
            label="작성자"
            value={author}
            onChange={(e) => setAuthor(e.target.value)}
            required
            sx={{ mt: 2 }}
        />
        <TextField
            fullWidth
            label="비밀번호"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            sx={{ mt: 2 }}
        />
        <TextField
            fullWidth
            label="제목"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            multiline
            rows={1}
            required
            sx={{ mt: 2 }}
        />
        <TextField
            fullWidth
            label="질문"
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            multiline
            rows={4}
            required
            sx={{ mt: 2 }}
        />
        <FormControlLabel
            control={
              <Checkbox
                  checked={secretQna}
                  onChange={(e) => setSecretQna(e.target.checked)}
              />
            }
            label="비밀글"
        />
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
          <Button type="submit" variant="contained">
            등록
          </Button>
        </Box>
      </Box>
  );
};

export default QnAForm;
