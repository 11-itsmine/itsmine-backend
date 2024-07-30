import React, {useEffect, useState} from 'react';
import axiosInstance from '../../api/axiosInstance';
import {Box, Container, Grid, Typography} from '@mui/material';

const Hotdeal = () => {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    axiosInstance.get('path_to_your_backend_api') // 백엔드 API 경로로 변경하세요.
    .then(response => {
      setProducts(response.data);
    })
    .catch(error => {
      console.error('Error fetching product data:', error);
    });
  }, []);

  return (
      <Container>
        <Box sx={{
          borderBottom: 1,
          borderColor: 'divider',
          display: 'flex',
          justifyContent: 'space-around',
          padding: 2
        }}>
          <Typography>랭킹</Typography>
          <Typography>실시간경매</Typography>
          <Typography>판매하기</Typography>
          <Typography>급처</Typography>
        </Box>
        <Grid container spacing={2} sx={{marginTop: 2}}>
          {products.map((product, index) => (
              <Grid item xs={12} sm={6} md={4} key={index}>
                <Box sx={{
                  border: '1px solid #ddd',
                  padding: 2,
                  textAlign: 'center'
                }}>
                  <img src={product.image} alt={product.name}
                       style={{width: '100%', height: 'auto'}}/>
                  <Typography>상품명: {product.name}</Typography>
                  <Typography sx={{
                    color: 'red',
                    fontWeight: 'bold'
                  }}>{product.price}</Typography>
                </Box>
              </Grid>
          ))}
        </Grid>
      </Container>
  );
};

export default Hotdeal;
