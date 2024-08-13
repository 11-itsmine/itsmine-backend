import React, { useEffect, useState } from 'react';
import './App.css';
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import SignUp from "./components/auth/Signup";
import Main from "./main/Main";
import Footer from "./components/footer/Footer";
import Nav from "./components/nav/Nav";
import CreateProduct from "./components/createproduct/CreateProduct";
import styled, { ThemeProvider } from "styled-components";
import SignIn from "./components/auth/Signin";
import theme from "./styles/theme";
import ItemList from "./components/item/ItemList";
import AuctionComponent from "./components/Auction/AuctionComponent";
import Profile from "./components/profile/profile";
import Item from "./components/item/Item";
import KakaoCallback from "./api/KakaoCallback";
import AdminPage from "./components/backOffice/AdminPage";
import axiosInstance from './api/axiosInstance';
import LoadingScreen from './components/LoadingScreen';
import ProductEditPage from "./components/createproduct/ProductEditPage"; // 로딩 페이지 추가
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom'; // Navigate 임포트 추가

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userRole, setUserRole] = useState('');
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태 추가

  useEffect(() => {
    const token = localStorage.getItem('Authorization');
    if (token) {
      setIsLoggedIn(true);
      fetchUserRole(token);
    }
  }, []);

  const fetchUserRole = async (token) => {
    try {
      console.log('Fetching user role with token:', token);
      const response = await axiosInstance.get('/v1/users/role', {
        headers: {
          'Authorization': token.trim()
        }
      });
      console.log('User role response:', response.data);
      setUserRole(response.data.userRole);
    } catch (error) {
      console.error('Failed to fetch user role:', error);
    }
  };

  const handleLogin = () => {
    setIsLoggedIn(true);
    const token = localStorage.getItem('Authorization');
    if (token) {
      fetchUserRole(token);
    }
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    localStorage.removeItem('Authorization');
    setUserRole('');
  };

  const finishLoading = () => {
    setIsLoading(false);
  };

  if (isLoading) {
    return <LoadingScreen onFinish={finishLoading} />; // 로딩 중일 때 로딩 페이지 표시
  }

  return (
      <ThemeProvider theme={theme}>
        <BrowserRouter>
          <Nav userRole={userRole} />
          <MainWrapper>
            <Routes>
              <Route path="/" element={<Navigate to="/itsmine" />} />
              <Route path="/itsmine" element={<Main />} />
              <Route path="/itsmine/login" element={<SignIn isLoggedIn={isLoggedIn} onLogin={handleLogin} />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/oauth/callback/kakao" element={<KakaoCallback isLoggedIn={isLoggedIn} onLogin={handleLogin} />} />
              <Route path="/products" element={<CreateProduct />} />
              <Route path="/edit-product/:productId" element={<ProductEditPage />} />
              <Route path="/items" element={<ItemList />} />
              <Route path="/item" element={<Item />} />
              <Route path="/products/:productId" element={<AuctionComponent />} />
              <Route path="/profile" element={<Profile />} />
              {userRole === 'MANAGER' && (
                  <Route path="/admin" element={<AdminPage />} />
              )}
            </Routes>
            <Footer />
          </MainWrapper>
        </BrowserRouter>
      </ThemeProvider>
  );
}

export default App;

const MainWrapper = styled.div`
  margin-top: 5rem;
`;