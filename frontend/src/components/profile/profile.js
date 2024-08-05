import React, {useEffect, useState} from 'react';
import {
  Avatar,
  Box,
  Button,
  Container,
  Grid,
  IconButton,
  Paper,
  Tab,
  Tabs,
  Typography,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import PhotoCamera from '@mui/icons-material/PhotoCamera';
import axiosInstance from '../../api/axiosInstance';
import {useNavigate} from 'react-router-dom';

const Profile = () => {
  const navigate = useNavigate();

  // 사용자 프로필 상태 관리
  const [profile, setProfile] = useState(null);
  const [profileError, setProfileError] = useState(null);
  const [file, setFile] = useState(null);
  const [uploadError, setUploadError] = useState(null);
  const [uploadSuccess, setUploadSuccess] = useState(false);

  // 내 상품 목록 상태 관리
  const [products, setProducts] = useState([]);
  const [productError, setProductError] = useState(null);

  // 좋아요한 내 상품 목록 상태 관리
  const [likedProducts, setLikedProducts] = useState([]);
  const [likedError, setLikedError] = useState(null);

  // 경매 목록 상태 관리
  const [auctions, setAuctions] = useState([]);
  const [auctionError, setAuctionError] = useState(null);

  // 탭 상태 관리
  const [tabValue, setTabValue] = useState(0);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);

  // 기본 프로필 이미지 URL 설정
  const defaultProfileImageUrl = '/images/default-profile.png';

  // 프로필 이미지 URL 상태 관리
  const [profileImageUrl, setProfileImageUrl] = useState(
      defaultProfileImageUrl
  );

  // 사용자 프로필 데이터를 가져오는 함수
  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        const response = await axiosInstance.get('/users/profile');
        const userData = response.data.data;
        setProfile(userData);

        // 이미지 URL이 있는 경우 설정, 없으면 기본 이미지 사용
        if (userData.imageUrls && userData.imageUrls.length > 0) {
          setProfileImageUrl(userData.imageUrls[0]); // 첫 번째 이미지를 프로필 이미지로 사용
        } else {
          setProfileImageUrl(defaultProfileImageUrl); // 기본 이미지 사용
        }
      } catch (err) {
        alert('프로필 정보를 가져오는 중 오류가 발생했습니다.');
        setProfileError(
            err.response
                ? err.response.data
                : '프로필 정보를 가져오는 중 오류가 발생했습니다.'
        );
      }
    };

    fetchUserProfile();
  }, []);

  // 내 상품 목록을 가져오는 함수
  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await axiosInstance.get('/products/user', {
          params: {
            page,
            size,
          },
        });
        setProducts(response.data.data.content);
      } catch (err) {
        alert('내 상품 목록을 가져오는 중 오류가 발생했습니다.');
        setProductError(
            err.response
                ? err.response.data
                : '내 상품 목록을 가져오는 중 오류가 발생했습니다.'
        );
      }
    };

    fetchProducts();
  }, [page, size]);

  // 좋아요한 내 상품 목록을 가져오는 함수
  useEffect(() => {
    const fetchLikedProducts = async () => {
      try {
        const response = await axiosInstance.get('/products/likes', {
          params: {
            page,
            size,
          },
        });
        setLikedProducts(response.data.data.content);
      } catch (err) {
        alert('좋아하는 내 상품 목록을 가져오는 중 오류가 발생했습니다.');
        setLikedError(
            err.response
                ? err.response.data
                : '좋아하는 내 상품 목록을 가져오는 중 오류가 발생했습니다.'
        );
      }
    };

    fetchLikedProducts();
  }, [page, size]);

  // 경매 목록을 가져오는 함수
  useEffect(() => {
    const fetchAuctions = async () => {
      try {
        const response = await axiosInstance.get('/auctions', {
          params: {
            page,
            size,
          },
        });
        setAuctions(response.data.data.content);
      } catch (err) {
        alert('경매 목록을 가져오는 중 오류가 발생했습니다.');
        setAuctionError(
            err.response
                ? err.response.data
                : '경매 목록을 가져오는 중 오류가 발생했습니다.'
        );
      }
    };

    fetchAuctions();
  }, [page, size]);

  // 파일 입력 변경 처리 함수
  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  // 프로필 사진 업로드 함수
  const handleProfileUpload = async (e) => {
    e.preventDefault();

    if (!file) {
      setUploadError('업로드할 파일을 선택해주세요.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);

    try {
      const response = await axiosInstance.post('/s3/upload/profile', formData,
          {
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
      navigate(0); // 프로필 페이지로 리다이렉트하여 이미지 업데이트를 확인
    } catch (err) {
      if (err.response && err.response.status === 409) {
        setUploadError('이미 존재하는 이미지입니다.');
      } else {
        alert('프로필 업로드 중 오류가 발생했습니다.');
        setUploadError(
            err.response
                ? err.response.data
                : '프로필 업로드 중 오류가 발생했습니다.'
        );
      }
      setUploadSuccess(false);
    }
  };

  // 탭 변경 핸들러
  const handleChange = (event, newValue) => {
    setTabValue(newValue);
  };

  return (
      <Container component="main" maxWidth="lg" sx={{mt: 8}}>
        <Box sx={{mt: 4, mb: 4}}>
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
                    position: 'relative',
                  }}
              >
                {profile && (
                    <>
                      <Avatar
                          src={profileImageUrl} // 변경된 프로필 이미지 URL 사용
                          sx={{width: 120, height: 120, bgcolor: 'grey.500'}}
                      />
                      <IconButton
                          sx={{
                            position: 'absolute',
                            top: 10,
                            right: 10,
                            color: 'black',
                          }}
                          onClick={handleProfileUpload}
                      >
                        <EditIcon/>
                      </IconButton>
                      <Box sx={{mt: 2}}>
                        <input
                            accept="image/*"
                            style={{display: 'none'}}
                            id="profile-image-upload"
                            type="file"
                            onChange={handleFileChange}
                        />
                        <label htmlFor="profile-image-upload">
                          <IconButton color="primary" component="span">
                            <PhotoCamera/>
                          </IconButton>
                        </label>
                        {file && (
                            <Button variant="contained"
                                    onClick={handleProfileUpload}>
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
              <Paper sx={{p: 2, height: '100%', position: 'relative'}}>
                <Typography variant="h6" sx={{mb: 2}}>
                  사용자 정보
                </Typography>
                {profileError && (
                    <Typography variant="body1" color="red">
                      오류: {profileError}
                    </Typography>
                )}
                {profile ? (
                    <>
                      <Typography variant="body1" sx={{mb: 1}}>
                        <strong>사용자 이름:</strong> {profile.username}
                      </Typography>
                      <Typography variant="body1" sx={{mb: 1}}>
                        <strong>이름:</strong> {profile.name}
                      </Typography>
                      <Typography variant="body1" sx={{mb: 1}}>
                        <strong>닉네임:</strong> {profile.nickname}
                      </Typography>
                      <Typography variant="body1" sx={{mb: 1}}>
                        <strong>이메일:</strong> {profile.email}
                      </Typography>
                      <Typography variant="body1" sx={{mb: 1}}>
                        <strong>주소:</strong> {profile.address}
                      </Typography>
                    </>
                ) : (
                    <Typography variant="body1">로딩 중...</Typography>
                )}
              </Paper>
            </Grid>
          </Grid>
          <Box sx={{mt: 4}}>
            <Tabs value={tabValue} onChange={handleChange} centered>
              <Tab label="내 상품 목록"/>
              <Tab label="좋아하는 제품"/>
              <Tab label="경매 목록"/>
            </Tabs>
            <Box sx={{mt: 2}}>
              {tabValue === 0 && (
                  <Grid container spacing={2}>
                    {productError && (
                        <Typography variant="body1" color="red">
                          오류: {productError}
                        </Typography>
                    )}
                    {products.map((product) => (
                        <Grid item xs={12} sm={6} md={4} key={product.id}>
                          <Paper sx={{p: 2}}>
                            {product.imagesUrl && product.imagesUrl.length > 0
                                && (
                                    <img
                                        src={product.imagesUrl[0]}
                                        alt={product.productName}
                                        style={{width: '100%', height: 'auto'}}
                                    />
                                )}
                            <Typography
                                variant="h6">{product.productName}</Typography>
                            <Typography
                                variant="body2">{product.description}</Typography>
                            <Typography variant="body2">
                              <strong>시작 가격:</strong> {product.startPrice}원
                            </Typography>
                            <Typography variant="body2">
                              <strong>현재 가격:</strong> {product.currentPrice}원
                            </Typography>
                            <Typography variant="body2">
                              <strong>즉시 구매가:</strong> {product.auctionNowPrice}원
                            </Typography>
                            <Typography variant="body2">
                              <strong>마감일:</strong> {new Date(
                                product.dueDate).toLocaleString()}
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
                          <Paper sx={{p: 2}}>
                            {product.imagesUrl && product.imagesUrl.length > 0
                                && (
                                    <img
                                        src={product.imagesUrl[0]}
                                        alt={product.productName}
                                        style={{width: '100%', height: 'auto'}}
                                    />
                                )}
                            <Typography
                                variant="h6">{product.productName}</Typography>
                            <Typography
                                variant="body2">{product.description}</Typography>
                            <Typography variant="body2">
                              <strong>시작 가격:</strong> {product.startPrice}원
                            </Typography>
                            <Typography variant="body2">
                              <strong>현재 가격:</strong> {product.currentPrice}원
                            </Typography>
                            <Typography variant="body2">
                              <strong>즉시 구매가:</strong> {product.auctionNowPrice}원
                            </Typography>
                            <Typography variant="body2">
                              <strong>마감일:</strong> {new Date(
                                product.dueDate).toLocaleString()}
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
                          <Paper sx={{p: 2}}>
                            <Typography
                                variant="h6">상품: {auction.productName}</Typography>
                            <Typography variant="body2">
                              <strong>입찰자 이름:</strong> {auction.username}
                            </Typography>
                            <Typography variant="body2">
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
