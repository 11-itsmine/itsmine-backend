import React, { useEffect, useState } from 'react';
import {
  Box,
  Button,
  Container,
  FormControl,
  Grid,
  IconButton,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
  Paper,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import DeleteIcon from '@mui/icons-material/Delete';
import axiosInstance from '../../api/axiosInstance';

const ProductCreatePage = () => {
  const [productName, setProductName] = useState('');
  const [description, setDescription] = useState('');
  const [auctionNowPrice, setAuctionNowPrice] = useState('');
  const [startPrice, setStartPrice] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [categoryName, setCategoryName] = useState('');
  const [categories, setCategories] = useState([]);
  const [imageUrls, setImageUrls] = useState([]);
  const [isProductNameValid, setIsProductNameValid] = useState(false);
  const [isDescriptionValid, setIsDescriptionValid] = useState(false);
  const [isAuctionNowPriceValid, setIsAuctionNowPriceValid] = useState(false);
  const [isStartPriceValid, setIsStartPriceValid] = useState(false);
  const [isDueDateValid, setIsDueDateValid] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const response = await axiosInstance.get('/v1/categories');
        setCategories(response.data.data);
      } catch (error) {
        console.error('Error fetching categories:', error);
        alert('카테고리를 가져오는 중 오류가 발생했습니다.');
      }
    };

    fetchCategories();
  }, []);

  const handleImageChange = async (event) => {
    const files = event.target.files;
    if (!files.length) {
      return;
    }

    const formData = new FormData();
    Array.from(files).forEach((file) => {
      formData.append('file', file);
    });

    try {
      const response = await axiosInstance.post('/v1/s3/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      const newImageUrls = [...imageUrls, ...response.data.imagesUrl];
      setImageUrls(newImageUrls.slice(0, 5)); // 최대 5개 이미지 저장
    } catch (error) {
      console.error('Error uploading image:', error);
      alert('이미지 업로드가 불가능합니다. 새로고침(F5) 후 다시 시도해주세요.');
    }
  };

  const handleImageDelete = (index) => {
    const newImageUrls = [...imageUrls];
    newImageUrls.splice(index, 1);
    setImageUrls(newImageUrls);
  };

  const handleAuctionNowPriceChange = (e) => {
    const value = e.target.value;
    if (value >= 0) { // Ensure value is at least 1
      setAuctionNowPrice(value);
      setIsAuctionNowPriceValid(true);
    } else {
      setIsAuctionNowPriceValid(false);
    }
  };

  const handleStartPriceChange = (e) => {
    const value = e.target.value;
    if (value >= 0) { // Ensure value is at least 1
      setStartPrice(value);
      setIsStartPriceValid(true);
    } else {
      setIsStartPriceValid(false);
    }
  };

  const handleDueDateChange = (e) => {
    const value = e.target.value;
    if (value >= 1) { // Ensure value is at least 1
      setDueDate(value);
      setIsDueDateValid(true);
    } else {
      setIsDueDateValid(false);
    }
  };

  const handleProductNameChange = (e) => {
    const value = e.target.value;
    setProductName(value);
    setIsProductNameValid(!!value);
  };

  const handleDescriptionChange = (e) => {
    const value = e.target.value;
    setDescription(value);
    setIsDescriptionValid(!!value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (
        !isProductNameValid ||
        !isDescriptionValid ||
        !isAuctionNowPriceValid ||
        !isStartPriceValid ||
        !isDueDateValid
    ) {
      alert('모든 필드를 올바르게 입력하세요.');
      return;
    }

    try {
      const productData = {
        productCreateDto: {
          productName,
          description,
          auctionNowPrice,
          startPrice,
          dueDate,
          categoryName,
        },
        productImagesRequestDto: {
          imagesUrl: imageUrls,
        },
      };

      await axiosInstance.post('/v1/products', productData);

      alert('상품 등록이 완료되었습니다.\n홈 화면으로 이동합니다.');
      navigate('/itsmine'); // 상품 등록 후 홈 페이지로 이동
    } catch (error) {
      console.error('Error creating product:', error);
      alert('상품 등록이 불가능합니다. 해당 상품의 이름과 정보를 다시 한번 확인해주세요.');
    }
  };

  return (
      <Container
          maxWidth="lg"
          sx={{
            mt: 1,
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: 'calc(100vh - 64px)',
          }}
      >
        <Paper
            sx={{
              p: 3,
              borderRadius: 2,
              boxShadow: 3,
              width: '100%',
              maxWidth: '1200px',
            }}
        >
          <Grid container spacing={4}>
            <Grid item xs={12} md={7}>
              <Typography variant="h5" component="h1" gutterBottom sx={{ fontWeight: 'bold', color: '#262626' }}>
                상품 등록
              </Typography>
            </Grid>
            <Grid item xs={12} md={5}>
              <Typography variant="h5" gutterBottom sx={{ fontWeight: 'bold', color: '#262626' }}>
                이미지 업로드
              </Typography>
            </Grid>
          </Grid>
          <Box component="form" onSubmit={handleSubmit}>
            <Grid container spacing={4}>
              {/* 왼쪽 섹션: 상품 정보 입력 */}
              <Grid item xs={12} md={7}>
                <Grid container spacing={3}>
                  <Grid item xs={12}>
                    <TextField
                        fullWidth
                        label="상품명"
                        value={productName}
                        onChange={handleProductNameChange}
                        required
                        sx={{
                          '& .MuiInputBase-input': { fontSize: '16px' },
                          '& .MuiInputLabel-root': { color: '#757575', fontSize: '16px' },
                          '& .MuiOutlinedInput-root': {
                            '& fieldset': {
                              borderColor: isProductNameValid ? '#28a745' : '#ff1744',
                            },
                            '&.Mui-focused fieldset': {
                              borderColor: isProductNameValid ? '#28a745' : '#ff1744',
                            },
                          },
                        }}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                        fullWidth
                        label="설명"
                        value={description}
                        onChange={handleDescriptionChange}
                        multiline
                        rows={4}
                        required
                        sx={{
                          '& .MuiInputBase-input': { fontSize: '16px' },
                          '& .MuiInputLabel-root': { color: '#757575', fontSize: '16px' },
                          '& .MuiOutlinedInput-root': {
                            '& fieldset': {
                              borderColor: isDescriptionValid ? '#28a745' : '#ff1744',
                            },
                            '&.Mui-focused fieldset': {
                              borderColor: isDescriptionValid ? '#28a745' : '#ff1744',
                            },
                          },
                        }}
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
                        sx={{
                          '& .MuiInputBase-input': {
                            fontSize: '16px',
                            backgroundColor: isAuctionNowPriceValid ? '#d4edda' : '#ffebee',
                          },
                          '& .MuiInputLabel-root': { color: '#757575', fontSize: '16px' },
                          '& .MuiOutlinedInput-root': {
                            '& fieldset': {
                              borderColor: isAuctionNowPriceValid ? '#28a745' : '#ff1744',
                            },
                          },
                        }}
                        InputProps={{
                          inputProps: { min: 1 }, // Minimum value is 1
                        }}
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
                        sx={{
                          '& .MuiInputBase-input': {
                            fontSize: '16px',
                            backgroundColor: isStartPriceValid ? '#d4edda' : '#ffebee',
                          },
                          '& .MuiInputLabel-root': { color: '#757575', fontSize: '16px' },
                          '& .MuiOutlinedInput-root': {
                            '& fieldset': {
                              borderColor: isStartPriceValid ? '#28a745' : '#ff1744',
                            },
                          },
                        }}
                        InputProps={{
                          inputProps: { min: 1 }, // Minimum value is 1
                        }}
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                        fullWidth
                        label="경매 진행 시간 (시간 단위)"
                        type="number"
                        value={dueDate}
                        onChange={handleDueDateChange}
                        required
                        sx={{
                          '& .MuiInputBase-input': {
                            fontSize: '16px',
                            backgroundColor: isDueDateValid ? '#d4edda' : '#ffebee',
                          },
                          '& .MuiInputLabel-root': { color: '#757575', fontSize: '16px' },
                          '& .MuiOutlinedInput-root': {
                            '& fieldset': {
                              borderColor: isDueDateValid ? '#28a745' : '#ff1744',
                            },
                          },
                        }}
                        InputProps={{
                          inputProps: { min: 1 }, // Minimum value is 1
                        }}
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <FormControl fullWidth required>
                      <InputLabel id="category-label" sx={{ color: '#757575', fontSize: '16px' }}>
                        카테고리
                      </InputLabel>
                      <Select
                          labelId="category-label"
                          value={categoryName}
                          onChange={(e) => setCategoryName(e.target.value)}
                          label="카테고리"
                          sx={{
                            '& .MuiInputBase-input': { fontSize: '16px' },
                            '& .MuiInputLabel-root': { color: '#757575', fontSize: '16px' },
                            '& .MuiOutlinedInput-root': {
                              '& fieldset': {
                                borderColor: categoryName ? '#28a745' : '#ff1744',
                              },
                              '&.Mui-focused fieldset': {
                                borderColor: categoryName ? '#28a745' : '#ff1744',
                              },
                            },
                          }}
                      >
                        {categories.map((category, index) => (
                            <MenuItem key={index} value={category.categoryName}>
                              {category.categoryName}
                            </MenuItem>
                        ))}
                      </Select>
                    </FormControl>
                  </Grid>
                </Grid>
              </Grid>
              {/* 오른쪽 섹션: 이미지 업로드 및 미리보기 */}
              <Grid item xs={12} md={5}>
                <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 2 }}>
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, width: '100%', justifyContent: 'center' }}>
                    {imageUrls.map((url, index) => (
                        <Box key={index} sx={{ position: 'relative', width: '48%', height: '200px' }}>
                          <img
                              src={url}
                              alt={`이미지 ${index + 1}`}
                              style={{ width: '100%', height: '100%', objectFit: 'cover', borderRadius: '8px' }}
                          />
                          <IconButton
                              onClick={() => handleImageDelete(index)}
                              sx={{
                                position: 'absolute',
                                top: 0,
                                right: 0,
                                backgroundColor: 'rgba(255, 255, 255, 0.8)',
                                '&:hover': { backgroundColor: 'rgba(255, 255, 255, 1)' },
                              }}
                          >
                            <DeleteIcon sx={{ color: '#ff1744' }} />
                          </IconButton>
                        </Box>
                    ))}
                  </Box>
                </Box>
              </Grid>
            </Grid>
            <Grid container justifyContent="space-between" alignItems="center" sx={{ mt: 4 }}>
              <Button
                  type="submit"
                  variant="contained"
                  sx={{
                    backgroundColor: '#262626',
                    color: '#ffffff',
                    '&:hover': { backgroundColor: '#000000' },
                    width: '200px',
                  }}
              >
                등록
              </Button>
              <Button
                  variant="contained"
                  component="label"
                  sx={{
                    backgroundColor: '#262626',
                    color: '#ffffff',
                    '&:hover': { backgroundColor: '#000000' },
                    width: '200px',
                  }}
              >
                이미지 업로드
                <input type="file" hidden onChange={handleImageChange} multiple />
              </Button>
            </Grid>
          </Box>
        </Paper>
      </Container>
  );
};

export default ProductCreatePage;
