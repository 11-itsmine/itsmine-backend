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
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import axiosInstance from '../../api/axiosInstance'; // axiosInstance import
import { useNavigate } from 'react-router-dom';
import PhotoCamera from '@mui/icons-material/PhotoCamera';

const MyProfile = () => {
    const [userInfo, setUserInfo] = useState({});
    const [tradeList, setTradeList] = useState([]);
    const [sellingList, setSellingList] = useState([]);
    const [likedItems, setLikedItems] = useState([]);
    const [tabValue, setTabValue] = useState(0);
    const [likePage, setLikePage] = useState(0);
    const [hasMoreLikes, setHasMoreLikes] = useState(true);
    const [selectedFile, setSelectedFile] = useState(null);
    const navigate = useNavigate();

    // 사용자 정보를 가져오는 함수
    const fetchUserInfo = async () => {
        try {
            const response = await axiosInstance.get('/users/profile');
            setUserInfo(response.data);
        } catch (error) {
            console.error('Failed to fetch user info:', error);
        }
    };

    // 거래 목록을 가져오는 함수
    const fetchTradeList = async () => {
        try {
            const response = await axiosInstance.get('/api/user/trade-list');
            setTradeList(response.data);
        } catch (error) {
            console.error('Failed to fetch trade list:', error);
        }
    };

    // 판매 목록을 가져오는 함수
    const fetchSellingList = async () => {
        try {
            const response = await axiosInstance.get('/api/user/selling-list');
            setSellingList(response.data);
        } catch (error) {
            console.error('Failed to fetch selling list:', error);
        }
    };

    // 좋아요한 항목을 가져오는 함수
    const fetchLikedItems = async (page) => {
        try {
            const response = await axiosInstance.get('/products/likes', {
                params: { page, size: 3 },
            });
            setLikedItems((prevItems) => [...prevItems, ...response.data.data.content]);
            setHasMoreLikes(response.data.data.totalPages > page + 1);
        } catch (error) {
            console.error('Failed to fetch liked items:', error);
        }
    };

    // 초기 데이터 로드
    useEffect(() => {
        fetchUserInfo();
        fetchTradeList();
        fetchSellingList();
    }, []);

    // 탭 변경 시 좋아요한 항목을 다시 가져옴
    useEffect(() => {
        if (tabValue === 0) {
            fetchLikedItems(likePage);
        }
    }, [tabValue, likePage]);

    // 탭 변경 핸들러
    const handleChange = (event, newValue) => {
        setTabValue(newValue);
        if (newValue === 0 && likedItems.length === 0) {
            setLikePage(0);
        }
    };

    // 프로필 수정 페이지로 이동
    const handleEditProfile = () => {
        navigate('/edit-profile');
    };

    // 파일 선택 시 호출되는 핸들러
    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    // 프로필 이미지 업로드
    const handleUploadProfileImage = async () => {
        if (!selectedFile) {
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedFile);
        formData.append('userId', userInfo.id);

        try {
            const response = await axiosInstance.post('/upload/profile', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });

            // 업로드 후 사용자 정보 갱신
            await fetchUserInfo();
            alert('프로필 이미지가 성공적으로 업로드되었습니다.');
        } catch (error) {
            console.error('프로필 이미지 업로드 실패:', error);
            alert('프로필 이미지 업로드 중 오류가 발생했습니다.');
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
                                position: 'relative',
                            }}
                        >
                            <Avatar
                                src={userInfo.profileImageUrl} // 프로필 이미지 URL
                                sx={{ width: 120, height: 120, bgcolor: 'grey.500' }}
                            />
                            <IconButton
                                sx={{ position: 'absolute', top: 10, right: 10, color: 'black' }}
                                onClick={handleEditProfile}
                            >
                                <EditIcon />
                            </IconButton>
                            <Box sx={{ mt: 2 }}>
                                <input
                                    accept="image/*"
                                    style={{ display: 'none' }}
                                    id="profile-image-upload"
                                    type="file"
                                    onChange={handleFileChange}
                                />
                                <label htmlFor="profile-image-upload">
                                    <IconButton color="primary" component="span">
                                        <PhotoCamera />
                                    </IconButton>
                                </label>
                                {selectedFile && (
                                    <Button variant="contained" onClick={handleUploadProfileImage}>
                                        업로드
                                    </Button>
                                )}
                            </Box>
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={8}>
                        <Paper sx={{ p: 2, height: '100%', position: 'relative' }}>
                            <IconButton
                                sx={{ position: 'absolute', top: 10, right: 10, color: 'black' }}
                                onClick={handleEditProfile}
                            >
                                <EditIcon />
                            </IconButton>
                            <Typography variant="h6" sx={{ mb: 2 }}>
                                {userInfo.username || 'username'}
                            </Typography>
                            <Typography variant="body1" sx={{ mb: 1 }}>
                                닉네임: {userInfo.nickname || 'nickname'}
                            </Typography>
                            <Typography variant="body1" sx={{ mb: 1 }}>
                                이메일: {userInfo.email || 'email'}
                            </Typography>
                            <Typography variant="body1" sx={{ mb: 1 }}>
                                주소: {userInfo.address || 'address'}
                            </Typography>
                        </Paper>
                    </Grid>
                </Grid>
                <Box sx={{ mt: 4 }}>
                    <Tabs value={tabValue} onChange={handleChange} centered>
                        <Tab label="좋아요" />
                        <Tab label="내 판매 목록" />
                        <Tab label="내 거래 목록" />
                    </Tabs>
                    <Box sx={{ mt: 2 }}>
                        {tabValue === 0 && (
                            <Grid container spacing={2}>
                                {likedItems.map((item, index) => (
                                    <Grid item xs={12} sm={6} md={4} key={index}>
                                        <Paper sx={{ p: 2 }}>
                                            <img
                                                src={item.imagesUrl[0]}
                                                alt={item.productName}
                                                style={{ width: '100%', height: 'auto' }}
                                            />
                                            <Typography>{item.productName}</Typography>
                                        </Paper>
                                    </Grid>
                                ))}
                                {hasMoreLikes && (
                                    <Grid item xs={12}>
                                        <Button onClick={() => setLikePage((prev) => prev + 1)}>
                                            더 보기
                                        </Button>
                                    </Grid>
                                )}
                            </Grid>
                        )}
                        {tabValue === 1 && (
                            <Grid container spacing={2}>
                                {sellingList.map((item, index) => (
                                    <Grid item xs={12} sm={6} md={4} key={index}>
                                        <Paper sx={{ p: 2 }}>{item.name}</Paper>
                                    </Grid>
                                ))}
                            </Grid>
                        )}
                        {tabValue === 2 && (
                            <Grid container spacing={2}>
                                {tradeList.map((item, index) => (
                                    <Grid item xs={12} sm={6} md={4} key={index}>
                                        <Paper sx={{ p: 2 }}>{item.name}</Paper>
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

export default MyProfile;
