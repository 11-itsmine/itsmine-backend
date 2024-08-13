import React, { useState } from "react";
import { Box, TextField, Button } from "@mui/material";
import axiosInstance from "../../api/axiosInstance";

const ReplyForm = ({ qnaId, author, onReplyAdded }) => {
  const [content, setContent] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axiosInstance.post(`/v1/qnas/${qnaId}/comments`, {
        author, // 저자를 포함하여 백엔드로 전송
        content,
      });

      if (response.data) {
        onReplyAdded(response.data.data);
        setContent("");
      }
    } catch (error) {
      console.error("Error adding reply:", error);
    }
  };

  return (
      <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
        <TextField
            fullWidth
            label="답글 내용"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
            multiline
            rows={4}
            sx={{ mt: 2 }}
        />
        <Button type="submit" variant="contained" sx={{ mt: 2 }}>
          답글 등록
        </Button>
      </Box>
  );
};

export default ReplyForm;
