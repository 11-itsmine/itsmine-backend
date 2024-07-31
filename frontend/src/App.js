import React, {useEffect, useState} from 'react';
import './App.css';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import SignIn from "./Components/auth/Signin";
import SignUp from "./Components/auth/Signup";
import Main from "./main/Main";
import Footer from "./Components/footer/Footer";
import Nav from "./Components/nav/Nav";

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
      <BrowserRouter>
        <Nav/>
        <Routes>
          <Route path="/"
                 element={isLoggedIn ? <Navigate to="/itsmine "/> : <SignIn
                     onLogin={handleLogin}/>}/>
          <Route path="/" element={<SignIn/>}/>
          <Route path="/signup" element={<SignUp/>}/>
          <Route path="/itsmine" element={<Main/>}/>
        </Routes>
        <Footer/>
      </BrowserRouter>
  );
}

export default App;
