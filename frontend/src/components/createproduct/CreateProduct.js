import React, { useEffect, useState } from 'react';
import {
  Box,
  Button,
  Container,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import IconButton from '@mui/material/IconButton';
import DeleteIcon from '@mui/icons-material/Delete';
import axiosInstance from "../../api/axiosInstance";

const ProductCreatePage = () => {
  const [productName, setProductName] = useState('');
  const [description, setDescription] = useState('');
  const [auctionNowPrice, setAuctionNowPrice] = useState('');
  const [startPrice, setStartPrice] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [categoryName, setCategoryName] = useState('');
  const [categories, setCategories] = useState([]);
  const [imageUrls, setImageUrls] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axiosInstance.get('/v1/categories');
        setCategories(response.data.data);
      } catch (error) {
        console.error('Error fetching categories:', error);
        alert("카테고리를 가져오는 중 오류가 발생했습니다.");
      }
    };

    fetchCategories();
  }, []);

  const handleImageChange = async (event) => {
    const files = event.target.files;
    if (!files.length) return;

    const existingUrls = new Set(imageUrls);
    const formData = new FormData();
    Array.from(files).forEach((file) => {
      if (!existingUrls.has(file.name)) {
        formData.append('file', file);
      }
    });

    try {
      const response = await axiosInstance.post('/v1/s3/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      const newImageUrls = [...imageUrls, ...response.data.imagesUrl];
      setImageUrls(newImageUrls.slice(0, 5)); // 최대 5개 이미지 저장
    } catch (error) {
      console.error('Error uploading image:', error);
      alert("이미지 업로드가 불가능합니다. 새로고침(F5) 뒤 다시 시도해주세요");
    }
  };

  const handleImageDelete = (index) => {
    const newImageUrls = [...imageUrls];
    newImageUrls.splice(index, 1);
    setImageUrls(newImageUrls);
  };

  const handleStartPriceChange = (e) => {
    const value = Math.max(0, Number(e.target.value) || 0);
    setStartPrice(value);
  };

  const handleAuctionNowPriceChange = (e) => {
    const value = Math.max(0, Number(e.target.value) || 0);
    setAuctionNowPrice(value);
  };

  const handleDueDateChange = (e) => {
    const hoursToAdd = Math.max(0, Number(e.target.value) || 0);
    const currentTime = new Date();
    const futureTime = new Date(currentTime.getTime() + hoursToAdd * 60 * 60 * 1000); // 시간 단위로 추가
    setDueDate(futureTime.toISOString()); // ISO 형식으로 서버에 전달
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!productName || !description || !auctionNowPrice || !startPrice || !dueDate || !categoryName) {
      alert('모든 필드를 입력해주세요.');
      return;
    }

    try {
      const token = localStorage.getItem('Authorization');
      const productData = {
        productCreateDto: {
          productName,
          description,
          auctionNowPrice: Number(auctionNowPrice),
          startPrice: Number(startPrice),
          dueDate,
          categoryName
        },
        productImagesRequestDto: {
          imagesUrl: imageUrls
        }
      };

      console.log("Sending product data:", JSON.stringify(productData, null, 2));
      console.log("Authorization header:", token ? `Bearer ${token.trim()}` : '');

      const response = await axiosInstance.post(`/v1/products`, productData, {
        headers: {
          Authorization: token ? `Bearer ${token.trim()}` : ''
        }
      });

      alert('상품 등록이 완료 되었습니다.\n홈 화면으로 이동합니다.');
      navigate('/itsmine');
    } catch (error) {
      console.error('Error response:', error.response);
      console.error('Error data:', error.response ? error.response.data : error.message);
      if (error.response && error.response.status === 400) {
        alert("잘못된 요청입니다. 입력한 정보를 다시 확인해주세요.");
      } else {
        alert("상품 등록이 불가능합니다. 서버 관리자에게 문의하세요.");
      }
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
                    <Box key={index} sx={{
                      position: 'relative',
                      width: '18%',
                      height: 'auto'
                    }}>
                      <img src={url} alt={`이미지 ${index + 1}`} style={{
                        width: '100%',
                        height: '100%',
                        objectFit: 'cover'
                      }} />
                      <IconButton
                          onClick={() => handleImageDelete(index)}
                          sx={{ position: 'absolute', top: 0, right: 0 }}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Box>
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
                  label="즉시 구매가"
                  type="number"
                  value={auctionNowPrice}
                  onChange={handleAuctionNowPriceChange}
                  required
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <TextField
                  fullWidth
                  label="시작 가격"
                  type="number"
                  value={startPrice}
                  onChange={handleStartPriceChange}
                  required
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                  fullWidth
                  label="경매 진행 시간 (시간 단위)"
                  type="number"
                  onChange={handleDueDateChange}
                  required
              />
            </Grid>
            <Grid item xs={12}>
              <FormControl fullWidth required>
                <InputLabel id="category-label">카테고리</InputLabel>
                <Select
                    labelId="category-label"
                    value={categoryName}
                    onChange={(e) => setCategoryName(e.target.value)}
                    label="카테고리"
                >
                  {categories.map((category, index) => (
                      <MenuItem key={index} value={category.categoryName}>
                        {category.categoryName}
                      </MenuItem>
                  ))}
                </Select>
              </FormControl>
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
