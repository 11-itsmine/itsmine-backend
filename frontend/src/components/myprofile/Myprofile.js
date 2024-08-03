import React, { useEffect, useState } from 'react';
import { Avatar, Box, Container, Grid, Typography, Paper, Tabs, Tab, Button, IconButton } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import axiosInstance from '../../api/axiosInstance';
import { useNavigate } from 'react-router-dom';

const Myprofile = () => {
    const [userInfo, setUserInfo] = useState({});
    const [tradeList, setTradeList] = useState([]);
    const [sellingList, setSellingList] = useState([]);
    const [likedItems, setLikedItems] = useState([]);
    const [tabValue, setTabValue] = useState(0);
    const [likePage, setLikePage] = useState(0);
    const [hasMoreLikes, setHasMoreLikes] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const response = await axiosInstance.get(`/api/user/me`);
                setUserInfo(response.data);
            } catch (error) {
                console.error('Failed to fetch user info:', error);
            }
        };

        const fetchTradeList = async () => {
            try {
                const response = await axiosInstance.get('/api/user/trade-list');
                setTradeList(response.data);
            } catch (error) {
                console.error('Failed to fetch trade list:', error);
            }
        };

        const fetchSellingList = async () => {
            try {
                const response = await axiosInstance.get('/api/user/selling-list');
                setSellingList(response.data);
            } catch (error) {
                console.error('Failed to fetch selling list:', error);
            }
        };

        fetchUserInfo();
        fetchTradeList();
        fetchSellingList();
    }, []);

    useEffect(() => {
        if (tabValue === 0) {
            fetchLikedItems(likePage);
        }
    }, [tabValue, likePage]);

    const fetchLikedItems = async (page) => {
        try {
            const response = await axiosInstance.get('/products/likes', {
                params: { page, size: 3 },
            });
            setLikedItems(prevItems => [...prevItems, ...response.data.data.content]);
            setHasMoreLikes(response.data.data.totalPages > page + 1);
        } catch (error) {
            console.error('Failed to fetch liked items:', error);
        }
    };

    const handleChange = (event, newValue) => {
        setTabValue(newValue);
        if (newValue === 0 && likedItems.length === 0) {
            setLikePage(0);
        }
    };

    const handleEditProfile = () => {
        navigate('/edit-profile');
    };

    return (
        <Container component="main" maxWidth="lg" sx={{ mt: 8 }}>
            <Box sx={{ mt: 4, mb: 4 }}>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={4}>
                        <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', height: '100%', position: 'relative' }}>
                            <Avatar sx={{ width: 120, height: 120, bgcolor: 'grey.500' }} />
                            <IconButton
                                sx={{ position: 'absolute', top: 10, right: 10, color: 'black' }}
                                onClick={handleEditProfile}
                            >
                                <EditIcon />
                            </IconButton>
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
                            <Typography variant="h6" sx={{ mb: 2 }}>{userInfo.username || 'username'}</Typography>
                            <Typography variant="body1" sx={{ mb: 1 }}>Nickname: {userInfo.nickname || 'nickname'}</Typography>
                            <Typography variant="body1" sx={{ mb: 1 }}>Email: {userInfo.email || 'email'}</Typography>
                            <Typography variant="body1" sx={{ mb: 1 }}>Address: {userInfo.address || 'address'}</Typography>
                        </Paper>
                    </Grid>
                </Grid>
                <Box sx={{ mt: 4 }}>
                    <Tabs value={tabValue} onChange={handleChange} centered>
                        <Tab label="Like" />
                        <Tab label="My Selling List" />
                        <Tab label="My Trade List" />
                    </Tabs>
                    <Box sx={{ mt: 2 }}>
                        {tabValue === 0 && (
                            <Grid container spacing={2}>
                                {likedItems.map((item, index) => (
                                    <Grid item xs={12} sm={6} md={4} key={index}>
                                        <Paper sx={{ p: 2 }}>
                                            <img src={item.imagesUrl[0]} alt={item.productName} style={{ width: '100%', height: 'auto' }} />
                                            <Typography>{item.productName}</Typography>
                                        </Paper>
                                    </Grid>
                                ))}
                                {hasMoreLikes && (
                                    <Grid item xs={12}>
                                        <Button onClick={() => setLikePage(prev => prev + 1)}>Load more</Button>
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

export default Myprofile;