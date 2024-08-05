import React, {useEffect, useState} from 'react';
import './App.css';
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import SignUp from "./components/auth/Signup";
import Main from "./main/Main";
import Footer from "./components/footer/Footer";
import Nav from "./components/nav/Nav";
import CreateProduct from "./components/createproduct/CreateProduct";
import {ThemeProvider} from "styled-components";
import SignIn from "./components/auth/Signin";
import theme from "./styles/theme";
import ItemList from "./components/item/ItemList";
import AuctionComponent from "./components/Auction/AuctionComponent";
import Profile from "./components/profile/profile";
import Item from "./components/item/Item";

function App() {
  useEffect(() => {
    const token = localStorage.getItem('Authorization');
    if (token) {
      setIsLoggedIn(true);
    }
  }, []);

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    localStorage.removeItem('Authorization'); // 토큰 삭제
  };

  return (
      <ThemeProvider theme={theme}>
        <BrowserRouter>
          <Nav/>
          <Routes>
            <Route path="/itsmine" element={<Main/>}/>
            <Route path="/itsmine/login"
                   element={<SignIn isLoggedIn={isLoggedIn}
                                    onLogin={handleLogin}/>}/>
            <Route path="/signup" element={<SignUp/>}/>
            <Route path="/products" element={<CreateProduct/>}/>
            <Route path="/items" element={<ItemList/>}/>
            <Route path="/item" element={<Item/>}/>
            <Route path="/products/:productId" element={<AuctionComponent/>}/>
            <Route path="/profile" element={<Profile/>}/>
          </Routes>
          <Footer/>
        </BrowserRouter>
      </ThemeProvider>
  );
}

export default App;
