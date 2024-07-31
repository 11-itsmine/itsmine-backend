import React, { useState } from 'react';
import axios from 'axios';
import { Box, Button, Container, Grid, TextField, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const ProductCreatePage = () => {
  const [productName, setProductName] = useState('');
  const [description, setDescription] = useState('');
  const [auctionNowPrice, setAuctionNowPrice] = useState('');
  const [startPrice, setStartPrice] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [categoryName, setCategoryName] = useState('');
  const [imageUrls, setImageUrls] = useState([]);
  const navigate = useNavigate();

  const handleImageChange = async (event) => {
    const files = event.target.files;
    if (!files.length) return;

    const formData = new FormData();
    Array.from(files).forEach((file, index) => {
      formData.append('file', file);
    });

    try {
      const response = await axios.post('http://localhost:8080/s3/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      const newImageUrls = [...imageUrls, ...response.data.imagesUrl];
      setImageUrls(newImageUrls.slice(0, 5)); // 최대 5개 이미지 저장
    } catch (error) {
      console.error('Error uploading image:', error);
    }
  };

  const token = localStorage.getItem('Authorization');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const productData = {
        productCreateDto: {
          productName,
          description,
          auctionNowPrice,
          startPrice,
          dueDate,
          categoryName
        },
        productImagesRequestDto: {
          imagesUrl: imageUrls
        }
      };

      await axios.post('http://localhost:8080/products', productData, {
        headers: {
          Authorization: token
        }
      });

      navigate('/'); // 상품 등록 후 홈 페이지로 이동
    } catch (error) {
      console.error('Error creating product:', error);
    }
  };

  return (
      <Container>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 3 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            상품 등록
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <Button variant="contained" component="label">
                이미지 업로드
                <input type="file" hidden onChange={handleImageChange} multiple />
              </Button>
            </Grid>
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, mt: 2 }}>
                {imageUrls.map((url, index) => (
                    <img key={index} src={url} alt={`이미지 ${index + 1}`} style={{ width: '18%', height: 'auto' }} />
                ))}
              </Box>
            </Grid>
            <Grid item xs={12}>
              <TextField
                  fullWidth
                  label="상품명"
                  value={productName}
                  onChange={(e) => setProductName(e.target.value)}
                  required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                  fullWidth
                  label="설명"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  multiline
                  rows={4}
                  required
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                  fullWidth
                  label="현재 가격"
                  type="number"
                  value={auctionNowPrice}
                  onChange={(e) => setAuctionNowPrice(e.target.value)}
                  required
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                  fullWidth
                  label="시작 가격"
                  type="number"
                  value={startPrice}
                  onChange={(e) => setStartPrice(e.target.value)}
                  required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                  fullWidth
                  label="종료 시간 (시간 단위)"
                  type="number"
                  value={dueDate}
                  onChange={(e) => setDueDate(e.target.value)}
                  required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                  fullWidth
                  label="카테고리"
                  value={categoryName}
                  onChange={(e) => setCategoryName(e.target.value)}
                  required
              />
            </Grid>
            <Grid item xs={12}>
              <Button type="submit" variant="contained" color="primary" fullWidth>
                등록
              </Button>
            </Grid>
          </Grid>
        </Box>
      </Container>
  );
};

export default ProductCreatePage;
