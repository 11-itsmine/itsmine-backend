import React, {useState} from 'react';
import './App.css';
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import SignIn from "./components/auth/Signin";
import SignUp from "./components/auth/Signup";
import Hotdeal from "./components/hotDeal/Hotdeal";

function App() {

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
        <Routes>
          <Route path="/"
                 element={isLoggedIn ? <Navigate to="/board"/> : <SignIn
                     onLogin={handleLogin}/>}/>
          <Route path="/" element={<SignIn/>}/>
          <Route path="/signup" element={<SignUp/>}/>
          <Route path="/itsmine" element={<Hotdeal onLogout={handleLogout}/>}/>
        </Routes>
      </BrowserRouter>
  );
}

export default App;
