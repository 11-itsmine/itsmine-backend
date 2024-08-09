import React, { useEffect, useState } from 'react';
import {
  Avatar,
  Box,
  Container,
  Grid,
  Typography,
  Paper,
  Tabs,
  Tab,
  Button,
  IconButton,
  TextField,
} from '@mui/material';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import EditIcon from '@mui/icons-material/Edit';
import axiosInstance from '../../api/axiosInstance';

const Profile = () => {
  const [profile, setProfile] = useState(null);
  const [profileError, setProfileError] = useState(null);
  const [file, setFile] = useState(null);
  const [uploadError, setUploadError] = useState(null);
  const [uploadSuccess, setUploadSuccess] = useState(false);

  const [products, setProducts] = useState([]);
  const [productError, setProductError] = useState(null);

  const [likedProducts, setLikedProducts] = useState([]);
  const [likedError, setLikedError] = useState(null);

  const [auctions, setAuctions] = useState([]);
  const [auctionError, setAuctionError] = useState(null);

  const [tabValue, setTabValue] = useState(0);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);

  const defaultProfileImageUrl = '/images/default-profile.png';

  const [profileImageUrl, setProfileImageUrl] = useState(defaultProfileImageUrl);
  const [editMode, setEditMode] = useState(false);

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axiosInstance.get('/v1/users/profile');
        const userData = response.data.data;
        setProfile(userData);

        if (userData.imageUrls && userData.imageUrls.length > 0) {
          setProfileImageUrl(userData.imageUrls[0]);
        } else {
          setProfileImageUrl(defaultProfileImageUrl);
        }
      } catch (err) {
        alert("프로필 정보를 가져오는 중 오류가 발생했습니다.");
        setProfileError(
            err.response ? err.response.data : '프로필 정보를 가져오는 중 오류가 발생했습니다.'
        );
      }
    };

    fetchUserProfile();
  }, []);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axiosInstance.get('/v1/products/user', {
          params: {
            page,
            size,
          },
        });
        setProducts(response.data.data.content);
      } catch (err) {
        alert("내 상품 목록을 가져오는 중 오류가 발생했습니다.");
        setProductError(
            err.response ? err.response.data : '내 상품 목록을 가져오는 중 오류가 발생했습니다.'
        );
      }
    };

    fetchProducts();
  }, [page, size]);

  useEffect(() => {
    const fetchLikedProducts = async () => {
      try {
        const response = await axiosInstance.get('/v1/products/likes', {
          params: {
            page,
            size,
          },
        });
        setLikedProducts(response.data.data.content);
      } catch (err) {
        alert("좋아하는 내 상품 목록을 가져오는 중 오류가 발생했습니다.");
        setLikedError(
            err.response ? err.response.data : '좋아하는 내 상품 목록을 가져오는 중 오류가 발생했습니다.'
        );
      }
    };

    fetchLikedProducts();
  }, [page, size]);

  useEffect(() => {
    const fetchAuctions = async () => {
      try {
        const response = await axiosInstance.get('/v1/auctions', {
          params: {
            page,
            size,
          },
        });
        setAuctions(response.data.data.content);
      } catch (err) {
        alert("경매 목록을 가져오는 중 오류가 발생했습니다.");
        setAuctionError(
            err.response ? err.response.data : '경매 목록을 가져오는 중 오류가 발생했습니다.'
        );
      }
    };

    fetchAuctions();
  }, [page, size]);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleProfileUpload = async (e) => {
    e.preventDefault();

    if (!file) {
      setUploadError('업로드할 파일을 선택해주세요.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await axiosInstance.post('/v1/s3/upload/profile', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      setUploadSuccess(true);
      setProfile((prevProfile) => ({
        ...prevProfile,
        imagesUrl: response.data.imageUrl,
      }));
      setUploadError(null);

    } catch (err) {
      if (err.response && err.response.status === 409) {
        setUploadError('이미 존재하는 이미지입니다.');
      } else {
        alert('프로필 업로드 중 오류가 발생했습니다.');
        setUploadError(err.response ? err.response.data : '프로필 업로드 중 오류가 발생했습니다.');
      }
      setUploadSuccess(false);
    }
    window.location.reload();
  };

  const handleChange = (event, newValue) => {
    setTabValue(newValue);
  };

  const handleEditToggle = () => {
    setEditMode(!editMode);
  };

  const handleProfileChange = (e) => {
    const { name, value } = e.target;
    setProfile((prevProfile) => ({
      ...prevProfile,
      [name]: value,
    }));
  };

  const handleProfileUpdate = async () => {
    try {
      await axiosInstance.put('/update', profile);
      alert('프로필이 성공적으로 업데이트되었습니다.');
      setEditMode(false);
    } catch (error) {
      alert('프로필 업데이트 중 오류가 발생했습니다.');
    }
  };

  return (
      <Container component="main" maxWidth="lg" sx={{ mt: 8 }}>
        <Box sx={{ mt: 4, mb: 4 }}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={4}>
              <Paper
                  sx={{
                    p: 2,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    height: '100%',
                  }}
              >
                {profile && (
                    <>
                      <Avatar
                          src={profileImageUrl}
                          sx={{ width: 120, height: 120, bgcolor: 'grey.500' }}
                      />
                      <Box sx={{ mt: 2, display: 'flex', alignItems: 'center', flexDirection: 'column' }}>
                        <input
                            accept="image/*"
                            style={{ display: 'none' }}
                            id="profile-image-upload"
                            type="file"
                            onChange={handleFileChange}
                        />
                        <label htmlFor="profile-image-upload">
                          <IconButton color="primary" component="span">
                            <PhotoCamera sx={{ color: '#757575' }} />
                          </IconButton>
                        </label>
                        {file && (
                            <Button variant="text" onClick={handleProfileUpload} sx={{ color: '#757575', mt: 1 }}>
                              업로드
                            </Button>
                        )}
                      </Box>
                      {uploadSuccess && (
                          <Typography variant="body2" color="green">
                            프로필 사진이 성공적으로 업로드되었습니다.
                          </Typography>
                      )}
                      {uploadError && (
                          <Typography variant="body2" color="red">
                            오류: {uploadError}
                          </Typography>
                      )}
                    </>
                )}
              </Paper>
            </Grid>
            <Grid item xs={12} md={8}>
              <Paper sx={{ p: 2, height: '100%', position: 'relative' }}>
                <Typography variant="h6" sx={{ mb: 2, fontSize: '16px', fontWeight: 'bold', color: '#262626' }}>
                  ITSYOU
                </Typography>
                <IconButton
                    onClick={handleEditToggle}
                    sx={{
                      position: 'absolute',
                      top: 16,
                      right: 16,
                      color: '#757575',
                    }}
                >
                  <EditIcon />
                </IconButton>
                {profileError && (
                    <Typography variant="body1" color="red">
                      오류: {profileError}
                    </Typography>
                )}
                {profile ? (
                    <>
                      {!editMode ? (
                          <>
                            <Typography variant="body1" sx={{ mb: 1, fontSize: '14px', color: '#262626' }}>
                              <strong>사용자 이름:</strong> {profile.username}
                            </Typography>
                            <Typography variant="body1" sx={{ mb: 1, fontSize: '14px', color: '#262626' }}>
                              <strong>이름:</strong> {profile.name}
                            </Typography>
                            <Typography variant="body1" sx={{ mb: 1, fontSize: '14px', color: '#262626' }}>
                              <strong>닉네임:</strong> {profile.nickname}
                            </Typography>
                            <Typography variant="body1" sx={{ mb: 1, fontSize: '14px', color: '#262626' }}>
                              <strong>이메일:</strong> {profile.email}
                            </Typography>
                            <Typography variant="body1" sx={{ mb: 1, fontSize: '14px', color: '#262626' }}>
                              <strong>주소:</strong> {profile.address}
                            </Typography>
                          </>
                      ) : (
                          <>
                            <TextField
                                label="닉네임"
                                name="nickname"
                                value={profile.nickname}
                                onChange={handleProfileChange}
                                fullWidth
                                sx={{ mb: 2, '& .MuiInputBase-input': { fontSize: '14px' } }}
                            />
                            <TextField
                                label="이메일"
                                name="email"
                                value={profile.email}
                                onChange={handleProfileChange}
                                fullWidth
                                sx={{ mb: 2, '& .MuiInputBase-input': { fontSize: '14px' } }}
                            />
                            <TextField
                                label="주소"
                                name="address"
                                value={profile.address}
                                onChange={handleProfileChange}
                                fullWidth
                                sx={{ mb: 2, '& .MuiInputBase-input': { fontSize: '14px' } }}
                            />
                            <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 1 }}>
                              <Button variant="text" onClick={handleProfileUpdate} sx={{ color: '#757575' }}>
                                저장
                              </Button>
                              <Button variant="text" onClick={handleEditToggle} sx={{ color: '#757575' }}>
                                취소
                              </Button>
                            </Box>
                          </>
                      )}
                    </>
                ) : (
                    <Typography variant="body1">로딩 중...</Typography>
                )}
              </Paper>
            </Grid>
          </Grid>
          <Box sx={{ mt: 4 }}>
            <Tabs
                value={tabValue}
                onChange={handleChange}
                centered
                sx={{
                  borderBottom: '1px solid #ddd',
                  '& .MuiTab-root.Mui-selected': {
                    color: '#262626', // 진한 검정색 글씨
                  },
                  '& .MuiTab-root': {
                    fontSize: '14px',
                    color: '#8e8e8e',
                  },
                }}
                TabIndicatorProps={{
                  style: {
                    backgroundColor: '#262626', // 진한 검정색 인디케이터
                  },
                }}
            >
              <Tab label="내 상품 목록" />
              <Tab label="좋아하는 제품" />
              <Tab label="경매 목록" />
            </Tabs>
            <Box sx={{ p: 2 }}>
              {tabValue === 0 && (
                  <Grid container spacing={2}>
                    {productError && (
                        <Typography variant="body1" color="red">
                          오류: {productError}
                        </Typography>
                    )}
                    {products.map((product) => (
                        <Grid item xs={12} sm={6} md={4} key={product.id}>
                          <Paper sx={{ p: 2 }}>
                            {product.imagesUrl && product.imagesUrl.length > 0 && (
                                <img
                                    src={product.imagesUrl[0]}
                                    alt={product.productName}
                                    style={{ width: '100%', height: 'auto', objectFit: 'cover', aspectRatio: '1/1' }}
                                />
                            )}
                            <Typography variant="h6" sx={{ fontSize: '14px', fontWeight: 'bold', color: '#262626' }}>
                              {product.productName}
                            </Typography>
                            {/* 상품 설명 텍스트 숨김 */}
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>시작 가격:</strong> {product.startPrice}원
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>현재 가격:</strong> {product.currentPrice}원
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>즉시 구매가:</strong> {product.auctionNowPrice}원
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>마감일:</strong> {new Date(product.dueDate).toLocaleString()}
                            </Typography>
                          </Paper>
                        </Grid>
                    ))}
                  </Grid>
              )}
              {tabValue === 1 && (
                  <Grid container spacing={2}>
                    {likedError && (
                        <Typography variant="body1" color="red">
                          오류: {likedError}
                        </Typography>
                    )}
                    {likedProducts.map((product) => (
                        <Grid item xs={12} sm={6} md={4} key={product.id}>
                          <Paper sx={{ p: 2 }}>
                            {product.imagesUrl && product.imagesUrl.length > 0 && (
                                <img
                                    src={product.imagesUrl[0]}
                                    alt={product.productName}
                                    style={{ width: '100%', height: 'auto', objectFit: 'cover', aspectRatio: '1/1' }}
                                />
                            )}
                            <Typography variant="h6" sx={{ fontSize: '14px', fontWeight: 'bold', color: '#262626' }}>
                              {product.productName}
                            </Typography>
                            {/* 상품 설명 텍스트 숨김 */}
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>시작 가격:</strong> {product.startPrice}원
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>현재 가격:</strong> {product.currentPrice}원
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>즉시 구매가:</strong> {product.auctionNowPrice}원
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>마감일:</strong> {new Date(product.dueDate).toLocaleString()}
                            </Typography>
                          </Paper>
                        </Grid>
                    ))}
                  </Grid>
              )}
              {tabValue === 2 && (
                  <Grid container spacing={2}>
                    {auctionError && (
                        <Typography variant="body1" color="red">
                          오류: {auctionError}
                        </Typography>
                    )}
                    {auctions.map((auction) => (
                        <Grid item xs={12} sm={6} md={4} key={auction.id}>
                          <Paper sx={{ p: 2 }}>
                            <Typography variant="h6" sx={{ fontSize: '14px', fontWeight: 'bold', color: '#262626' }}>
                              상품: {auction.productName}
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>입찰자 이름:</strong> {auction.username}
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '12px', color: '#8e8e8e' }}>
                              <strong>입찰 가격:</strong> {auction.bidPrice}원
                            </Typography>
                          </Paper>
                        </Grid>
                    ))}
                  </Grid>
              )}
            </Box>
          </Box>
        </Box>
      </Container>
  );
};

export default Profile;