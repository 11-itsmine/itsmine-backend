import {useState} from "react";
import {useNavigate} from 'react-router-dom';
import axiosInstance from "../../api/axiosInstance";

function Test({onLogin}) {
  const [loginRequest, setLoginRequest] = useState({
    username: '',
    password: ''
  });

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axiosInstance.post('/api/auth/login',
          loginRequest);

      // 로그인 성공 시 처리 로직
      console.log('Login successful!', response.data.AccessToken);

      // 토큰을 localStorage에 저장
      const token = response.data.accessToken
      localStorage.setItem('Authorization', token);

      // 부모 컴포넌트에 로그인 상태 변경 알리기
      onLogin();

      // 페이지를 리다이렉트하거나 상태를 업데이트할 수 있습니다.
      navigate('/board');
    } catch (error) {
      // 로그인 실패 시 처리 로직
      console.error('Login failed:', error);
      const errorMessage = JSON.parse(error.request.responseText).message;
      alert(`${errorMessage}`);
    }
  };

  const handleChange = (e) => {
    const {name, value} = e.target;
    setLoginRequest({...loginRequest, [name]: value});
  };

  const goToSignup = () => {
    navigate('/signup');
  };

  return (
      <form onSubmit={handleSubmit}>
        <h1>로그인 페이지</h1>
        <label>
          Username:
          <input type="text" name="username" value={loginRequest.username}
                 onChange={handleChange}/>
        </label>
        <br/>
        <label>
          Password:
          <input type="password" name="password" value={loginRequest.password}
                 onChange={handleChange}/>
        </label>
        <br/>
        <button type="submit">Login</button>
        <button type="button" onClick={goToSignup}>Signup</button>
      </form>
  );
}

export default Test;
